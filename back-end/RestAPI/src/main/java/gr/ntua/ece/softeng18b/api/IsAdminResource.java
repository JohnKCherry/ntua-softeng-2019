package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import org.restlet.data.Header;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

public class IsAdminResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @SuppressWarnings("unchecked")
	protected Representation get() throws ResourceException {

    	//authorization of user
    	Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String user_token = headers.getFirstValue("X-OBSERVATORY-AUTH");
        
        if(user_token == null || user_token.isEmpty()) throw new ResourceException(401, "False");
        if(!dataAccess.isLogedIn(user_token))throw new ResourceException(401, "False");
        Boolean admin = dataAccess.isAdmin(user_token);

        if(!admin) {
        	return new JsonMessageRepresentation("message : false");
        }
        else{
        	return new JsonMessageRepresentation("message : true");
        }
    }
}
