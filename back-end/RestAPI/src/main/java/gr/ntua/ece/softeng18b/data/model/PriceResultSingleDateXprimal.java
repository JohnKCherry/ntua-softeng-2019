package gr.ntua.ece.softeng18b.data.model;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PriceResultSingleDateXprimal {

    private final long productId;
    private final long shopId;
    private final Double price;
    private final String date;
    private final String productName;
    private final List<String> productTags;
    private final String shopName;
    private final List<String> shopTags;
    private final String shopAddress;
    private final int shopDist;
    
    

    public PriceResultSingleDateXprimal(long product_id, long shop_id, Double price, Date date, String product_name, String product_tags, String shop_name, String shop_tags, String shop_address, Double shop_dist) {
        this.productId          = product_id;
        this.shopId        	 	= shop_id;
        this.price			   	= price;
        this.date    		 	= date.toString();
        this.productName		= product_name;
        this.productTags 		= Arrays.asList(product_tags.split("\\s*(=>|,|\\s)\\s*"));
        this.shopName			= shop_name;
        this.shopTags 			= Arrays.asList(shop_tags.split("\\s*(=>|,|\\s)\\s*"));
        this.shopAddress	 	= shop_address;
        this.shopDist			= Integer.valueOf((int) Math.round(shop_dist));
        
    }

    public long getProductId() {
        return productId;
    }
    
    public long getShoptId() {
        return shopId;
    }

    public Double getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }
    
    public String getProductName() {
    	return productName;
    }
    
    public String getShopName() {
    	return shopName;
    }
    
    public String getShopAddress() {
    	return shopAddress;
    }
    
    public int getShopDist() {
    	return shopDist;
    }
    
    public List<String> getProductTags() {
        return productTags;
    }
    
    public List<String> getShopTags() {
        return shopTags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceResultSingleDateXprimal price = (PriceResultSingleDateXprimal) o;
        return (this.productId == price.getProductId() && this.shopId == price.getShoptId() && this.getPrice() == price.getPrice() && this.date.equals(price.getDate()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId,shopId,date);
    }
}
