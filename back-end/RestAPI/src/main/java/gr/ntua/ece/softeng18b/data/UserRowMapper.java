package gr.ntua.ece.softeng18b.data;

import gr.ntua.ece.softeng18b.data.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("rawtypes")
public class UserRowMapper implements RowMapper {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {

        long id            = rs.getLong("id");
        String fullname    = rs.getString("fullname");
        String username = rs.getString("username");
        String email    = rs.getString("email");
        int authorization       = rs.getInt("authorization");

        return new User(id, fullname, username, email, authorization);
    }

}
