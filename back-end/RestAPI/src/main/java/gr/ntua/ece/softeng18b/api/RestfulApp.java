package gr.ntua.ece.softeng18b.api;

import java.util.Arrays;
import java.util.HashSet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.restlet.service.CorsService;
import org.restlet.routing.Filter;
import org.restlet.engine.application.CorsFilter;

public class RestfulApp extends Application {

    @Override
    public synchronized Restlet createInboundRoot() {

    	Router router = new Router(getContext());
    	
    	//POST
        router.attach("/login", LoginResource.class);
        
        //POST
        router.attach("/signup", SignupResource.class);
        
        //POST
        router.attach("/logout", LogoutResource.class);
        
        //POST, PATCH
        router.attach("/profile", ProfileResource.class);

        //GET, POST
        router.attach("/products", ProductsResource.class);

        //GET, PATCH, PUT, DELETE
        router.attach("/products/{id}", ProductResource.class);
        
        // GET 
        router.attach("/productswithimage/{id}", ProductWithImageResource.class);
        
        //GET
        router.attach("/productswithimage", ProductsWithImageResource.class);
        
        //GET 
        router.attach("/productsbynamewithimage/{name}", ProductsByNameWithImageResource.class);
        
        //GET, POST
        router.attach("/shops", ShopsResource.class);
        
        //GET, PATCH, PUT, DELETE
        router.attach("/shops/{id}", ShopResource.class);
       
        //GET, POST, DELETE
        router.attach("/prices", PricesResource.class);
        
        //DELETE
        router.attach("/prices/{product_id}/{shop_id}", PricesResource.class);
        
        //GET, POST
        router.attach("/favourites", FavouritesResource.class);
        
        //DELETE
        router.attach("/favourites/{productId}", FavouritesResource.class);

        CorsFilter corsFilter = new CorsFilter(getContext(), router);
    	corsFilter.setAllowedOrigins(new HashSet(Arrays.asList("*")));
    	corsFilter.setAllowedCredentials(true);
    	corsFilter.setAllowingAllRequestedHeaders(true);
        return corsFilter;
        //return router;
    }

}
