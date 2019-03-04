package gr.ntua.ece.softeng18b.data;

import gr.ntua.ece.softeng18b.data.model.FeedEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@SuppressWarnings("rawtypes")
public class FeedEntityRowMapper implements RowMapper {

    @Override
    public FeedEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

        long id            	= rs.getLong("id");
        long product_id    	= rs.getLong("product_id");
        long shop_id 	   	= rs.getLong("shop_id");
        double price    	= rs.getDouble("price");
        Timestamp timestamp = rs.getTimestamp("date");

        return new FeedEntity(id, product_id, shop_id, price, timestamp);
    }

}
