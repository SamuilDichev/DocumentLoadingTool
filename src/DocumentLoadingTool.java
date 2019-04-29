import com.couchbase.client.java.document.EntityDocument;
import models.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Config;
import util.DBHelper;
import util.DocumentHelper;

import java.util.*;

/**
 * @author Samuil Dichev
 */
public class DocumentLoadingTool {
  private static final Logger LOGGER = LoggerFactory.getLogger(DocumentLoadingTool.class);

  public static void main(String[] args) throws Exception {
    // Take input from user
    int docNum = takeInput();

    // Generate docs
    List<EntityDocument<Employee>> docs = new ArrayList<>();
    for (int i = 0; i < docNum; i++) {
      Employee e = new Employee(UUID.randomUUID().toString());
      e.setName(Integer.toString(i));
      e.setEmail(e.getName() + "@gmail.com");
      e.setJobTitle("Random Job Title");

      docs.add(EntityDocument.create(e));
    }

    // Check if above the configured bulk insertion threshold and start the appropriate insertion type.
    DocumentHelper<Employee> tool = new DocumentHelper<>();
    if ((docs.size() >= Integer.parseInt(Config.getInstance().getProperty("db.bulk.threshold")))) {
      tool.bulkInsert(docs, "default");
    } else {
      tool.insert(docs, "default");
    }

    DBHelper.getInstance().getCluster().disconnect();
  }

  /**
   * Takes input from the user. Repeats on wrong input.
   *
   * @return the number of documents that should be randomly generated and inserted into the DB.
   */
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
