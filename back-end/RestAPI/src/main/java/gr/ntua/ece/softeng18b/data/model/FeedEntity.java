package gr.ntua.ece.softeng18b.data.model;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FeedEntity {

    private final long id;
    private final long product_id;
    private final long shop_id;
    private final double price;
    private final Timestamp timestamp;

    public FeedEntity(long id, long product_id, long shop_id, double price, Timestamp timestamp) {
        this.id        		= id;
        this.product_id     = product_id;
        this.shop_id		= shop_id;
        this.price    		= price;
        this.timestamp   	= timestamp;
    }

    public long getId() {
        return id;
    }

    public long getProductId() {
        return product_id;
    }

    public long getShopId() {
        return shop_id;
    }

    public double price() {
        return price;
    }
    
    public Timestamp timestamp() {
        return timestamp;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
