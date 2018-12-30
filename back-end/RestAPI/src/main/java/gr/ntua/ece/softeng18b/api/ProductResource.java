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
        //TODO: Implement this
        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
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
        boolean withdrawn = Boolean.valueOf(form.getFirstValue("withdrawn"));
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
        Product product = dataAccess.updateProduct(id, name, description, category, withdrawn, tags);

        return new JsonProductRepresentation(product);
    }
}
