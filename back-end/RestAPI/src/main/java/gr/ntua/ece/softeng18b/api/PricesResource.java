package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
import gr.ntua.ece.softeng18b.data.model.Price;
import org.restlet.data.Form;
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
        dateFrom = Date.valueOf(dateFrom_string);
        dateTo =Date.valueOf(dateTo_string);
        try{
        	Price price_r = dataAccess.addPrice(product_id, shop_id, price, dateFrom, dateTo);
        	return new JsonPriceRepresentation(price_r);
        }
        catch(org.springframework.dao.DuplicateKeyException e){
        	throw new ResourceException(400,"A price for this product in this shop already exists in the database");
        }

        
    }
    
}
