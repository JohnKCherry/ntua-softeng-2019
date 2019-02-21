package gr.ntua.ece.softeng18b.api;

import java.util.HashMap;
import java.util.Map;

import gr.ntua.ece.softeng18b.conf.Configuration;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import gr.ntua.ece.softeng18b.data.DataAccess;

public class SignupResource extends ServerResource {

	private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();
	
    @Override
    protected Representation post(Representation entity) throws ResourceException {
    	//Create a new restlet form
        Form form = new Form(entity);
        //Read the parameters
        String fullname = form.getFirstValue("fullname");
        String username = form.getFirstValue("username");
        String password = form.getFirstValue("password");
        String email = form.getFirstValue("email");
        
        if(fullname == null || fullname.isEmpty()) 	throw new ResourceException(400, "Bad value for fullname");
        if(username == null || username.isEmpty()) 	throw new ResourceException(400, "Bad value for username");
        if(password == null || password.isEmpty()) 	throw new ResourceException(400, "Bad value for password");
        if(email == null 	|| email.isEmpty()) 	throw new ResourceException(400, "Bad value for e-mail");
        
        //By default, all new users are crowdsourcers (level 2 access)
        int auth = 2;
        
        try{
        	String message = dataAccess.addUser(fullname, username, password, email, auth);
        	Map<String, Object> map = new HashMap<>();
            map.put("message", message);
            map.put("token", dataAccess.getUserApiToken_username_only(username) + username);
            return new JsonMapRepresentation(map);
        }
        catch(org.springframework.dao.DuplicateKeyException e){
        	throw new ResourceException(400,"This username already exists in the database");
        }   
    }
}