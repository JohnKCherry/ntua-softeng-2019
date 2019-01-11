package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
import gr.ntua.ece.softeng18b.data.model.Product;
import gr.ntua.ece.softeng18b.data.model.Shop;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopsResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation get() throws ResourceException {
    	String startAttr	= getQuery().getValues("start");
    	String countAttr	= getQuery().getValues("count");
    	String statusAttr	= getQuery().getValues("status");
    	String sortAttr 	= getQuery().getValues("sort");
    	String formatAttr	= getQuery().getValues("format");
    	
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
    }
    
    @Override
    protected Representation post(Representation entity) throws ResourceException {

        //Create a new restlet form
        Form form = new Form(entity);
        //Read the parameters
        String name		 	= form.getFirstValue("name");
        String address 		= form.getFirstValue("address");
        String lng_string	= form.getFirstValue("lng");
        String lat_string	= form.getFirstValue("lat");
        String tags			= form.getValues("tags");
        Double lng, lat;
        //System.out.println("Καλό store");
        //System.out.println(name);
        
        //validate the values (in the general case)
        if(name == null || address == null || lng_string == null || lat_string == null) throw new ResourceException(400,"This operation needs more parameters for a new shop");
        if(tags == null) tags = "";
        //String regex = "^[p{IsGreek}.\\a-zA-Z0-9\\s.\\-.\\,.\\'.\\[.\\[.\\(.\\).\\..\\+.\\-.\\:.\\@]+$";
        //String regex_s = "^[p{IsGreek}.\\a-zA-Z0-9\\s.\\-.\\,.\\'.\\[.\\[.\\(.\\)]+$";
        //if(!name.matches(regex) || !address.matches(regex) || !tags.matches(regex_s) ) throw new ResourceException(400,"Forbidden characters in parameters");
        //System.out.println(name);
        lng = toDouble(lng_string);
        lat = toDouble(lat_string);     
        if(lng == null || lat == null)throw new ResourceException(400,"Bad parameter for shop's long or lat");
         
        try{
        	Shop shop = dataAccess.addShop(name, address, lng, lat, false, tags);
        	return new JsonShopRepresentation(shop);
        }
        catch(org.springframework.dao.DuplicateKeyException e){
        	throw new ResourceException(400,"This shop already exists in the database");
        }
      
    }
}
