package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
import gr.ntua.ece.softeng18b.data.model.Product;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsByNameResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation get() throws ResourceException {

        String name = getAttribute("name");
        
        String startAttr	= getQuery().getValues("start");
    	String countAttr	= getQuery().getValues("count");
    	String statusAttr	= getQuery().getValues("status");
        
        String formatAttr	= getQuery().getValues("format");
    	if(formatAttr!=null && !formatAttr.equals("json")) throw new ResourceException(400,"Only json format is supported at the moment");

        if (name == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing product name");
        }
        
        int start, count, status;
        
        try {
            start = Integer.parseInt(startAttr);
        } catch(NumberFormatException e) {
        	start = 0; //default
        }
        
        try {
            count = Integer.parseInt(countAttr);
        } catch(NumberFormatException e) {
        	count = 10; //default
        }
        
        try {
        	if(statusAttr == null) throw  new NumberFormatException("The status attribute entered, " + statusAttr + " is invalid."); 
        	if(statusAttr.equals("ACTIVE")) status = 0;
            else if (statusAttr.equals("WITHDRAWN")) status = 1;
            else if (statusAttr.equals("ALL")) status = -1; // -1 for all products
            else throw  new NumberFormatException("The status attribute entered, " + statusAttr + " is invalid."); 
        } catch(NumberFormatException e) {
        	status = 0; //default
        }

        List<Product> products = dataAccess.getProductsByName(new Limits(start,count), status,name);

        Map<String, Object> map = new HashMap<>();
        map.put("total", products.size());
        map.put("products", products);

        return new JsonMapRepresentation(map);
    }
}
