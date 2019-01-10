package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
import gr.ntua.ece.softeng18b.data.model.ProductWithImage;
import gr.ntua.ece.softeng18b.data.model.Shop;

import org.restlet.data.Form;
import org.restlet.data.Header;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProductsByNameWithImageResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation get() throws ResourceException {

        String name = getAttribute("name");
        
        String formatAttr	= getQuery().getValues("format");
    	if(formatAttr!=null && !formatAttr.equals("json")) throw new ResourceException(400,"Only json format is supported at the moment");

        if (name == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing product name");
        }

        List<ProductWithImage> products = dataAccess.getProductsByNameWithImage(name);

        Map<String, Object> map = new HashMap<>();
        map.put("total", products.size());
        map.put("products", products);

        return new JsonMapRepresentation(map);
    }
}
