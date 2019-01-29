package apiTest;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

// The multi get API allows to get a list of documents based on their index, type and id
public class TransportClientToMuitiGet {
    public static void main(String[] args) throws UnknownHostException, NullPointerException {
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        MultiGetResponse responses = client.prepareMultiGet()
                .add("twitter", "tweet", "1") // get by a single id
                .add("twitter", "tweet", "2", "3", "4") // by a list of ids for the same index / type
                .add("another", "type", "foo") // you can also get from another index
                .get();

        for (MultiGetItemResponse itemResponse : responses) { // iterate over the result set
            GetResponse getResponse = itemResponse.getResponse();
            if (getResponse.isExists()) { // check if the document exists
                String json = getResponse.getSourceAsString(); // access to the _source field
                System.out.println(json);
            }
        }
        client.close();
    }
}
