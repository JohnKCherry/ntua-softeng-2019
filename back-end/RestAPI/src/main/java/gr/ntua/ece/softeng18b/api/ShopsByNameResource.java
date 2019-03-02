package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
import gr.ntua.ece.softeng18b.data.model.Shop;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopsByNameResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation get() throws ResourceException {

        String name = getAttribute("name");
        
        String startAttr	= getQuery().getValues("start");
    	String countAttr	= getQuery().getValues("count");
    	String statusAttr	= getQuery().getValues("status");
    	String sortAttr 	= getQuery().getValues("sort");
        
        String formatAttr	= getQuery().getValues("format");
    	if(formatAttr!=null && !formatAttr.equals("json")) throw new ResourceException(400,"Only json format is supported at the moment");

        if (name == null || (name.trim().length() == 0)) {
        	if(formatAttr!=null && !formatAttr.equals("json")) throw new ResourceException(400,"Only json format is supported at the moment");
        	
        	int start, count, status;
        	String sort = sortAttr;
        	
            try {
            	start = Integer.parseInt(startAttr);
            } 
            catch(NumberFormatException e) {
            	start = 0; //default
            }
            //////////////////////////////////////////////
            try {
            	if(sort == null) throw  new NumberFormatException("The sort attribute entered, " + sort + " is invalid."); 
                if(sort.equals("id|ASC")) sort = "id ASC";
                else if(sort.equals("id|DESC")) sort = "id DESC";
                else if(sort.equals("name|ASC")) sort = "name ASC";
                else if(sort.equals("name|DESC")) sort = "name DESC";
                else throw  new NumberFormatException("The sort attribute entered, " + sort + " is invalid."); 
            } catch(NumberFormatException e) {
            	sort = "id DESC"; //default
            }
            //////////////////////////////////////////////
        
            try {
                count = Integer.parseInt(countAttr);
            } catch(NumberFormatException e) {
            	count = 10; //default
            }
            //////////////////////////////////////////////

            try {
            	if(statusAttr == null) throw  new NumberFormatException("The sort attribute entered, " + sort + " is invalid."); 
            	if(statusAttr.equals("ACTIVE")) status = 0;
                else if (statusAttr.equals("WITHDRAWN")) status = 1;
                else if (statusAttr.equals("ALL")) status = -1; // -1 for all shops
                else throw  new NumberFormatException("The status attribute entered, " + sort + " is invalid."); 
            } catch(NumberFormatException e) {
            	status = 0; //default
            }
            //////////////////////////////////////////////

            
        	
            List<Shop> shops = dataAccess.getShops(new Limits(start,count),status,sort);

            Map<String, Object> map = new HashMap<>();
            map.put("start", start);
            map.put("count", count);
            map.put("total", shops.size());
            map.put("shops", shops);

            return new JsonMapRepresentation(map);
        	//throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing product name");
        }
        
        int start, count, status;
        String sort = sortAttr;
        Boolean user_sort = true;
    	
        try {
            start = Integer.parseInt(startAttr);
        } catch(NumberFormatException e) {
        	start = 0; //default
        }
        //////////////////////////////////////////////
        try {
        	if(sort == null) throw  new NumberFormatException("The sort attribute entered, " + sort + " is invalid."); 
            if(sort.equals("id|ASC")) sort = "id ASC";
            else if(sort.equals("id|DESC")) sort = "id DESC";
            else if(sort.equals("name|ASC")) sort = "name ASC";
            else if(sort.equals("name|DESC")) sort = "name DESC";
            else throw  new NumberFormatException("The sort attribute entered, " + sort + " is invalid."); 
        } catch(NumberFormatException e) {
        	sort = "id DESC"; //default
        	user_sort = false;
        }
        
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

        List<Shop> shops = dataAccess.getShopsByName(new Limits(start,count), status,name, sort, user_sort);

        Map<String, Object> map = new HashMap<>();
        map.put("total", shops.size());
        map.put("shops", shops);

        return new JsonMapRepresentation(map);
    }
}
