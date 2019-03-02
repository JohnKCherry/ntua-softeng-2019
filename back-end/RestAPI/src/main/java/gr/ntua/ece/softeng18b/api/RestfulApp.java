package gr.ntua.ece.softeng18b.api;

import java.util.Arrays;
import java.util.HashSet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.restlet.engine.application.CorsFilter;

public class RestfulApp extends Application {

    @Override
    public synchronized Restlet createInboundRoot() {

    	Router router = new Router(getContext());
    	
    	//Administration endpoints 
    	router.attach("/numberofusers", NumberOfUsersResource.class);
    	router.attach("/numberofshops", NumberOfShopsResource.class);
    	router.attach("/numberofadmins", NumberOfAdminsResource.class);
    	router.attach("/numberofproducts", NumberOfProductsResource.class);
    	router.attach("/numberofactiveusers", NumberOfActiveCrowdsourcer.class);
    	router.attach("/numberofbanusers", NumberOfBanUsersResource.class);
    	router.attach("/numberofactiveshops", NumberOfActiveShopsResource.class);
    	router.attach("/numberofwithdrawnshops", NumberOfWithdrawnShopsResource.class);
    	router.attach("/numberofactiveproducts", NumberOfActiveProductsResource.class);
    	router.attach("/numberofactiveprices", NumberOfActivePricesResource.class);
    	router.attach("/numberofpastprices", NumberOfPastPricesResource.class);
    	router.attach("/users", UsersResource.class);
    	//GET
    	router.attach("/users/{id}", UserResource.class);
    	router.attach("/isadmin", IsAdminResource.class);
    	
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
        
        //GET, POST
        router.attach("/productswithimage", ProductsWithImageResource.class);
        
        //GET 
        router.attach("/productsbynamewithimage/{name}", ProductsByNameWithImageResource.class);
        
        //GET 
        router.attach("/productsbyname/{name}", ProductsByNameResource.class);
        router.attach("/productsbyname/", ProductsByNameResource.class);
        
        //GET 
        router.attach("/shopsbyname/{name}", ShopsByNameResource.class);
        router.attach("/shopsbyname/", ShopsByNameResource.class);
        
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
        
        //TODO: endpoint for accepting contact form
        

        CorsFilter corsFilter = new CorsFilter(getContext(), router);
    	corsFilter.setAllowedOrigins(new HashSet<String>(Arrays.asList("*")));
    	corsFilter.setAllowedCredentials(true);
    	corsFilter.setAllowingAllRequestedHeaders(true);
        return corsFilter;
        //return router;
    }

}
