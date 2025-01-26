package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class JsonReader {
    private static final Logger logger = LoggerFactory.getLogger(JsonReader.class);

    public static Map<String, Map<String, String>> loadElements(String featureFileName) {
        try {
            String jsonFileName = featureFileName.replace(".feature", ".json");
            String jsonFilePath = "src/test/java/elements/" + jsonFileName;

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Map<String, String>> elements = objectMapper.readValue(
                    new File(jsonFilePath),
                    new TypeReference<Map<String, Map<String, String>>>() {}
            );

            logger.info("JSON dosyasından elementler yüklendi: {}", jsonFilePath);
            Allure.step("JSON dosyası başarıyla yüklendi: " + jsonFilePath);
            return elements;
        } catch (IOException e) {
            String errorMessage = "JSON dosyası yüklenirken hata oluştu: " + featureFileName;
            logger.error(errorMessage, e);

            // Allure'da hata mesajını bir adım olarak kaydedin
            Allure.step(errorMessage);

            // Hata detaylarını loglamak için bir Allure ek açıklaması kullanabilirsiniz
            Allure.addAttachment("Hata Detayları", e.getMessage());
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
