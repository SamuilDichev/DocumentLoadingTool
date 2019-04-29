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
 * @author Samuil Dichev
 */
public class DocumentHelper<T> {
  private static final Logger LOGGER = LoggerFactory.getLogger(DocumentHelper.class);

  public void bulkInsert(List<EntityDocument<T>> docs, String bucketName) {
    Bucket bucket = DBHelper.getInstance().getCluster().openBucket(bucketName);

    Observable<EntityDocument<T>> bulkOp = Observable.from(docs)
            .flatMap(new Func1<EntityDocument<T>, Observable<EntityDocument<T>>>() {

              @Override
              public Observable<EntityDocument<T>> call(EntityDocument<T> doc) {
                return bucket.repository().async().upsert(doc);
              }
            });

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

  public void insert(List<EntityDocument<T>> docs, String bucketName) {
    Bucket bucket = DBHelper.getInstance().getCluster().openBucket(bucketName);

    for (EntityDocument<T> doc : docs) {
      bucket.repository().upsert(doc);
    }
  }
}
