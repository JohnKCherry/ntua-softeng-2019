package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.model.Shop;

import org.restlet.data.Form;
import org.restlet.data.Header;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

import java.util.HashMap;
import java.util.Optional;

public class ShopResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation get() throws ResourceException {

        String idAttr = getAttribute("id");
        
        String formatAttr	= getQuery().getValues("format");
    	if(formatAttr!=null && !formatAttr.equals("json")) throw new ResourceException(400,"Only json format is supported at the moment");

        if (idAttr == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing shop id");
        }

        Long id = null;
        try {
            id = Long.parseLong(idAttr);
        }
        catch(Exception e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid shop id: " + idAttr);
        }

        Optional<Shop> optional = dataAccess.getShop(id);
        Shop shop = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found - id: " + idAttr));

        return new JsonShopRepresentation(shop);
    }
    
    @SuppressWarnings("unchecked")
	@Override
    protected Representation put(Representation entity) throws ResourceException {

        //Create a new restlet form
        Form form = new Form(entity);
        //Read the parameters
        String id_string = getAttribute("id");
        String name = form.getFirstValue("name");
        String address = form.getFirstValue("address");
        String lng_string = form.getFirstValue("lng");
        String lat_string = form.getFirstValue("lat");
        String withdrawn = form.getFirstValue("status");
        String tags = form.getFirstValue("tags");
        
        //authorization of user
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String user_token = headers.getFirstValue("X-OBSERVATORY-AUTH");
        if(user_token == null || user_token.isEmpty()) throw new ResourceException(401, "Not authorized to update shop");
        if(!dataAccess.isLogedIn(user_token))throw new ResourceException(401, "Not authorized to update shop");

        //validate the values (in the general case)
        if(name == null || address == null || lng_string == null || lat_string== null) throw new ResourceException(400);
        if(tags == null) tags = "";
        	
        //String regex = "^[a-zA-Z0-9\\s.\\-.\\,.\\'.\\[.\\[.\\(.\\).\\..\\+.\\-.\\:.\\@]+$";
        //String regex_s = "^[a-zA-Z0-9\\s.\\-.\\,.\\'.\\[.\\[.\\(.\\)]+$";
        //if(!name.matches(regex) || !address.matches(regex) || !tags.matches(regex_s) ) throw new ResourceException(400);
        int id;
        Double lng, lat;
        lng = toDouble(lng_string);
        lat = toDouble(lat_string);     
        if(lng == null || lat == null)throw new ResourceException(400,"Bad parameter for shop's long or lat");
        
        try {
        	id = Integer.parseInt(id_string);
        }
        catch(NumberFormatException e){
        	throw new ResourceException(400,"Bad parameter for shop id");
        }
        if(!dataAccess.getShop(id).isPresent()) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found - id: " + id_string);
        
        // Status field is not mentioned in the API specs so it is not mandatory
        if(withdrawn == null) {
        	if(dataAccess.getShop(id).get().isWithdrawn()) withdrawn = "1";
        	else withdrawn = "0";
        }
        else if(withdrawn.equals("ACTIVE")) withdrawn = "0";
        else if (withdrawn.equals("WITHDRAWN")) withdrawn = "1";
        else throw new ResourceException(400,"Bad parameter for parameter status!");
        
        try{
        	Shop shop = dataAccess.updateShop(id, name, address, lng, lat, withdrawn, tags);
        	return new JsonShopRepresentation(shop);
        }
        catch(org.springframework.dao.DuplicateKeyException e){
        	throw new ResourceException(400,"Input parameters have conflict with another shop in the database");
        }    
    }
    
    @SuppressWarnings("unchecked")
	@Override
    protected Representation patch(Representation entity) throws ResourceException {

        //Create a new restlet form
        Form form = new Form(entity);
        //Read the parameters
        String id_string = getAttribute("id");
        String name = form.getFirstValue("name");
        String address = form.getFirstValue("address");
        String lng_string = form.getFirstValue("lng");
        String lat_string = form.getFirstValue("lat");
        String withdrawn = form.getFirstValue("status");
        String tags = form.getFirstValue("tags");
        
        //authorization of user
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String user_token = headers.getFirstValue("X-OBSERVATORY-AUTH");
        if(user_token == null || user_token.isEmpty()) throw new ResourceException(401, "Not authorized to patch shop");
        if(!dataAccess.isLogedIn(user_token))throw new ResourceException(401, "Not authorized to patch shop");

        //validate the values (in the general case)
        //We check to see wich parameter was given (while ensuring that it is the only one)
        
        int id;
        try {
        	id = Integer.parseInt(id_string);
        }
        catch(NumberFormatException e){
        	throw new ResourceException(400,"Bad parameter for shop id");
        }
        if(!dataAccess.getShop(id).isPresent()) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found - id: " + id_string);
        
        String update_parameter, value;
        //String regex = "^[a-zA-Z0-9\\s.\\-.\\,.\\'.\\[.\\[.\\(.\\).\\..\\+.\\-.\\:.\\@]+$";
        //String regex_s = "^[a-zA-Z0-9\\s.\\-.\\,.\\'.\\[.\\[.\\(.\\)]+$";
        if(name!=null && address == null && lng_string == null && lat_string== null && withdrawn == null && tags == null) {
        	//if(!name.matches(regex)) throw new ResourceException(400,"Bad parameter for name!");
        	update_parameter = "name";
        	value = name;
        }
        else if(address!=null && name == null && lng_string == null && lat_string == null && withdrawn == null && tags == null) {
        	//if(!address.matches(regex)) throw new ResourceException(400,"Bad parameter for address!");
        	update_parameter = "address";
        	value = address;
        }
        else if(lng_string!=null && name == null && address == null && lat_string == null && withdrawn == null && tags == null) {
        	Double lng = toDouble(lng_string);
        	if(lng == null) throw new ResourceException(400,"Bad parameter for longitude!");
        	update_parameter = "location";
        	Double lat = dataAccess.getShop(id).get().getLat();
        	String location = "ST_GeomFromText('POINT("+lng.toString()+" "+lat+")', 4326)";
        	value = location;
        }
        else if(lat_string!=null && name == null && address == null && lng_string == null && withdrawn == null && tags == null) {
        	Double lat = toDouble(lat_string);
        	if(lat == null) throw new ResourceException(400,"Bad parameter for latitude!");
        	update_parameter = "location";
        	Double lng = dataAccess.getShop(id).get().getLng();
        	String location = "ST_GeomFromText('POINT("+lng.toString()+" "+lat+")', 4326)";
        	value = location;
        }
        else if(withdrawn!=null && name == null && address == null && lng_string == null && lat_string == null && tags == null) {
        	if(!withdrawn.equals("ACTIVE") && !withdrawn.equals("WITHDRAWN")) throw new ResourceException(400,"Bad parameter for status!");
        	update_parameter = "withdrawn";
        	if(withdrawn.equals("ACTIVE")) value = "0";
        	else value = "1";
        }
        else if(tags!=null && name == null && address == null && lng_string == null && lat_string == null && form.getFirstValue("withdrawn") == null) {
        	//if(!tags.matches(regex_s)) throw new ResourceException(400,"Bad parameter for tags!");
        	update_parameter = "tags";
        	value = tags;
        }
        else  throw new ResourceException(400,"None or more than one values where found for patch request!");

        try{
        	Shop shop = dataAccess.patchShop(id, update_parameter,value);
            return new JsonShopRepresentation(shop);
        }
        catch(org.springframework.dao.DuplicateKeyException e){
        	throw new ResourceException(400,"Input parameters have conflict with another shop in the database");
        }    
    }
    
    @SuppressWarnings("unchecked")
	@Override
    protected Representation delete() throws ResourceException {
    	int id;
    	String id_string = getAttribute("id");
    	try {
        	id = Integer.parseInt(id_string);
        }
    	catch(NumberFormatException e){
        	throw new ResourceException(400,"Bad parameter for shop id");
        }
    	
        if(!dataAccess.getShop(id).isPresent()) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found - id: " + id_string);

        //authorization of user
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String user_token = headers.getFirstValue("X-OBSERVATORY-AUTH");
        if(user_token == null || user_token.isEmpty()) throw new ResourceException(401, "Not authorized to delete shop");
        if(!dataAccess.isLogedIn(user_token))throw new ResourceException(401, "Not authorized to delete shop");
        
        Boolean admin = dataAccess.isAdmin(user_token);
        HashMap<String,String> msg = new HashMap<String,String>();
        msg.put("message", "OK");
        
    	if(!admin) {     
    		dataAccess.patchShop(id, "withdrawn","1");
    		//Check if withdrawal was successful
    		if(dataAccess.getShop(id).get().isWithdrawn()) return new JsonMapRepresentation(msg);
    		return new JsonMessageRepresentation("Could not complete shop withdrawal");
    	}
    	else{
    		dataAccess.deleteShop(id);
    		if(!dataAccess.getShop(id).isPresent()) return new JsonMapRepresentation(msg);
    		return new JsonMessageRepresentation("Could not complete shop deletion");
    	}
    }
    
    
}