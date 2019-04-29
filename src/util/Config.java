package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author Samuil Dichev
 */
public class Config {
  private Properties configFile;
  private static final Config INSTANCE = new Config();
  private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

  private Config() {
    configFile = new java.util.Properties();

    try {
      configFile.load(this.getClass().getClassLoader().getResourceAsStream("main.conf"));
    } catch (Exception e) {
      LOGGER.error("Could not load main.conf", e);
    }
  }

  public String getProperty(String key) {
    String value = this.configFile.getProperty(key);
    return value;
  }

  public static Config getInstance() {
    return INSTANCE;
  }
}
