package apiTest;

import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class TransportClientToUpdate {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        UpdateRequest request = new UpdateRequest("index", "type", "1")
                .doc(jsonBuilder()
                        .startObject()
                        .field("gender", "male")
                        .endObject());
        client.update(request).get();

        System.out.println(request.toString());
        client.close();


        /*
        client.prepareUpdate("ttl", "doc", "1")
                // Your script. It could also be a locally stored script name. In that case, you’ll need to use ScriptService.ScriptType.FILE
                .setScript(new Script("ctx._source.gender = \"male\"", ScriptService.ScriptType.INLINE, null, null))
                .get();

        client.prepareUpdate("ttl", "doc", "1")
                .setDoc(jsonBuilder()
                        .startObject()
                        .field("gender", "male")
                        .endObject())
                .get();
        */

        /* Upsert-> If the document does not exist, the content of the upsert element will be used to index the fresh doc
        IndexRequest indexRequest = new IndexRequest("index", "type", "1")
                .source(jsonBuilder()
                        .startObject()
                        .field("name", "Joe Smith")
                        .field("gender", "male")
                        .endObject());
        UpdateRequest updateRequest = new UpdateRequest("index", "type", "1")
                .doc(jsonBuilder()
                        .startObject()
                        .field("gender", "male")
                        .endObject())
                .upsert(indexRequest); // If the document does not exist, the one in indexRequest will be added
        client.update(updateRequest).get();
        */
    }
}
