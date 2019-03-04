package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.model.FeedEntity;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation get() throws ResourceException {
        List<FeedEntity> feed = dataAccess.getFeed();

        Map<String, Object> map = new HashMap<>();
        map.put("feed", feed);

        return new JsonMapRepresentation(map);
    }
}