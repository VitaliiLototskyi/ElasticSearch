package elastic;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class Controller {
    public static void main(String[] args) throws IOException {
//        Message message = new Message("1", "1.2.3.4", new Date(), "12324", "google.com",
//                "ok", 5, "qwerty", "chrome");
//        IndexResponse response = Controller.toIndex("eventmaker", message);
//        System.out.println(response.toString());
        Controller.toDeleteList("eventmaker", "messageId", "1");

    }

    public static IndexResponse toIndex(String indexName, Message message) throws IOException {
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        IndexResponse response = client.prepareIndex(indexName, "event")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("messageId", message.getId())
                        .field("clientIP", message.getClientIP())
                        .field("sentTime", message.getSentTime())
                        .field("uuid", message.getUuid())
                        .field("requestURL", message.getRequestURL())
                        .field("responseCode", message.getResponseCode())
                        .field("fileSize", message.getFileSize())
                        .field("client_location", message.getClient_location())
                        .field("browser", message.getBrowser())
                        .endObject()).get();
        client.close();
        return response;
    }

    public static void toDeleteList(String indexName, String searchByField, String searchByValue) throws UnknownHostException {
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                .filter(QueryBuilders.matchQuery(searchByField, searchByValue))
                .source(indexName)
                .get();
        long deleted = response.getDeleted(); // number of deleted documents
        System.out.println(response.toString() + " Deleted " + deleted + " documents");
        client.close();
    }
}
