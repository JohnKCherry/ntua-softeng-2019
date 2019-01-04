package gr.ntua.ece.softeng18b.data;

import gr.ntua.ece.softeng18b.data.model.ProductWithImage;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("rawtypes")
public class ProductWithImageRowMapper implements RowMapper {

    @Override
    public ProductWithImage mapRow(ResultSet rs, int rowNum) throws SQLException {

        long id            = rs.getLong("id");
        String name        = rs.getString("name");
        String description = rs.getString("description");
        String category    = rs.getString("category");
        boolean withdrawn  = rs.getBoolean("withdrawn");
        String tags        = rs.getString("tags");
        Blob image		   = rs.getBlob("image");

        return new ProductWithImage(id, name, description, category, withdrawn, tags, image);
    }

}
