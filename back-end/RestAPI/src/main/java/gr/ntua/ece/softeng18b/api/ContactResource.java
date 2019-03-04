package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
import gr.ntua.ece.softeng18b.data.model.Product;
import org.restlet.data.Form;
import org.restlet.data.Header;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @SuppressWarnings("unchecked")
	@Override
    protected Representation post(Representation entity) throws ResourceException {

        //Create a new restlet form
        Form form = new Form(entity);
        //Read the parameters
        String fullname = form.getFirstValue("fullname");
        String company = form.getFirstValue("company");
        String mail = form.getFirstValue("mail");
        String choice = form.getValues("choice");
        String subject = form.getValues("subject");
        String message = form.getValues("message");
        
        //validate the values (in the general case)
        if(fullname == null || company == null || mail == null || choice == null || subject == null || message == null) throw new ResourceException(400,"This operation needs more parameters");
      
        	dataAccess.addContact(fullname, company, mail, choice, subject, message);
        	Map<String, Object> map = new HashMap<>();
            map.put("message", "OK");
            return new JsonMapRepresentation(map);
    }
}
