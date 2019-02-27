package gr.ntua.ece.softeng18b.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;

public class LoginResource extends ServerResource {
	
	private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation post(Representation entity) throws ResourceException {
    	//Create a new restlet form
        Form form = new Form(entity);
        //Read the parameters
        String username = form.getFirstValue("username");
        String password = form.getFirstValue("password");
        if(username == null || password == null) throw new ResourceException(400, "Missing username or password");
        
        Optional<String> optional = dataAccess.getUserApiToken(username, password);
        if(optional == null) throw new ResourceException(401,"Login failed. Wrong username or password");
        String api_token = optional.orElseThrow(() -> new ResourceException(401, "Login failed. Wrong username or password"));
        Map<String, Object> map = new HashMap<>();
        map.put("token", api_token + username);
        //if(dataAccess.isLogedIn(api_token + username)) System.out.println("Succesfull login check");
        //dataAccess.logoutUser(api_token + username);
        //if(!dataAccess.isLogedIn(api_token + username)) System.out.println("Succesfull logout check");
        return new JsonMapRepresentation(map);
    }
}