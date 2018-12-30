package gr.ntua.ece.softeng18b.data;


import gr.ntua.ece.softeng18b.data.model.Product;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class DataAccess {

    private static final Object[] EMPTY_ARGS = new Object[0];

    private static final int MAX_TOTAL_CONNECTIONS = 16;
    private static final int MAX_IDLE_CONNECTIONS = 8;

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setup(String driverClass, String url, String user, String pass) throws SQLException {

        //initialize the data source
        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName(driverClass);
        bds.setUrl(url);
        bds.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        bds.setMaxIdle(MAX_IDLE_CONNECTIONS);
        bds.setUsername(user);
        bds.setPassword(pass);
        bds.setValidationQuery("SELECT 1");
        bds.setTestOnBorrow(true);
        bds.setDefaultAutoCommit(true);

        //check that everything works OK
        bds.getConnection().close();

        //initialize the jdbc template utilitiy
        jdbcTemplate = new JdbcTemplate(bds);
    }

    public List<Product> getProducts(Limits limits, long status, String sort) {
        //TODO: Support limits
    	Long[] params_small = new Long[]{limits.getStart(),(long)limits.getCount()};
    	Long[] params = new Long[]{limits.getStart(),status,(long)limits.getCount() };
    	if(status == -1) return jdbcTemplate.query("select * from products where id>=? order by "+sort+" limit ?", params_small, new ProductRowMapper());
    	return jdbcTemplate.query("select * from products where id>=? and withdrawn =? order by "+sort+" limit ?", params, new ProductRowMapper());      
    }

    public Product addProduct(String name, String description, String category, boolean withdrawn, String tags ) {
        //Create the new product record using a prepared statement
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(
                        "insert into products(name, description, category, withdrawn, tags) values(?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, name);
                ps.setString(2, description);
                ps.setString(3, category);
                ps.setBoolean(4, withdrawn);
                ps.setString(5, tags);
                return ps;
            }
        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int cnt = jdbcTemplate.update(psc, keyHolder);

        if (cnt == 1) {
            //New row has been added
            Product product = new Product(
                keyHolder.getKey().longValue(), //the newly created project id
                name,
                description,
                category,
                withdrawn,
                tags
            );
            return product;

        }
        else {
            throw new RuntimeException("Creation of Product failed");
        }
    }
    
    // Update Product: similar to addProduct
    public Product updateProduct(int id, String name, String description, String category, String withdrawn, String tags ) {
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(
                        "update products SET name=? , description=?, category=?, withdrawn=?, tags=? WHERE id=?",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, name);
                ps.setString(2, description);
                ps.setString(3, category);
                ps.setBoolean(4, withdrawn.equals("1"));
                ps.setString(5, tags);
                ps.setString(6, ""+id);
                return ps;
            }
        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int cnt = jdbcTemplate.update(psc, keyHolder);

        if (cnt == 1) {
            //A row has been updated
            Product product = new Product(
                id, //the newly created project id
                name,
                description,
                category,
                withdrawn.equals("1"),
                tags
            );
            return product;

        }
        else {
            throw new RuntimeException("Update of Product failed");
        }
    }
    
    //Patch Product similar to update product
    public Product patchProduct(int id,String update_parameter, String value) {
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE products SET "+update_parameter+"=? where id=?",
                        Statement.RETURN_GENERATED_KEYS
                );
                
                //ps.setString(1, "`"+update_parameter);
                if(update_parameter.equals("withdrawn") && value.equals("1")) ps.setBoolean(1, true);
                else if(update_parameter.equals("withdrawn") && value.equals("0")) ps.setBoolean(1,false);
                else ps.setString(1, value);
                ps.setString(2, ""+id);
                return ps;
            }
        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int cnt = jdbcTemplate.update(psc, keyHolder);

        if (cnt == 1) {
            //A specific column of a row has been updated
            Optional<Product> product = this.getProduct(id);
            if(product.isPresent()) return product.get();
            throw new RuntimeException("Retrival of patched of Product failed");

        }
        else {
            throw new RuntimeException("Patch of Product failed");
        }
    }
    
    public void deleteProduct(int id) {
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM products WHERE id=?",
                        Statement.RETURN_GENERATED_KEYS
                );
                
                ps.setString(1, ""+id);
                return ps;
            }
        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int cnt = jdbcTemplate.update(psc, keyHolder);

        if(cnt !=1 ) throw new RuntimeException("Deletion of Product failed");
        return;
    }


    public Optional<Product> getProduct(long id) {
        Long[] params = new Long[]{id};
        List<Product> products = jdbcTemplate.query("select * from products where id = ?", params, new ProductRowMapper());
        if (products.size() == 1)  {
            return Optional.of(products.get(0));
        }
        else {
            return Optional.empty();
        }
    }


}
