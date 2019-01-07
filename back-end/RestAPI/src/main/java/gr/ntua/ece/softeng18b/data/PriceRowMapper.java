package gr.ntua.ece.softeng18b.data;

import gr.ntua.ece.softeng18b.data.model.Price;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("rawtypes")
public class PriceRowMapper implements RowMapper {

    @Override
    public Price mapRow(ResultSet rs, int rowNum) throws SQLException {

        long product_id    = rs.getLong("product_id");
    	long shop_id       = rs.getLong("shop_id");
        Double price       = rs.getDouble("price");
        Date dateFrom 	   = rs.getDate("dateFrom");
        Date dateTo  	   = rs.getDate("dateTo");

        return new Price(product_id, shop_id, price, dateFrom, dateTo);
    }

}
