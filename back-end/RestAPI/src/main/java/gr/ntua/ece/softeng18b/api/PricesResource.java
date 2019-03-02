package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
import gr.ntua.ece.softeng18b.data.model.Price;
import gr.ntua.ece.softeng18b.data.model.PriceResult;
import gr.ntua.ece.softeng18b.data.model.PriceResultSingleDate;
import gr.ntua.ece.softeng18b.data.model.PriceResultSingleDateXprimal;

import org.restlet.data.Form;
import org.restlet.data.Header;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PricesResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();



    @SuppressWarnings("unchecked")
	@Override
    protected Representation post(Representation entity) throws ResourceException {

        //Create a new restlet form
        Form form = new Form(entity);
        //Read the parameters
        String shop_id_string = form.getFirstValue("shopId");
        String product_id_string = form.getFirstValue("productId");
        Double price;
        try{
        	price = toDouble(form.getFirstValue("price"));
        }
        catch(NumberFormatException e) {
        	throw new ResourceException(400,"Bad value for price");
        }
        if(price == null || price <= 0)  throw new ResourceException(400,"Bad value for price!");
        String dateFrom_string = form.getFirstValue("dateFrom");
        String dateTo_string = form.getFirstValue("dateTo");
        
        //authorization of user
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String user_token = headers.getFirstValue("X-OBSERVATORY-AUTH");
        //user_token = dataAccess.getUserApiToken_username_only("user").get()+"user";
        if(user_token == null || user_token.isEmpty()) throw new ResourceException(401, "Not authorized to post price");
        if(!dataAccess.isLogedIn(user_token))throw new ResourceException(401, "Not authorized to post price");
        
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
        	//return new JsonPriceRepresentation(price_r);
        	
        	//Weird format (like get prices) to pass testing...
        	Map<String, Object> map = new HashMap<>();
            List<PriceResultSingleDateXprimal> prices = dataAccess.getPricesXprimal(new Limits(0,20)," AND product_id = " + price_r.getProductId() + " AND shop_id = "+ price_r.getShoptId() +" ", "dd.Date ASC",false,"","",dateFrom,dateTo);
            map.put("total", prices.size());
        	System.out.print(">>>>>>>>>>"+prices.size());
            map.put("start", 0);
            map.put("count", 20);
            map.put("prices", prices);
            System.out.println(new JsonMapRepresentation(map).toString());
            return new JsonMapRepresentation(map);
        	
        }
        catch(org.springframework.dao.DuplicateKeyException e){
        	throw new ResourceException(400,"A price for this product in this shop already exists in the database");
        }
        catch(org.springframework.dao.DataIntegrityViolationException e) {
        	throw new ResourceException(400,"Invalid productId or ShopId");
        }

    }
    
    @Override
    protected Representation get() throws ResourceException {
    	String startAttr			= getQuery().getValues("start");
    	String countAttr			= getQuery().getValues("count");
    	String geoDistAttr			= getQuery().getValues("geoDist");
    	String geoLngAttr			= getQuery().getValues("geoLng");
    	String geoLatAttr			= getQuery().getValues("geoLat");
    	String sortAttr 			= getQuery().getValues("sort");
    	String formatAttr   		= getQuery().getValues("format");
    	String dateFromAttr			= getQuery().getValues("dateFrom");
    	String dateToAttr			= getQuery().getValues("dateTo");
    	String shops_string			= getQuery().getValues("shops");
    	String products_string		= getQuery().getValues("products");
    	String product_tags_string	= getQuery().getValues("productTags");
    	String shop_tags_string		= getQuery().getValues("shopTags");
    	String verbose				= getQuery().getValues("verbose");
    	String tagsAttr				= getQuery().getValues("tags");
    	String statusAttr			= getQuery().getValues("status");
    	
    	if(formatAttr!=null && !formatAttr.equals("json")) throw new ResourceException(400,"Only json format is supported at the moment");
    	
    	int start, count, status;
    	String sort = sortAttr;
    	
    	 try {
         	if(statusAttr == null) throw  new NumberFormatException("The status attribute entered, " + statusAttr + " is invalid."); 
         	if(statusAttr.equals("ACTIVE")) status = 0;
             else if (statusAttr.equals("WITHDRAWN")) status = 1;
             else if (statusAttr.equals("ALL")) status = -1; // -1 for all products
             else throw  new NumberFormatException("The status attribute entered, " + statusAttr+ " is invalid."); 
         } catch(NumberFormatException e) {
         	status = 0; //default
         }
    	
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
            else if(sort.equals("date|ASC") && !(verbose != null && verbose.equals("false"))) sort = "date ASC";
            else if(sort.equals("date|DESC") && !(verbose != null && verbose.equals("false"))) sort = "date DESC";
            else throw  new NumberFormatException("The sort attribute entered, " + sort + " is invalid."); 
        } catch(NumberFormatException e) {
        	sort = "price ASC"; //default
        }
        //////////////////////////////////////////////
    
        try {
            count = Integer.parseInt(countAttr);
        } catch(NumberFormatException e) {
        	count = 20; //default
        }
        //////////////////////////////////////////////
        String where_clause = "";
        String shopDist ="";
        Boolean geo = false;
        String have_clause= "";
       	if(geoDistAttr!= null && geoLngAttr != null && geoLatAttr != null && !geoDistAttr.isEmpty() && !geoLngAttr.isEmpty()) {
            Double check_lng = toDouble(geoLngAttr);
            Double check_lat = toDouble(geoLatAttr);
            Double check_dist = toDouble(geoDistAttr);
            if(check_lng == null || check_lat == null || check_dist == null) throw new ResourceException(400,"Bad parameters for lng or lat");
        	have_clause += " HAVING shopDist <="+ geoDistAttr;
        	shopDist = "(6371 * acos (cos ( radians("+geoLngAttr +") )* cos( radians( ST_Y(shops.location) ) )* cos( radians( ST_X(shops.location) ) - radians("+geoLatAttr+") )+ sin ( radians("+geoLngAttr+") )* sin( radians( ST_Y(shops.location) ) ))) as shopDist";
        	geo = true;
        }
       	else if((geoDistAttr!= null && !geoDistAttr.isEmpty()) || (geoLngAttr != null && !geoLngAttr.isEmpty()) || (geoLatAttr != null && !geoLatAttr.isEmpty())) throw new ResourceException(400,"Not enough parameters for geolocation processing");
       	
       	String regex = "^[0-9\\,]+$";
       	if( shops_string!= null && !shops_string.isEmpty() && shops_string.matches(regex)) {
       		where_clause += " AND shop_id in ("+shops_string +")";
       	}
       	else if(shops_string!= null && !shops_string.isEmpty()) throw new ResourceException(400,"Bad value for shops list");
       	
       	if(products_string != null && !products_string.isEmpty() && products_string.matches(regex)) {
       		where_clause += " AND product_id in("+products_string +")";
       	}
       	else if(products_string != null && !products_string.isEmpty()) throw new ResourceException(400,"Bad value for products list");
       	
       	
       	Boolean notags = (tagsAttr == null || tagsAttr.isEmpty());
       	if(!notags) product_tags_string = shop_tags_string = null;
       	
       	if(product_tags_string != null && !product_tags_string.isEmpty() && !product_tags_string.contains(";")){
       		List<String> ptags = Arrays.asList(product_tags_string.split("\\s*(=>|,|\\s)\\s*"));
       		if(!ptags.isEmpty()) {
       			where_clause += " AND ( 0 ";
       			for(String s : ptags){
       				where_clause += "OR (products.tags LIKE '%"+s+"%')";
       			}
       			where_clause+= ") ";
       		}
       	}
       	else if(product_tags_string != null && !product_tags_string.isEmpty()) throw new ResourceException(400,"Bad value for product tags list");
       	
       	if(shop_tags_string != null && !shop_tags_string.isEmpty() && !shop_tags_string.contains(";")) {
       		List<String> stags = Arrays.asList(shop_tags_string.split("\\s*(=>|,|\\s)\\s*"));
       		if(!stags.isEmpty()) {
       			where_clause += " AND ( 0 ";	
       			for(String s : stags){
       				where_clause += "OR (shops.tags LIKE '%"+s+"%')";
       			}
       			where_clause+= ") ";
       		}
       	}
       	else if(shop_tags_string != null && !shop_tags_string.isEmpty()) throw new ResourceException(400,"Bad value for shop tags list");
       	
       	if(tagsAttr != null && !tagsAttr.isEmpty() && !tagsAttr.contains(";")) {
       		List<String> gtags = Arrays.asList(tagsAttr.split("\\s*(=>|,|\\s)\\s*"));
       		if(!gtags.isEmpty()) {
       			where_clause += " AND ( ( 0 ";
       			for(String s : gtags){
       				where_clause += "OR (products.tags LIKE '%"+s+"%')";
       			}
       			where_clause+= ") ";
       			
       			where_clause += " OR ( 0 ";	
       			for(String s : gtags){
       				where_clause += "OR (shops.tags LIKE '%"+s+"%')";
       			}
       			where_clause+= ") ) ";
       		}
       	}
       	else if( tagsAttr != null && !tagsAttr.isEmpty()) throw new ResourceException(400,"Bad value for tags list");
       	
       	
       	Date dateFrom = null, dateTo = null ;
        try{
        	dateFrom = Date.valueOf(dateFromAttr);
        	dateTo	 = Date.valueOf(dateToAttr);
        	if(dateTo.before(dateFrom)) {
            	throw new ResourceException(400,"Bad parameter for dates, dateFrom must be earlier than dateTo");
            }
        	where_clause += " AND prices.dateFrom <= '"+ dateTo.toString() +"' AND prices.dateTo >= '" + dateFrom.toString()+"'";
        }
        catch(IllegalArgumentException e){
        	where_clause += " AND prices.dateTo >= CURDATE() AND prices.dateFrom <= CURDATE() ";
        	dateFrom = Date.valueOf(LocalDateTime.now().toLocalDate());
        	dateTo	 = Date.valueOf(LocalDateTime.now().toLocalDate());
        }
        
        //Set product status == 0 for valid results!!!
        if(status!= -1) where_clause += " AND products.withdrawn = "+status+" ";
        
        Map<String, Object> map = new HashMap<>();
        if(verbose != null && verbose.equals("false")) {
        	List<PriceResult> prices = dataAccess.getPrices(new Limits(start,count),where_clause,sort,geo,shopDist,have_clause,dateFrom,dateTo);
        	map.put("prices", prices);
        	map.put("total", prices.size());
        }
        else {
        	List<PriceResultSingleDateXprimal> prices = dataAccess.getPricesXprimal(new Limits(start,count),where_clause,sort,geo,shopDist,have_clause,dateFrom,dateTo);
        	map.put("prices", prices);
        	map.put("total", prices.size());
        }
        map.put("start", start);
        map.put("count", count);
        
        return new JsonMapRepresentation(map);
    }
    
    public List<PriceResultSingleDate> convertPriceResult(PriceResult p){
    	List<PriceResultSingleDate> result = new ArrayList<PriceResultSingleDate>();
    	Date temp = p.getDateFrom();
    	while(temp.before(p.getDateTo())) {
    		result.add(new PriceResultSingleDate(p.getProductId(),p.getShoptId(),p.getPrice(),temp,p.getProductName(),p.getProductTags(),p.getShopName(),p.getShopTags(),p.getShopAddress(),(double) p.getShopDist()));

    		String dt = temp.toString();  // Start date
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		Calendar c = Calendar.getInstance();
    		try {
				c.setTime(sdf.parse(dt));
			} catch (ParseException e) {
				throw new ResourceException(500,"PriceResult converter did something bad...");
			}
    		c.add(Calendar.DATE, 1);  // number of days to add
    		dt = sdf.format(c.getTime());  // dt is now the new date
    		temp = Date.valueOf(dt);
    	}
		return result;
    }
    
    @SuppressWarnings("unchecked")
	protected Representation delete() throws ResourceException {
        //Read the parameters
        String shop_id_string = getAttribute("shop_id");
        String product_id_string = getAttribute("product_id");
        
      //authorization of user
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String user_token = headers.getFirstValue("X-OBSERVATORY-AUTH");
        if(user_token == null || user_token.isEmpty()) throw new ResourceException(401, "Not authorized to delete price");
        if(!dataAccess.isLogedIn(user_token))throw new ResourceException(401, "Not authorized to delete price");
        
        //validate the values (in the general case)
        int product_id, shop_id; 
        try {
        	product_id = Integer.parseInt(product_id_string);
        	shop_id = Integer.parseInt(shop_id_string);
        }
    	catch(NumberFormatException e){
        	throw new ResourceException(400,"Bad parameter for product_id or shop_id");
        }
        dataAccess.deletePrice(product_id, shop_id);
        
        HashMap<String,String> msg = new HashMap<String,String>();
        msg.put("message", "OK");
        
        return new JsonMapRepresentation(msg);
        
    }
    
}
