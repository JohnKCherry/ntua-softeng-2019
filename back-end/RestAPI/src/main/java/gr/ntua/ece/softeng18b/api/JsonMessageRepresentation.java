package gr.ntua.ece.softeng18b.api;

import com.google.gson.Gson;
import org.restlet.data.MediaType;
import org.restlet.representation.WriterRepresentation;

import java.io.IOException;
import java.io.Writer;

public class JsonMessageRepresentation extends WriterRepresentation {

    private final String message;

    public JsonMessageRepresentation(String message) {
        super(MediaType.APPLICATION_JSON);
        this.message = message;
    }

    @Override
    public void write(Writer writer) throws IOException {
        Gson gson = new Gson();
        writer.write(gson.toJson(message));
    }
}