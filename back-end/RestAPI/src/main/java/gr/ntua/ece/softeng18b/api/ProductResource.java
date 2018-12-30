package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.model.Product;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.util.Optional;

public class ProductResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation get() throws ResourceException {

        String idAttr = getAttribute("id");

        if (idAttr == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing product id");
        }

        Long id = null;
        try {
            id = Long.parseLong(idAttr);
        }
        catch(Exception e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid product id: " + idAttr);
        }

        Optional<Product> optional = dataAccess.getProduct(id);
        Product product = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product not found - id: " + idAttr));

        return new JsonProductRepresentation(product);
    }

    @Override
    protected Representation delete() throws ResourceException {
    	int id;
    	String id_string = getAttribute("id");
    	try {
        	id = Integer.parseInt(id_string);
        }
    	catch(NumberFormatException e){
        	throw new ResourceException(400,"Bad parameter for product id");
        }
    	
        if(!dataAccess.getProduct(id).isPresent()) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product not found - id: " + id_string);

    	Boolean admin = false;	//TODO: Implement authority detection based on user login
    	if(!admin) {     
    		dataAccess.patchProduct(id, "withdrawn","1");
    		//Check if withdrawal was successful
    		if(dataAccess.getProduct(id).get().isWithdrawn()) return new JsonMessageRepresentation("OK");
    		return new JsonMessageRepresentation("Could not complete product withdrawal");
    	}
    	else{
    		dataAccess.deleteProduct(id);
    		if(!dataAccess.getProduct(id).isPresent()) return new JsonMessageRepresentation("OK");
    		return new JsonMessageRepresentation("Could not complete product withdrawal");
    	}
    }
    
    @Override
    protected Representation put(Representation entity) throws ResourceException {

        //Create a new restlet form
        Form form = new Form(entity);
        //Read the parameters
        String id_string = getAttribute("id");
        String name = form.getFirstValue("name");
        String description = form.getFirstValue("description");
        String category = form.getFirstValue("category");
        String withdrawn = form.getFirstValue("status");
        String tags = form.getFirstValue("tags");

        //validate the values (in the general case)
        if(name == null || description == null || category == null) throw new ResourceException(400);
        if(tags == null) tags = "";
        	
        String regex = "^[a-zA-Z0-9\\s.\\-.\\,.\\'.\\[.\\[.\\(.\\).\\..\\+.\\-.\\:.\\@]+$";
        String regex_s = "^[a-zA-Z0-9\\s.\\-.\\,.\\'.\\[.\\[.\\(.\\)]+$";
        if(!name.matches(regex) || !description.matches(regex) || !category.matches(regex_s) || !tags.matches(regex_s) ) throw new ResourceException(400);
        int id;
        try {
        	id = Integer.parseInt(id_string);
        }
        catch(NumberFormatException e){
        	throw new ResourceException(400,"Bad parameter for product id");
        }
        if(!dataAccess.getProduct(id).isPresent()) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product not found - id: " + id_string);
        
        // Status field is not mentioned in the API specs so it is not mandatory
        if(withdrawn == null) {
        	if(dataAccess.getProduct(id).get().isWithdrawn()) withdrawn = "1";
        	else withdrawn = "0";
        }
        else if(withdrawn.equals("ACTIVE")) withdrawn = "0";
        else if (withdrawn.equals("WITHDRAWN")) withdrawn = "1";
        else throw new ResourceException(400,"Bad parameter for parameter status!");
        

        Product product = dataAccess.updateProduct(id, name, description, category, withdrawn, tags);

        return new JsonProductRepresentation(product);
    }
    
    @Override
    protected Representation patch(Representation entity) throws ResourceException {

        //Create a new restlet form
        Form form = new Form(entity);
        //Read the parameters
        String id_string = getAttribute("id");
        String name = form.getFirstValue("name");
        String description = form.getFirstValue("description");
        String category = form.getFirstValue("category");
        String withdrawn = form.getFirstValue("status");
        String tags = form.getFirstValue("tags");

        //validate the values (in the general case)
        //We check to see wich parameter was given (while ensuring that it is the only one)
        String update_parameter, value;
        String regex = "^[a-zA-Z0-9\\s.\\-.\\,.\\'.\\[.\\[.\\(.\\).\\..\\+.\\-.\\:.\\@]+$";
        String regex_s = "^[a-zA-Z0-9\\s.\\-.\\,.\\'.\\[.\\[.\\(.\\)]+$";
        if(name!=null && description == null && category == null && withdrawn == null && tags == null) {
        	if(!name.matches(regex)) throw new ResourceException(400,"Bad parameter for name!");
        	update_parameter = "name";
        	value = name;
        }
        else if(description!=null && name == null && category == null && withdrawn == null && tags == null) {
        	if(!description.matches(regex)) throw new ResourceException(400,"Bad parameter for description!");
        	update_parameter = "description";
        	value = description;
        }
        else if(category!=null && name == null && description == null && withdrawn == null && tags == null) {
        	if(!name.matches(regex_s)) throw new ResourceException(400,"Bad parameter for category!");
        	update_parameter = "category";
        	value = category;
        }
        else if(withdrawn!=null && name == null && description == null && category == null && tags == null) {
        	if(!withdrawn.equals("ACTIVE") && !withdrawn.equals("WITHDRAWN")) throw new ResourceException(400,"Bad parameter for withdrawn!");
        	update_parameter = "withdrawn";
        	if(withdrawn.equals("ACTIVE")) value = "0";
        	else value = "1";
        }
        else if(tags!=null && name == null && description == null && category == null && form.getFirstValue("withdrawn") == null) {
        	if(!tags.matches(regex_s)) throw new ResourceException(400,"Bad parameter for tags!");
        	update_parameter = "tags";
        	value = tags;
        }
        else  throw new ResourceException(400,"None or more than one values where found for patch request!");
        
        int id;
        try {
        	id = Integer.parseInt(id_string);
        }
        catch(NumberFormatException e){
        	throw new ResourceException(400,"Bad parameter for product id");
        }
        if(!dataAccess.getProduct(id).isPresent()) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product not found - id: " + id_string);

        Product product = dataAccess.patchProduct(id, update_parameter,value);

        return new JsonProductRepresentation(product);
    }
}
