package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
import gr.ntua.ece.softeng18b.data.model.Product;
import gr.ntua.ece.softeng18b.data.model.ProductWithImage;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.util.Streams;
import org.restlet.data.Header;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsWithImageResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation get() throws ResourceException {
    	String startAttr	= getQuery().getValues("start");
    	String countAttr	= getQuery().getValues("count");
    	String statusAttr	= getQuery().getValues("status");
    	String sortAttr 	= getQuery().getValues("sort");
    	String formatAttr	= getQuery().getValues("format");
    	
    	if(formatAttr!=null && !formatAttr.equals("json")) throw new ResourceException(400,"Only json format is supported at the moment");
    	
    	int start, count, status;
    	String sort = sortAttr;
    	
        try {
            start = Integer.parseInt(startAttr);
        } catch(NumberFormatException e) {
        	start = 0; //default
        }
        //////////////////////////////////////////////
        try {
        	if(sort == null) throw  new NumberFormatException("The sort attribute entered, " + sort + " is invalid."); 
            if(sort.equals("id|ASC")) sort = "id ASC";
            else if(sort.equals("id|DESC")) sort = "id DESC";
            else if(sort.equals("name|ASC")) sort = "name ASC";
            else if(sort.equals("name|DESC")) sort = "name DESC";
            else throw  new NumberFormatException("The sort attribute entered, " + sort + " is invalid."); 
        } catch(NumberFormatException e) {
        	sort = "id DESC"; //default
        }
        //////////////////////////////////////////////
    
        try {
            count = Integer.parseInt(countAttr);
        } catch(NumberFormatException e) {
        	count = 10; //default
        }
        //////////////////////////////////////////////

        try {
        	if(statusAttr == null) throw  new NumberFormatException("The sort attribute entered, " + sort + " is invalid."); 
        	if(statusAttr.equals("ACTIVE")) status = 0;
            else if (statusAttr.equals("WITHDRAWN")) status = 1;
            else if (statusAttr.equals("ALL")) status = -1; // -1 for all products
            else throw  new NumberFormatException("The status attribute entered, " + sort + " is invalid."); 
        } catch(NumberFormatException e) {
        	status = 0; //default
        }
        //////////////////////////////////////////////

        
    	
        List<ProductWithImage> products = dataAccess.getProductsWithImage(new Limits(start,count),status,sort);

        Map<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("count", count);
        map.put("total", products.size());
        map.put("products", products);

        return new JsonMapRepresentation(map);
    }
    
    @SuppressWarnings("unchecked")
	@Override
	
    protected Representation post(Representation entity) throws ResourceException {
    	Representation result = null;
    	HashMap<String, String> map = new HashMap<String, String>();
    	byte[] fileContent = null;
    	byte[] image = null;
        if (entity != null) {
            if (MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)) {
                // 1/ Create a factory for disk-based file items
                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setSizeThreshold(1000240);

                // 2/ Create a new file upload handler based on the Restlet
                // FileUpload extension that will parse Restlet requests and
                // generates FileItems.
                RestletFileUpload upload = new RestletFileUpload(factory);

                // 3/ Request is parsed by the handler which generates a
                // list of FileItems
                FileItemIterator fileIterator = null;
				try {
					fileIterator = upload.getItemIterator(entity);
				} catch (FileUploadException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

                try {
					while (fileIterator.hasNext()) {
					    FileItemStream fi = fileIterator.next();
					    if (fi.getFieldName().equals("fileToUpload")) {
					        // consume the stream immediately, otherwise the stream
					        // will be closed.
					        StringBuilder sb = new StringBuilder("");
					        //sb.append(fi.getContentType()).append("\n");
					        //sb.append("file name : ");
					        //sb.append(fi.getName()).append("\n");
					        BufferedReader br = new BufferedReader(new InputStreamReader(fi.openStream()));
					        String line = null;
					        while ((line = br.readLine()) != null) {
					            sb.append(line);
					        }
					        //sb.append("\n");
					        result = new StringRepresentation(sb.toString(), MediaType.TEXT_PLAIN);
					        fileContent = sb.toString().trim().getBytes();
					        image = fileContent;
					        //map.put("image", sb.toString());
					        //map.put("image",Streams.asString(fi.openStream()));
					    }
					    else{
					    	String fieldName = fi.getFieldName();
					        String fieldValue = Streams.asString(fi.openStream());
					        map.put(fieldName, fieldValue);
					        System.out.println(fieldName);
					        System.out.println(fieldValue);
					    }
					}
				} catch (FileUploadException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            } else {
                // POST request with no entity.
                setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            }
        }

        //Form ret = new Form(result);
        //Read the parameters
        String name = map.get("name");
        String description = map.get("description");
        String category = map.get("category");
        String tags = map.get("tags");
        //String image_string = map.get("image");
        
        //authorization of user
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String user_token = headers.getFirstValue("X-OBSERVATORY-AUTH");
        //if(user_token == null || user_token.isEmpty()) throw new ResourceException(401, "Not authorized to add product");
        //if(!dataAccess.isLogedIn(user_token))throw new ResourceException(401, "Not authorized to add product");

        
        //validate the values (in the general case)
        if(name == null || description == null || category == null) throw new ResourceException(400,"This operation needs more parameters");
        if(tags == null) tags = "";
        //String regex = "^[a-zA-Z0-9\\s.\\-.\\,.\\'.\\[.\\[.\\(.\\).\\..\\+.\\-.\\:.\\@]+$";
        //String regex_s = "^[a-zA-Z0-9\\s.\\-.\\,.\\'.\\[.\\[.\\(.\\)]+$";
        //if(!name.matches(regex) || !description.matches(regex) || !category.matches(regex_s) || !tags.matches(regex_s) ) throw new ResourceException(400);
        
        try{
        	Product product = dataAccess.addProductWithImage(name, description, category, false, tags, image);
        	return new JsonProductRepresentation(product);
        }
        catch(org.springframework.dao.DuplicateKeyException e){
        	throw new ResourceException(400,"This product already exists in the database");
        }
        
    }
    
}
