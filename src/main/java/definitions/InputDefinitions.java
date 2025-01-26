package definitions;

import base.BaseDriver;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonReader;
import utils.LocatorUtils;
import utils.ScenarioContext;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class InputDefinitions {

    private WebDriver driver = BaseDriver.getDriver();
    private LocatorUtils locatorUtils = new LocatorUtils(driver);
    private Map<String, Map<String, String>> elements;
    private static final Logger logger = LoggerFactory.getLogger(InputDefinitions.class);

    public InputDefinitions() {
        String featureFileName = HookDefinitions.scenario.getUri().toString();
        featureFileName = featureFileName.substring(featureFileName.lastIndexOf("/") + 1);
        logger.info("Feature dosyası yüklendi: {}", featureFileName);
        this.elements = JsonReader.loadElements(featureFileName);
    }

    @When("^'(.*)' değeri yazılır.$")
    public void writeValue(String value) {
        Allure.step("'" + value + "' değeri yazma işlemi başlatılıyor.", Status.PASSED);
        try {
            Actions actions = new Actions(driver);
            actions.sendKeys(value).perform();
            String successMessage = "'" + value + "' değeri başarıyla yazıldı.";
            Allure.step(successMessage, Status.PASSED);
            logger.info(successMessage);
        } catch (Exception e) {
            String errorMessage = "'" + value + "' değeri yazılamadı.";
            logger.error(errorMessage, e);
            Allure.step(errorMessage, Status.FAILED);
            Allure.addAttachment("Hata Detayları", e.getMessage());
        }
    }

    @When("^'(.*)' değeri '(.*)' alanına yazılır.$")
    public void writeValueToElement(String value, String key) {
        Allure.step("Değer '" + value + "' '" + key + "' alanına yazılmaya çalışılıyor.", () -> {
            if (!elements.containsKey(key)) {
                String errorMessage = "'" + key + "' alanı JSON dosyasında bulunamadı.";
                logger.error(errorMessage);
                Allure.step(errorMessage, Status.FAILED);
                throw new IllegalArgumentException(errorMessage);
            }
            try {
                WebElement element = locatorUtils.findElement(elements.get(key));
                element.clear();
                element.sendKeys(value);

                String successMessage = "'" + value + "' değeri '" + key + "' alanına başarıyla yazıldı.";
                logger.info(successMessage);
                Allure.step(successMessage, Status.PASSED);
            } catch (Exception e) {
                String errorMessage = "'" + key + "' alanına değer yazılamadı.";
                logger.error(errorMessage, e);
                Allure.step(errorMessage, Status.FAILED);
                Allure.addAttachment("Hata Detayları", "text/plain", e.getMessage(), "UTF-8");
                throw e; // Hata yeniden fırlatılmalı ki test başarısız olsun
            }
        });

}

    @When("^'(.*)' değişkenindeki değer '(.*)' alanına yazılır$")
    public void writeVariableToElement(String variableName, String key) {
        Allure.step("'" + variableName + "' değişkenindeki değeri '" + key + "' alanına yazma işlemi başlatılıyor.", Status.PASSED);
        if (!elements.containsKey(key)) {
            String warningMessage = "'" + key + "' alanı JSON dosyasında bulunamadı. İşlem atlandı.";
            logger.warn(warningMessage);
            Allure.step(warningMessage, Status.SKIPPED);
            return;
        }
        try {
            String value = (String) ScenarioContext.getContext(variableName);
            WebElement element = locatorUtils.findElement(elements.get(key));
            element.clear();
            element.sendKeys(value);
            String successMessage = "'" + variableName + "' değişkenindeki değer '" + key + "' alanına başarıyla yazıldı.";
            Allure.step(successMessage, Status.PASSED);
            logger.info(successMessage);
        } catch (Exception e) {
            String errorMessage = "'" + key + "' alanına değer yazılamadı.";
            logger.error(errorMessage, e);
            Allure.step(errorMessage, Status.FAILED);
            Allure.addAttachment("Hata Detayları", e.getMessage());
        }
    }
    @When("^'(.*)' listesinden '(.*)' sırasındaki değer seçilir$")
    public void selectValueFromListByIndex(String key, int index) {
        Allure.step("Kontrol ediliyor: '" + key + "' listesinden " + index + ". sıradaki değer seçiliyor.", () -> {
            if (!elements.containsKey(key)) {
                String errorMessage = "'" + key + "' JSON dosyasında bulunamadı.";
                logger.error(errorMessage);
                Allure.step(errorMessage, Status.FAILED);
                throw new IllegalArgumentException(errorMessage);
            }

            try {
                WebElement element = locatorUtils.findElement(elements.get(key));

                // Eğer element bir select dropdown ise
                if (element.getTagName().equals("select")) {
                    List<WebElement> options = element.findElements(By.tagName("option"));

                    if (options.size() > index) {
                        WebElement option = options.get(index);
                        option.click();
                        String successMessage = "'" + key + "' listesinden '" + index + "' sırasındaki değer '" + option.getText() + "' başarıyla seçildi.";
                        logger.info(successMessage);
                        Allure.step(successMessage, Status.PASSED);
                    } else {
                        String failMessage = "'" + key + "' listesinde " + index + ". sırada bir seçenek bulunamadı.";
                        logger.error(failMessage);
                        Allure.step(failMessage, Status.FAILED);
                        throw new IndexOutOfBoundsException(failMessage);  // Seçenek bulunamazsa hata fırlatılır
                    }
                } else {
                    String errorMessage = "'" + key + "' ögesi bir dropdown değil!";
                    logger.error(errorMessage);
                    Allure.step(errorMessage, Status.FAILED);
                    throw new IllegalArgumentException(errorMessage);
                }

            } catch (Exception e) {
                String errorMessage = "'" + key + "' ögesine ulaşılamadı.";
                logger.error(errorMessage, e);
                Allure.step(errorMessage, Status.FAILED);
                Allure.addAttachment("Hata Detayları", "text/plain", e.getMessage(), "UTF-8");
            }
        });
}
    @When("^'(.*)' uzunluğunda rastgele bir sayı üretilir ve '(.*)' değişkenine kaydedilir$")
    public void generateRandomNumberAndSaveToVariable(int length, String variableName) {
        Allure.step("Rastgele " + length + " uzunluğunda bir sayı üretiliyor ve '" + variableName + "' değişkenine kaydediliyor.", () -> {
            if (length <= 0) {
                String errorMessage = "Geçersiz uzunluk: " + length;
                logger.error(errorMessage);
                Allure.step(errorMessage, Status.FAILED);
                throw new IllegalArgumentException(errorMessage);
            }

            try {
                Random random = new Random();
                StringBuilder randomNumber = new StringBuilder();

                // Sayıyı istenen uzunlukta oluştur
                for (int i = 0; i < length; i++) {
                    randomNumber.append(random.nextInt(10)); // 0-9 arası bir sayı ekle
                }

                String generatedNumber = randomNumber.toString();
                // Sayıyı ScenarioContext'e kaydediyoruz
                ScenarioContext.setContext(variableName, generatedNumber);

                String successMessage = "Rastgele üretilen sayı: " + generatedNumber + " ve '" + variableName + "' değişkenine kaydedildi.";
                logger.info(successMessage);
                Allure.step(successMessage, Status.PASSED);

            } catch (Exception e) {
                String errorMessage = "Rastgele sayı üretilemedi.";
                logger.error(errorMessage, e);
                Allure.step(errorMessage, Status.FAILED);
            }
        });
    }
}
