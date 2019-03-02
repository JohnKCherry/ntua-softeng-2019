package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.model.User;
import gr.ntua.ece.softeng18b.data.model.Product;
import gr.ntua.ece.softeng18b.data.model.Value;
import org.restlet.data.Form;
import org.restlet.data.Header;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

import java.util.HashMap;
import java.util.Optional;

public class NumberOfActiveProductsResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation get() throws ResourceException {
    	
    	//authorization of user
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String user_token = headers.getFirstValue("X-OBSERVATORY-AUTH");
        if(user_token == null || user_token.isEmpty()) throw new ResourceException(401, "Administrator authorization is required to access this endpoint");
        if(!dataAccess.isLogedIn(user_token))throw new ResourceException(401, "Administrator authorization is required to access this endpoint");
        
        Boolean admin = dataAccess.isAdmin(user_token);
        
    	if(!admin) {     
    		throw new ResourceException(401, "Administrator authorization is required to access this endpoint");
    	}
    	
    	 Optional<Value> optional = dataAccess.getNumberOfActiveProducts();
         Value value = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "error of number of active products"));

         return new JsonValueRepresentation(value);
    }
}