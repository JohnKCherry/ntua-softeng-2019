package gr.ntua.ece.softeng18b.api;

import java.util.Optional;

import org.restlet.data.Form;
import org.restlet.data.Header;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.model.User;

public class ProfileResource extends ServerResource {
	
	private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @SuppressWarnings({ "unused", "unchecked" })
	@Override
    protected Representation post(Representation entity) throws ResourceException {
    	//Create a new restlet form
        Form form = new Form(entity);
        //Read the parameters
        
        //authorization of user
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String user_token = headers.getFirstValue("X-OBSERVATORY-AUTH");
        if(user_token == null || user_token.isEmpty()) throw new ResourceException(401, "Not loged in");
        if(!dataAccess.isLogedIn(user_token))throw new ResourceException(401, "Not loged in");
        User profile = dataAccess.getUserProfile(user_token);
        return new JsonUserRepresentation(profile);
    }
    
    @SuppressWarnings("unchecked")
	@Override
    protected Representation patch(Representation entity) throws ResourceException {

        //Create a new restlet form
        Form form = new Form(entity);
        //Read the parameters
        String fullname = form.getFirstValue("fullname");
        String username = form.getFirstValue("username");
        String email = form.getFirstValue("email");
        String password = form.getFirstValue("password");
        String authorization = form.getFirstValue("authorization");
        String user_id = form.getFirstValue("user_id");
        
        //authorization of user
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String user_token = headers.getFirstValue("X-OBSERVATORY-AUTH");
        
        if(user_token == null || user_token.isEmpty()) throw new ResourceException(401, "Not loged in");
        if(!dataAccess.isLogedIn(user_token))throw new ResourceException(401, "Not loged in");
        //validate the values (in the general case)
        //We check to see wich parameter was given (while ensuring that it is the only one)
        String update_parameter, value;
        if(fullname!=null && username == null && email == null && password == null) {
        	update_parameter = "fullname";
        	value = fullname;
        }
        else if(username!=null && fullname == null && email == null && password == null) {
        	if(username.equals(user_token.substring(64))) throw new ResourceException(400,"This is username is already taken or you entered your old username!");
        	update_parameter = "username";
        	value = username;
        }
        else if(email!=null && fullname == null && username == null && password == null) {
        	//if(!name.matches(regex_s)) throw new ResourceException(400,"Bad parameter for category!");
        	update_parameter = "email";
        	value = email;
        }
        else if(password!=null && fullname == null && username == null && email == null) {
        	Optional<String> check_new = dataAccess.getUserApiToken(user_token.substring(64), password);
        	if(check_new != null && check_new.isPresent()) throw new ResourceException(400,"This is the old password! Please enter a new one");
        	update_parameter = "password";
        	value = password;
        }
        else if(user_id != null && authorization != null && password==null && fullname == null && username == null && email == null) {
            update_parameter = "authorization";
            if(!dataAccess.isAdmin(user_token)) throw new ResourceException(401,"You are not authorized for this action"); 
            value = authorization;
        }
        
        else  throw new ResourceException(400,"None or more than one values where found for patch request!");        

        try{
        	dataAccess.patchUser(user_token, update_parameter,value, user_id);
        	return new JsonMessageRepresentation("OK");
        }
        catch(org.springframework.dao.DuplicateKeyException e){
        	throw new ResourceException(400,"Input parameters have conflict with another product in the database");
        } 

        
    }
}
