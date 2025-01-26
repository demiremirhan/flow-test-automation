package base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
    private static final Properties properties = new Properties();
    private static final Properties envProperties =  new Properties();

    static {
        try{
            FileInputStream configFile = new FileInputStream("src/main/resources/config.properties");
            properties.load(configFile);
            logger.info("config.properties dosyası eklendi.");
            String environment = properties.getProperty("environment");
            String envFilePath= "src/main/resources/env/"+environment+".properties";
            FileInputStream envFile =new FileInputStream(envFilePath);
            envProperties.load(envFile);
            logger.info("Ortam dosyası yüklendi: {}" ,environment);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Config dosyası yüklenirken hata ile karşılaşıldı.",e);
            throw new RuntimeException("Config dosyası yüklenirken hata ile karşılaşıldı.",e);
        }
    }
    public static int getStepDelay() {
        String delay = properties.getProperty("step.delay", "0");
        return Integer.parseInt(delay);
    }
    public static String getProperty(String key){
        if (envProperties.containsKey(key)){
            return envProperties.getProperty(key);
        }
        return properties.getProperty(key);
    }
}
