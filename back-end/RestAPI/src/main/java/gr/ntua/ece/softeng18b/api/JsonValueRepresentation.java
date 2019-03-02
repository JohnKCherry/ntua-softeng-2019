package gr.ntua.ece.softeng18b.api;

import com.google.gson.Gson;
import gr.ntua.ece.softeng18b.data.model.Value;
import org.restlet.data.MediaType;
import org.restlet.representation.WriterRepresentation;

import java.io.IOException;
import java.io.Writer;

public class JsonValueRepresentation extends WriterRepresentation {

    private final Value value;

    public JsonValueRepresentation(Value value) {
        super(MediaType.APPLICATION_JSON);
        this.value = value;
    }

    @Override
    public void write(Writer writer) throws IOException {
        Gson gson = new Gson();
        writer.write(gson.toJson(value));
    }
}