package gr.ntua.ece.softeng18b.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.restlet.data.Form;
import org.restlet.data.Header;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
import gr.ntua.ece.softeng18b.data.model.Product;
import gr.ntua.ece.softeng18b.data.model.ProductWithImage;
import gr.ntua.ece.softeng18b.data.model.User;

public class FavouritesResource extends ServerResource {
	
	private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();
	
	
	@Override
    protected Representation get() throws ResourceException {
		//authorization of user
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String user_token = headers.getFirstValue("X-OBSERVATORY-AUTH");
        
        if(user_token == null || user_token.isEmpty()) throw new ResourceException(401, "Not loged in");
    	
        List<ProductWithImage> products = dataAccess.getFavouriteProductsWithImage(user_token);

        Map<String, Object> map = new HashMap<>();
        map.put("total", products.size());
        map.put("products", products);

        return new JsonMapRepresentation(map);
    }


    @Override
    protected Representation post(Representation entity) throws ResourceException {
    	//Create a new restlet form
        Form form = new Form(entity);
        //Read the parameters
        String product_id_string = form.getFirstValue("productId");
        
        //authorization of user
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String user_token = headers.getFirstValue("X-OBSERVATORY-AUTH");
        if(user_token == null || user_token.isEmpty()) throw new ResourceException(401, "Not loged in");
        if(!dataAccess.isLogedIn(user_token))throw new ResourceException(401, "Not loged in");
        
        int product_id;
        try {
            product_id = Integer.parseInt(product_id_string);
        } catch(NumberFormatException e) {
        	throw new ResourceException(400, "Invalid product ID");
        }

        try{
        	dataAccess.addFavourite(user_token, product_id);
        }
        catch(org.springframework.dao.DuplicateKeyException e) {
        	throw new ResourceException(400,"This product is already in the favourites!");
        }
        catch(org.springframework.dao.DataIntegrityViolationException e) {
        	throw new ResourceException(400,"Invalid user token or product id!");
        }
        return new JsonMessageRepresentation("OK");
    }
    
    @Override
    protected Representation delete() throws ResourceException {
    	int product_id;
    	String product_id_string = getAttribute("productId");
    	try {
        	product_id = Integer.parseInt(product_id_string);
        }
    	catch(NumberFormatException e){
        	throw new ResourceException(400,"Bad parameter for product id");
        }
    	
        if(!dataAccess.getProduct(product_id).isPresent()) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product not found - id: " + product_id_string);
    	
    	//authorization of user
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String user_token = headers.getFirstValue("X-OBSERVATORY-AUTH");      
        if(user_token == null || user_token.isEmpty()) throw new ResourceException(401, "Not loged in");
        if(!dataAccess.isLogedIn(user_token))throw new ResourceException(401, "Not loged in");
   
        dataAccess.deleteFavourite(user_token, product_id);
        return new JsonMessageRepresentation("OK");
    }
}