import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import static org.elasticsearch.common.xcontent.XContentFactory.*;


import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

// The bulk API allows one to index and delete several documents in a single request.
public class TransportClientToUseBulkApi {
    public static void main(String[] args) throws IOException {
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        BulkRequestBuilder bulkRequest = client.prepareBulk();

        bulkRequest.add(client.prepareIndex("twitter", "tweet", "5")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "vitalik5")
                        .field("postDate", new Date())
                        .field("message", "message #5")
                        .endObject())
        );

        bulkRequest.add(client.prepareIndex("twitter", "tweet", "6")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "another")
                        .field("postDate", new Date())
                        .field("message", "message #6")
                        .endObject()
                )
        );

        bulkRequest.add(client.prepareDelete("twitter", "tweet", "6"));

        BulkResponse responses = bulkRequest.get();
        if (responses.hasFailures()) {
            // process failures by iterating through each bulk response item
        }
    }
}
