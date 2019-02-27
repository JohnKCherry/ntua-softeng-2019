package gr.ntua.ece.softeng18b.api;

import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Form;
import org.restlet.data.Header;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;

public class LogoutResource extends ServerResource {
	
	private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @SuppressWarnings("unchecked")
	@Override
    protected Representation post(Representation entity) throws ResourceException {
    	//Create a new restlet form
        @SuppressWarnings("unused")
		Form form = new Form(entity);
        //Read the parameters
        
        //authorization of user
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String user_token = headers.getFirstValue("X-OBSERVATORY-AUTH");
        //System.out.println(">>>>>>>000 User token is: "+user_token);
        if(user_token == null || user_token.isEmpty()) throw new ResourceException(401, "Not loged in");
        if(!dataAccess.isLogedIn(user_token))throw new ResourceException(401, "Not loged in");
        //System.out.println(">>>>>>>>>> User token is: "+user_token);
        
        dataAccess.logoutUser(user_token);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "OK");
        return new JsonMapRepresentation(map);
    }
}