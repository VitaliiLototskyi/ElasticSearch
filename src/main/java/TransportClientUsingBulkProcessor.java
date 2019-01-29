import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

// The BulkProcessor class offers a simple interface to flush bulk operations automatically based on the number
// or size of requests, or after a given period.
public class TransportClientUsingBulkProcessor {
    public static void main(String[] args) throws UnknownHostException {
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        BulkProcessor bulkProcessor = BulkProcessor.builder(
                client,// Add your elasticsearch client
                new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long executionId,
                                           BulkRequest request) {
                        // this method is called just before bulk is executed.
                        // You can for example see the numberOfActions with request.numberOfActions()
                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          BulkResponse response) {
                        // This method is called after bulk execution. You can for example check
                        // if there was some failing requests with response.hasFailures()
                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          Throwable failure) {
                        //This method is called when the bulk failed and raised a Throwable
                    }
                })
                .setBulkActions(10000) // to execute the bulk every 10 000 requests
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB)) // to flush the bulk every 5mb
                .setFlushInterval(TimeValue.timeValueSeconds(5)) // to flush the bulk every 5 seconds
                .setConcurrentRequests(1)//	Set the number of concurrent requests. A value of 0 means that only
                // a single request will be allowed to be executed. A value of 1 means 1 concurrent request is allowed
                // to be executed while accumulating new bulk requests.
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                // Set a custom backoff policy which will initially wait for 100ms, increase exponentially and
                // retries up to three times. A retry is attempted whenever one or more bulk item requests have
                // failed with an EsRejectedExecutionException which indicates that there were too little compute
                // resources available for processing the request. To disable backoff, pass BackoffPolicy.noBackoff().
                .build();
        // By default, BulkProcessor:
        //sets bulkActions to 1000
        //sets bulkSize to 5mb
        //does not set flushInterval
        //sets concurrentRequests to 1, which means an asynchronous execution of the flush operation.
        //sets backoffPolicy to an exponential backoff with 8 retries and a start delay of 50ms.
        // The total wait time is roughly 5.1 seconds.

        bulkProcessor.add(new IndexRequest("twitter", "tweet", "1"));
        bulkProcessor.add(new DeleteRequest("twitter", "tweet", "2"));
        bulkProcessor.close();
    }
}
