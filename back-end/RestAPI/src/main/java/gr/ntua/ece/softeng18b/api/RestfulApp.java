package gr.ntua.ece.softeng18b.api;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class RestfulApp extends Application {

    @Override
    public synchronized Restlet createInboundRoot() {

    	Router router = new Router(getContext());

        //GET, POST
        router.attach("/products", ProductsResource.class);

        //GET, PATCH, PUT, DELETE
        router.attach("/products/{id}", ProductResource.class);
        
        //GET, PATCH, PUT, DELETE
        router.attach("/productswithimage/{id}", ProductWithImageResource.class);
        
        //GET, POST
        router.attach("/shops", ShopsResource.class);
        
        //GET, PATCH, PUT, DELETE
        router.attach("/shops/{id}", ShopResource.class);
       
        //GET, POST
        router.attach("/prices", PricesResource.class);

        return router;
    }

}
