package apiTest;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import for elastic helper
import static org.elasticsearch.common.xcontent.XContentFactory.*;

import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;


//The index API allows one to index a typed JSON document into a specific index and make it searchable.
public class TransportClientToPost {
    public static void main(String[] args) {
        try {
            //settings
            //dynamically add new hosts and remove old ones.
            //Settings settings = Settings.builder().put("client.transport.sniff", true).build();
            Settings settings = Settings.builder()
                    .put("cluster.name", "elasticsearch").build();
            TransportClient client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
            //post by jsonBuilder
            IndexResponse response = client.prepareIndex("twitter", "tweet", "1")
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("name", "vitalik")
                            .field("postDate", new Date())
                            .field("message", "trying out elastic")
                            .endObject()).get();
            // Index name
            String _index = response.getIndex();
            // Type name
            String _type = response.getType();
            // Document ID (generated or not)
            String _id = response.getId();
            // Version (if it's the first time you index this document, you will get: 1)
            long _version = response.getVersion();
            // status has stored current instance statement.
            RestStatus status = response.status();
            System.out.println(_index + _type + _id + _version + status);
            // shutdown
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* post by String
    String json = "{" +
            "\"user\":\"vitalik\"," +
            "\"postDate\":\"2019-01-30\"," +
            "\"message\":\"trying out Elasticsearch\"" +
            "}";

    IndexResponse response = client.prepareIndex("twitter", "tweet")
            .setSource(json, XContentType.JSON)
            .get();
    */

    /* post by Map
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("user", "vitalik");
        json.put("postDate", new Date());
        json.put("message", "Trying out ElasticSearch");
    */

    /* post by XContentFactory.jsonBuilder()
    IndexResponse response = client.prepareIndex("twitter", "tweet", "1")
            .setSource(jsonBuilder()
                    .startObject()
                    .field("user", "vitalik")
                    .field("postDate", new Date())
                    .field("message", "trying out elastic")
                    .endObject()).get();
     */

}