package gr.ntua.ece.softeng18b.data;

import gr.ntua.ece.softeng18b.data.model.Shop;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("rawtypes")
public class ShopRowMapper implements RowMapper {

    @Override
    public Shop mapRow(ResultSet rs, int rowNum) throws SQLException {

        long id            = rs.getLong("id");
        String name        = rs.getString("name");
        String address	   = rs.getString("address");
        double lng  	   = rs.getDouble("x_coordinate");
        double lat 		   = rs.getDouble("y_coordinate");
        boolean withdrawn  = rs.getBoolean("withdrawn");
        String tags        = rs.getString("tags");

        return new Shop(id, name, address, lng, lat, tags, withdrawn);
    }

}
