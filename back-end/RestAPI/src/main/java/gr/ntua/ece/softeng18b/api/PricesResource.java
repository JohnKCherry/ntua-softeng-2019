package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
import gr.ntua.ece.softeng18b.data.model.Price;
import gr.ntua.ece.softeng18b.data.model.PriceResult;
import gr.ntua.ece.softeng18b.data.model.Product;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PricesResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();



    @Override
    protected Representation post(Representation entity) throws ResourceException {

        //Create a new restlet form
        Form form = new Form(entity);
        //Read the parameters
        String shop_id_string = form.getFirstValue("shopId");
        String product_id_string = form.getFirstValue("productId");
        Double price = toDouble(form.getFirstValue("price"));
        if(price == null || price <= 0)  throw new ResourceException(400,"Bad value for price!");
        String dateFrom_string = form.getFirstValue("dateFrom");
        String dateTo_string = form.getFirstValue("dateTo");
        
      //validate the values (in the general case)
        int product_id, shop_id; 
        try {
        	product_id = Integer.parseInt(product_id_string);
        	shop_id = Integer.parseInt(shop_id_string);
        }
    	catch(NumberFormatException e){
        	throw new ResourceException(400,"Bad parameter for product_id or shop_id");
        } 
        if(dateFrom_string == null || dateTo_string == null);
        Date dateFrom, dateTo;
        try{
        	dateFrom = Date.valueOf(dateFrom_string);
        	dateTo =Date.valueOf(dateTo_string);
        }
        catch(IllegalArgumentException e){
        	throw new ResourceException(400,"Bad format for dates");
        }
        
        if(dateTo.before(dateFrom)) {
        	throw new ResourceException(400,"Bad parameter for dates, dateFrom must be erlier than dateTo");
        }
        try{
        	Price price_r = dataAccess.addPrice(product_id, shop_id, price, dateFrom, dateTo);
        	return new JsonPriceRepresentation(price_r);
        }
        catch(org.springframework.dao.DuplicateKeyException e){
        	throw new ResourceException(400,"A price for this product in this shop already exists in the database");
        }

    }
    
    @Override
    protected Representation get() throws ResourceException {
    	String startAttr		= getQuery().getValues("start");
    	String countAttr		= getQuery().getValues("count");
    	String geoDistAttr		= getQuery().getValues("geoDist");
    	String geoLngAttr		= getQuery().getValues("geoLng");
    	String geoLatAttr		= getQuery().getValues("geoLat");
    	String sortAttr 		= getQuery().getValues("sort");
    	String formatAttr   	= getQuery().getValues("format");
    	String dateFromAttr		= getQuery().getValues("dateFrom");
    	String dateToAttr		= getQuery().getValues("dateTo");
    	String shops_string		= getQuery().getValues("shops");
    	String products_string	= getQuery().getValues("products");
    	String tags_string		= getQuery().getValues("tags");
    	
    	
    	if(formatAttr!=null && !formatAttr.equals("json")) throw new ResourceException(400,"Only json format is supported at the moment");
    	
    	int start, count;
    	String sort = sortAttr;
    	
        try {
            start = Integer.parseInt(startAttr);
        } catch(NumberFormatException e) {
        	start = 0; //default
        }
        //////////////////////////////////////////////
        try {
        	if(sort == null) throw  new NumberFormatException("The sort attribute entered, " + sort + " is invalid."); 
            if(sort.equals("geo.dist|ASC")) sort = "distance ASC";
            else if(sort.equals("geo.dist|DESC")) sort = "distance DESC";
            else if(sort.equals("price|ASC")) sort = "price ASC";
            else if(sort.equals("price|DESC")) sort = "price DESC";
            else if(sort.equals("date|ASC")) sort = "date ASC";
            else if(sort.equals("date|DESC")) sort = "date DESC";
            else throw  new NumberFormatException("The sort attribute entered, " + sort + " is invalid."); 
        } catch(NumberFormatException e) {
        	sort = "price ASC"; //default
        }
        //////////////////////////////////////////////
    
        try {
            count = Integer.parseInt(countAttr);
        } catch(NumberFormatException e) {
        	count = 10; //default
        }
        //////////////////////////////////////////////
        String where_clause = "";
        String shopDist ="";
        Boolean geo = false;
       	if(!geoDistAttr.isEmpty() && !geoLngAttr.isEmpty() && !geoLatAttr.isEmpty() ) {
            Double check_lng = toDouble(geoLngAttr);
            Double check_lat = toDouble(geoLatAttr);
            Double check_dist = toDouble(geoDistAttr);
            if(check_lng == null || check_lat == null || geoDistAttr == null) throw new ResourceException(400,"Bad parameters for lng or lat");
        	where_clause += " AND shopDist <="+ geoDistAttr;
        	shopDist = "(6371 * acos (cos ( radians(\"+geoLngAttr +\") )* cos( radians( ST_Y(shops.location) ) )* cos( radians( ST_X(shops.location) ) - radians(\"+geoLatAttr+\") )+ sin ( radians(\"+geoLngAttr+\") )* sin( radians( ST_Y(shops.location) ) ))) as shopDist";
        	geo = true;
        }
       	else if(!geoDistAttr.isEmpty() || !geoLngAttr.isEmpty() || !geoLatAttr.isEmpty()) throw new ResourceException(400,"Not enough parameters for geolocation processing");
       	
       	String regex = "^[0-9\\,]+$";
       	if(!shops_string.isEmpty() && shops_string.matches(regex)) {
       		where_clause += " AND shop_id in ("+shops_string +")";
       	}
       	else if(!shops_string.isEmpty()) throw new ResourceException(400,"Bad value for shops list");
       	
       	if(!products_string.isEmpty() && products_string.matches(regex)) {
       		where_clause += " AND product_id in("+products_string +")";
       	}
       	else if(!products_string.isEmpty()) throw new ResourceException(400,"Bad value for products list");
       	
       	if(!tags_string.isEmpty() && !tags_string.contains(";")) {
       		where_clause += " AND products.tags in ("+tags_string+")";
       	}
       	else if(!tags_string.isEmpty()) throw new ResourceException(400,"Bad value for tags list");
       	
       	Date dateFrom, dateTo;
        try{
        	dateFrom = Date.valueOf(dateFromAttr);
        	dateTo	 = Date.valueOf(dateToAttr);
        	if(dateTo.before(dateFrom)) {
            	throw new ResourceException(400,"Bad parameter for dates, dateFrom must be erlier than dateTo");
            }
        	where_clause += " AND prices.dateFrom >= "+ dateFrom.toString() +"AND prices.dateTo <= " + dateTo.toString();
        }
        catch(IllegalArgumentException e){
        	where_clause += " AND prices.dateTo >= CURDATE() ";
        }
        
        //TODO Set product status == 0 for valid results!!!
        
        List<PriceResult> prices = dataAccess.getPrices(new Limits(start,count),where_clause,sort,geo,shopDist);

        Map<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("count", count);
        map.put("total", prices.size());
        map.put("prices", prices);

        return new JsonMapRepresentation(map);
    }
    
}
