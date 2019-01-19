package gr.ntua.ece.softeng18b.data.model;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PriceResultSingleDateXprimal {

    private final long product_id;
    private final long shop_id;
    private final Double price;
    private final Date date;
    private final String product_name;
    private final List<String> product_tags;
    private final String shop_name;
    private final List<String> shop_tags;
    private final String shop_address;
    private final int shop_dist;
    
    

    public PriceResultSingleDateXprimal(long product_id, long shop_id, Double price, Date date, String product_name, String product_tags, String shop_name, String shop_tags, String shop_address, Double shop_dist) {
        this.product_id          = product_id;
        this.shop_id        	 = shop_id;
        this.price			   	 = price;
        this.date    		 = date;
        this.product_name		 = product_name;
        this.product_tags 		 = Arrays.asList(product_tags.split("\\s*(=>|,|\\s)\\s*"));
        this.shop_name			 = shop_name;
        this.shop_tags 			 = Arrays.asList(shop_tags.split("\\s*(=>|,|\\s)\\s*"));
        this.shop_address	 	 = shop_address;
        this.shop_dist			 = Integer.valueOf((int) Math.round(shop_dist));
        
    }

    public long getProductId() {
        return product_id;
    }
    
    public long getShoptId() {
        return shop_id;
    }

    public Double getPrice() {
        return price;
    }

    public Date getDate() {
        return date;
    }
    
    public String getProductName() {
    	return product_name;
    }
    
    public String getShopName() {
    	return shop_name;
    }
    
    public String getShopAddress() {
    	return shop_address;
    }
    
    public int getShopDist() {
    	return shop_dist;
    }
    
    public List<String> getProductTags() {
        return product_tags;
    }
    
    public List<String> getShopTags() {
        return shop_tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceResultSingleDateXprimal price = (PriceResultSingleDateXprimal) o;
        return (this.product_id == price.getProductId() && this.shop_id == price.getShoptId() && this.getPrice() == price.getPrice() && this.date.equals(price.getDate()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(product_id,shop_id,date);
    }
}
