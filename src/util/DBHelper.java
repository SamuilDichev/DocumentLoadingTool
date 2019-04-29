package util;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;

/**
 * @author Samuil Dichev
 */
public class DBHelper {

  private static Cluster CLUSTER;
  private static final DBHelper INSTANCE = new DBHelper();

  private DBHelper() {
    String server = Config.getInstance().getProperty("db.server");
    String user = Config.getInstance().getProperty("db.user");
    String pass = Config.getInstance().getProperty("db.pass");

    CLUSTER = CouchbaseCluster.create(server);
    CLUSTER.authenticate(user, pass);
  }

  public static DBHelper getInstance() {
    return INSTANCE;
  }

  public Cluster getCluster() {
    return CLUSTER;
  }

}
