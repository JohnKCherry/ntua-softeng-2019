package gr.ntua.ece.softeng18b.data.model;

import java.sql.Date;
import java.util.Objects;

public class Price {

    private final long product_id;
    private final long shop_id;
    private final Double price;
    private final Date dateFrom;
    private final Date dateTo;

    public Price(long product_id, long shop_id, Double price, Date dateFrom, Date dateTo) {
        this.product_id          = product_id;
        this.shop_id        	 = shop_id;
        this.price			   	 = price;
        this.dateFrom    		 = dateFrom;
        this.dateTo   			 = dateTo;
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

    public Date getDateFrom() {
        return dateFrom;
    }
    
    public Date getDateTo() {
        return dateTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return (this.product_id == price.getProductId() && this.shop_id == price.getShoptId() && this.getPrice() == price.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(product_id,shop_id);
    }
}
