package gr.ntua.ece.softeng18b.data;

import gr.ntua.ece.softeng18b.data.model.PriceResult;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("rawtypes")
public class PriceResultRowMapper implements RowMapper {

    @Override
    public PriceResult mapRow(ResultSet rs, int rowNum) throws SQLException {

        long product_id    	= rs.getLong("product_id");
    	long shop_id       	= rs.getLong("shop_id");
        Double price       	= rs.getDouble("price");
        Date dateFrom 	   	= rs.getDate("dateFrom");
        Date dateTo  	   	= rs.getDate("dateTo");
        String product_name = rs.getString("product_name"); 
        String shop_name 	= rs.getString("shop_name"); 
        String shop_address = rs.getString("shop_address"); 
        Double shop_dist = 0.0;
        try{
        	shop_dist	= rs.getDouble("shopDist");
        }
        catch(SQLException e) {}
        String shop_tags 	= rs.getString("shop_tags"); 
        String product_tags = rs.getString("product_tags"); 
        

        return new PriceResult(product_id, shop_id, price, dateFrom, dateTo, product_name, product_tags, shop_name, shop_tags, shop_address, shop_dist);
    }

}
