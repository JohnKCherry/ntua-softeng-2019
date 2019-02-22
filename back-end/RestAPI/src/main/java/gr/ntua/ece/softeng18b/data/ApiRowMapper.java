package gr.ntua.ece.softeng18b.data;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("rawtypes")
public class ApiRowMapper implements RowMapper {

    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {

        String password_hash            = rs.getString("password");
        int salt        			= rs.getInt("salt");
        
        return password_hash+salt;
    }

}
