package gr.ntua.ece.softeng18b.data;

import gr.ntua.ece.softeng18b.data.model.Value;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("rawtypes")
public class ValueRowMapper implements RowMapper {

    @Override
    public Value mapRow(ResultSet rs, int rowNum) throws SQLException {

        //long id            = rs.getLong("id");
        String value    = rs.getString("value");
        //String sum_of_shops    = rs.getString("shops");


        return new Value(value);
    }

}
