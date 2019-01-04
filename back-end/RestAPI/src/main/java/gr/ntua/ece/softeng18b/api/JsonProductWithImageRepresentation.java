package gr.ntua.ece.softeng18b.api;

import com.google.gson.Gson;
import gr.ntua.ece.softeng18b.data.model.ProductWithImage;
import org.restlet.data.MediaType;
import org.restlet.representation.WriterRepresentation;

import java.io.IOException;
import java.io.Writer;

public class JsonProductWithImageRepresentation extends WriterRepresentation {

    private final ProductWithImage productwithimage;

    public JsonProductWithImageRepresentation(ProductWithImage productwithimage) {
        super(MediaType.APPLICATION_JSON);
        this.productwithimage = productwithimage;
    }

    @Override
    public void write(Writer writer) throws IOException {
        Gson gson = new Gson();
        writer.write(gson.toJson(productwithimage));
    }
}