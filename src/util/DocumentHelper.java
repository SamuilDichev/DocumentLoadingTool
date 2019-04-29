package util;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.EntityDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import java.util.List;

/**
 * Inserts {@link EntityDocument} into a given bucket
 *
 * @author Samuil Dichev
 */
public class DocumentHelper<T> {
  private static final Logger LOGGER = LoggerFactory.getLogger(DocumentHelper.class);

  /**
   * Attempts to asynchronously insert documents
   *
   * @param docs A list of documents
   * @param bucketName The bucket where the docs should be inserted
   */
  public void bulkInsert(List<EntityDocument<T>> docs, String bucketName) {
    // Open a connection to the bucket
    Bucket bucket = DBHelper.getInstance().getCluster().openBucket(bucketName);

    Observable<EntityDocument<T>> bulkOp = Observable.from(docs)
            .flatMap(new Func1<EntityDocument<T>, Observable<EntityDocument<T>>>() {

              @Override
              public Observable<EntityDocument<T>> call(EntityDocument<T> doc) {
                return bucket.repository().async().upsert(doc);
              }
            });

    // Attempt to stop backpressure problems as described in
    // https://docs.couchbase.com/java-sdk/2.7/async-programming.html
    Subscriber<EntityDocument<T>> sub = new Subscriber<EntityDocument<T>>() {
      @Override
      public void onCompleted() {
        LOGGER.info("Bulk doc insertion completed!");
      }

      @Override
      public void onError(Throwable e) {
        LOGGER.error("Error during bulk doc insertion", e);
      }

      @Override
      public void onStart() {
        request(100);
      }

      @Override
      public void onNext(EntityDocument doc) {
        request(100);
      }
    };

    bulkOp.subscribe(sub);
    bulkOp.last().toBlocking().single();
  }

  /**
   * Inserts documents one-by-one (synchronously)
   *
   * @param docs A list of documents
   * @param bucketName The bucket where the docs should be inserted
   */
  public void insert(List<EntityDocument<T>> docs, String bucketName) {
    Bucket bucket = DBHelper.getInstance().getCluster().openBucket(bucketName);

    for (EntityDocument<T> doc : docs) {
      bucket.repository().upsert(doc);
    }
  }
}
