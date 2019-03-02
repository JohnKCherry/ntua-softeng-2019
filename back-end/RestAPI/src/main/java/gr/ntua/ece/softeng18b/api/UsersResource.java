package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
import gr.ntua.ece.softeng18b.data.model.Product;
import gr.ntua.ece.softeng18b.data.model.User;
import org.restlet.data.Form;
import org.restlet.data.Header;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersResource extends ServerResource {

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
        } catch(NumberFormatException e) {
        	start = 0; //default
        }
        //////////////////////////////////////////////
        try {
        	if(sort == null) throw  new NumberFormatException("The sort attribute entered, " + sort + " is invalid."); 
            if(sort.equals("id|ASC")) sort = "id ASC";
            else if(sort.equals("id|DESC")) sort = "id DESC";
            else if(sort.equals("username|ASC")) sort = "username ASC";
            else if(sort.equals("username|DESC")) sort = "username DESC";
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
/*
        try {
        	if(statusAttr == null) throw  new NumberFormatException("The status attribute entered, " + statusAttr + " is invalid."); 
        	if(statusAttr.equals("ADMIN")) status = 3;
            else if (statusAttr.equals("BAN")) status = 1;
            else if (statusAttr.equals("ACTIVE")) status = 2; // -1 for all products
            else if (statusAttr.equals("ALL")) status = -1; // -1 for all products
            else throw  new NumberFormatException("The status attribute entered, " + statusAttr+ " is invalid."); 
        } catch(NumberFormatException e) {
        	status = 0; //default
        }
   */     
        ////
        try {
        	if(statusAttr == null || statusAttr.isEmpty()) throw  new NumberFormatException("The status attribute entered, " + statusAttr + " is invalid."); 
        	if(statusAttr.equals("ADMIN")) status = 3;
            else if (statusAttr.equals("BAN")) status = 1;
            else if (statusAttr.equals("ACTIVE")) status = 2; // -1 for all products
            else if (statusAttr.equals("ALL")) status = -1; // -1 for all products
            else throw new ResourceException(400,"Bad value for field status"); //throw  new NumberFormatException("The status attribute entered, " + statusAttr+ " is invalid."); 
        } catch(NumberFormatException e) 
        {
        	status = 0; //default
        }
        
        //////////////////////////////////////////////
/*„È· ÙÔÌ ÂÎÂ„˜Ô !!! Õ¡ ‘œ ¬√¡ÀŸ !!
      //authorization of user
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String user_token = headers.getFirstValue("X-OBSERVATORY-AUTH");
        if(user_token == null || user_token.isEmpty()) throw new ResourceException(401, "Administrator authorization is required to access this endpoint");
        if(!dataAccess.isLogedIn(user_token))throw new ResourceException(401, "Administrator authorization is required to access this endpoint");
        
        Boolean admin = dataAccess.isAdmin(user_token);
        
    	if(!admin) {     
    		throw new ResourceException(401, "Administrator authorization is required to access this endpoint");
    	}
*/
    	if (status==0) throw new ResourceException(400,"Bad value for field status");
        List<User> users = dataAccess.getUsers(new Limits(start,count),status,sort);

        Map<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("count", count);
        map.put("total", users.size());
        map.put("users", users);

        return new JsonMapRepresentation(map);
    }

  
    
}
