package definitions;

import base.BaseDriver;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonReader;
import utils.LocatorUtils;
import utils.ScenarioContext;

import java.util.Map;

public class ControlDefinitions {
    private WebDriver driver = BaseDriver.getDriver();
    private LocatorUtils locatorUtils = new LocatorUtils(driver);
    private Map<String, Map<String, String>> elements;
    private static final Logger logger = LoggerFactory.getLogger(ControlDefinitions.class);

    public ControlDefinitions() {
        String featureFileName = HookDefinitions.scenario.getUri().toString();
        featureFileName = featureFileName.substring(featureFileName.lastIndexOf("/") + 1);
        logger.info("Feature dosyası yüklendi: {}", featureFileName);
        this.elements = JsonReader.loadElements(featureFileName);
    }

    @When("^'(.*)' ögesinin varlığı kontrol edilir$")
    public void checkElementPresence(String key) {
        Allure.step("Kontrol ediliyor: " + key, () -> {
            if (!elements.containsKey(key)) {
                String errorMessage = "'" + key + "' JSON dosyasında bulunamadı.";
                logger.error(errorMessage);
                Allure.step(errorMessage, Status.FAILED);
                throw new IllegalArgumentException(errorMessage);
            }

            try {
                WebElement element = locatorUtils.findElement(elements.get(key));
                if (element.isDisplayed()) {
                    String successMessage = "'" + key + "' ögesi başarıyla bulundu.";
                    logger.info(successMessage);
                    Allure.step(successMessage, Status.PASSED);
                } else {
                    String warnMessage = "'" + key + "' ögesi görünür değil.";
                    logger.warn(warnMessage);
                    Allure.step(warnMessage, Status.SKIPPED);
                    throw new AssertionError(warnMessage);  // Element bulunup görünür değilse fail olmasını sağla
                }
            } catch (Exception e) {
                String errorMessage = "'" + key + "' ögesine ulaşılamadı.";
                logger.error(errorMessage, e);
                Allure.step(errorMessage, Status.FAILED);
                Allure.addAttachment("Hata Detayları", "text/plain", e.getMessage(), "UTF-8");
                throw new RuntimeException(errorMessage);  // Element bulunamadığında fail olmasını sağla
            }
        });
    }
    @When("^'(.*)' ögesinin text'inin dolu olup olmadığı kontrol edilir$")
    public void checkElementTextIsNotEmpty(String key) {
        Allure.step("Kontrol ediliyor: '" + key + "' ögesinin text'i boş değil mi?", () -> {
            if (!elements.containsKey(key)) {
                String errorMessage = "'" + key + "' JSON dosyasında bulunamadı.";
                logger.error(errorMessage);
                Allure.step(errorMessage, Status.FAILED);
                throw new IllegalArgumentException(errorMessage);
            }

            try {
                WebElement element = locatorUtils.findElement(elements.get(key));
                String elementText = element.getText().trim(); // Trim ile baştaki ve sondaki boşlukları alırız

                if (elementText.isEmpty()) {
                    // Text boş olduğunda fail olmasını sağla
                    String failMessage = "'" + key + "' ögesinin text'i boş.";
                    logger.error(failMessage);
                    Allure.step(failMessage, Status.FAILED);
                    throw new AssertionError(failMessage);  // Fail olması için AssertionError fırlat
                } else {
                    String successMessage = "'" + key + "' ögesinin text'i dolu.";
                    logger.info(successMessage);
                    Allure.step(successMessage, Status.PASSED);
                }
            } catch (Exception e) {
                String errorMessage = "'" + key + "' ögesine ulaşılamadı.";
                logger.error(errorMessage, e);
                Allure.step(errorMessage, Status.FAILED);
                Allure.addAttachment("Hata Detayları", "text/plain", e.getMessage(), "UTF-8");
                throw new RuntimeException(errorMessage);  // Hata durumunda fail olmasını sağla
            }
        });
    }

    @When("^'(.*)' ögesinin değeri '(.*)' değişkenine eşitliği kontrol edilir$")
    public void checkElementValueEqualsVariable(String key, String variableName) {
        try {
            // Elementi bul
            WebElement element = locatorUtils.findElement(elements.get(key));

            // Elementin değeri
            String elementValue = element.getText();

            // Beklenen değeri al
            String expectedValue = (String) ScenarioContext.getContext(variableName);

            // Eşitlik kontrolü
            if (elementValue.equals(expectedValue)) {
                logger.info("Değer eşit: " + elementValue + " == " + expectedValue);
                Allure.step("Değer eşit: " + elementValue + " == " + expectedValue);
            } else {
                // Eşit değilse fail olarak işaretle
                String errorMessage = "Değer eşit değil: " + elementValue + " != " + expectedValue;
                logger.error(errorMessage);
                Allure.step(errorMessage);
                throw new AssertionError(errorMessage);  // Fail olmasını sağlamak için hata fırlatıyoruz
            }
        } catch (Exception e) {
            // Hata durumunda
            logger.error("Hata oluştu: ", e);
            Allure.step("Hata oluştu: " + e.getMessage());
            throw new RuntimeException("Test sırasında hata oluştu: " + e.getMessage());  // Testin fail olmasını sağlamak için hata fırlatıyoruz
        }
    }

}
