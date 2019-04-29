import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.EntityDocument;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import models.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DBHelper;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

/**
 * @author Samuil Dichev
 */
public class DocTool {
  private static final Logger LOGGER = LoggerFactory.getLogger(DocTool.class);

  public static void main(String[] args) throws Exception {
    // Take input from user
    int docNum = takeInput();

    // Initialize the Connection
    Bucket bucket = DBHelper.getInstance().getCluster().openBucket("default");

    // Generate docs
    for (int i = 0; i < docNum; i++) {
      Employee e = new Employee(UUID.randomUUID().toString());
      e.setName(Integer.toString(i));
      e.setEmail(e.getName() + "@gmail.com");
      e.setJobTitle("Random Job Title");

      EntityDocument<Employee> doc = EntityDocument.create(e.getId(), e);
      bucket.repository().upsert(doc);
    }
  }

  private static int takeInput() {
    Scanner in = new Scanner(System.in);
    System.out.print("Enter number of documents to generate: ");
    int docNum;
    try {
      docNum = in.nextInt();
    } catch (InputMismatchException e) {
      LOGGER.error("Wrong input type, please type a number.");
      docNum = takeInput();
    }

    return docNum;
  }
}
