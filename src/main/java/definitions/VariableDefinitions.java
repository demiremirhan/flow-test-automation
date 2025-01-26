package definitions;

import base.BaseDriver;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonReader;
import utils.LocatorUtils;
import utils.ScenarioContext;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;

public class VariableDefinitions {
    private WebDriver driver = BaseDriver.getDriver();
    private LocatorUtils locatorUtils = new LocatorUtils(driver);
    private Map<String, Map<String, String>> elements;
    private static final Logger logger = LoggerFactory.getLogger(VariableDefinitions.class);

    public VariableDefinitions() {
        String featureFileName = HookDefinitions.scenario.getUri().toString();
        featureFileName = featureFileName.substring(featureFileName.lastIndexOf("/") + 1);
        logger.info("Feature dosyası yüklendi: {}", featureFileName);
        this.elements = JsonReader.loadElements(featureFileName);
    }

    @When("^'(.*)' ögesinin içindeki değer '(.*)' değişkenine kaydedilir$")
    public void saveElementTextToVariable(String key, String variableName) {
        Allure.step("'" + key + "' ögesinin içindeki değeri '" + variableName + "' değişkenine kaydetme işlemi başlatılıyor.", Status.PASSED);

        if (!elements.containsKey(key)) {
            String warningMessage = "'" + key + "' ögesi JSON dosyasında bulunamadı. İşlem atlandı.";
            logger.warn(warningMessage);
            Allure.step(warningMessage, Status.SKIPPED);
            return;
        }

        try {
            WebElement element = locatorUtils.findElement(elements.get(key));
            String value = element.getText(); // Ögenin içindeki metni al
            ScenarioContext.setContext(variableName, value); // Değeri değişkene kaydet

            String successMessage = "'" + key + "' ögesinin değeri '" + variableName + "' değişkenine kaydedildi: " + value;
            Allure.step(successMessage, Status.PASSED);
            logger.info(successMessage);

            // Ek bilgi olarak Allure'a değer ekleme
           // Allure.addAttachment(variableName + " Değeri", "text/plain", value, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            String errorMessage = "'" + key + "' ögesinin içindeki metin kaydedilemedi.";
            logger.error(errorMessage, e);
            Allure.step(errorMessage, Status.FAILED);
            Allure.addAttachment("Hata Detayları", e.getMessage());
        }
    }
    @When("^'(.*)' değişkenine '(.*)' değeri eklenir$")
    public void addValueToVariable(String variableName, String valueToAdd) {
        String currentValue = (String) ScenarioContext.getContext(variableName);
        if (currentValue == null) {
            currentValue = ""; // Eğer değişken daha önce set edilmediyse boş bir değerle başla
        }
        String updatedValue = currentValue + valueToAdd;
        ScenarioContext.setContext(variableName, updatedValue);

        String successMessage = "'" + valueToAdd + "' değeri '" + variableName + "' değişkenine başarıyla eklendi.";
        logger.info(successMessage);
        Allure.step(successMessage, Status.PASSED);
    }
    @When("^'(.*)' ögesinin '(.*)' özelliği alınır ve '(.*)' değişkenine kaydedilir$")
    public void getElementAttributeAndSaveToVariable(String key, String attributeName, String variableName) {
        try {
            if (!elements.containsKey(key)) {
                String errorMessage = "'" + key + "' ögesi JSON dosyasında bulunamadı.";
                logger.error(errorMessage);
                Allure.step(errorMessage, Status.FAILED);
                throw new IllegalArgumentException(errorMessage);
            }

            WebElement element = locatorUtils.findElement(elements.get(key));
            String attributeValue = element.getAttribute(attributeName); // Özelliği alıyoruz

            // Değeri ScenarioContext'e kaydediyoruz
            ScenarioContext.setContext(variableName, attributeValue);

            String successMessage = "'" + key + "' ögesinin '" + attributeName + "' özelliği alındı ve '" + variableName + "' değişkenine kaydedildi: " + attributeValue;
            logger.info(successMessage);
            Allure.step(successMessage, Status.PASSED);

        } catch (Exception e) {
            String errorMessage = "'" + key + "' ögesinin '" + attributeName + "' özelliği alınamadı.";
            logger.error(errorMessage, e);
            Allure.step(errorMessage, Status.FAILED);
            Allure.addAttachment("Hata Detayları", "text/plain", e.getMessage(), "UTF-8");
        }
    }
    @When("^'(.*)' değişkenindeki değer '(.*)' değişkenindeki değerle karşılaştırılır$")
    public void compareVariablesEquality(String variableName1, String variableName2) {
        try {
            String value1 = (String) ScenarioContext.getContext(variableName1);
            String value2 = (String) ScenarioContext.getContext(variableName2);

            if (value1.equals(value2)) {
                String successMessage = "'" + variableName1 + "' ve '" + variableName2 + "' değişkenlerinin değerleri eşittir: " + value1;
                logger.info(successMessage);
                Allure.step(successMessage, Status.PASSED);
            } else {
                String failMessage = "'" + variableName1 + "' ve '" + variableName2 + "' değişkenlerinin değerleri eşit değil: " + value1 + " != " + value2;
                logger.error(failMessage);
                Allure.step(failMessage, Status.FAILED);
            }
        } catch (Exception e) {
            String errorMessage = "Değişkenlerin karşılaştırılması sırasında hata oluştu.";
            logger.error(errorMessage, e);
            Allure.step(errorMessage, Status.FAILED);
            Allure.addAttachment("Hata Detayları", "text/plain", e.getMessage(), "UTF-8");
        }
    }
    @When("^'(.*)' uzunluğunda rastgele bir kelime üretilir ve '(.*)' değişkenine kaydedilir$")
    public void generateRandomWordWithLengthAndSaveToVariable(int length, String variableName) {
        Allure.step("Rastgele " + length + " karakter uzunluğunda bir kelime üretiliyor ve '" + variableName + "' değişkenine kaydediliyor.", () -> {
            if (length <= 0) {
                String errorMessage = "Geçersiz uzunluk: " + length;
                logger.error(errorMessage);
                Allure.step(errorMessage, Status.FAILED);
                throw new IllegalArgumentException(errorMessage);
            }

            try {
                StringBuilder randomWord = new StringBuilder();
                Random random = new Random();

                // Rastgele harfleri ekleyerek kelimeyi oluştur
                for (int i = 0; i < length; i++) {
                    char randomChar = (char) (random.nextInt(26) + 'a');  // 'a' ile 'z' arası bir harf seç
                    randomWord.append(randomChar);
                }

                String generatedWord = randomWord.toString();

                // Kelimeyi ScenarioContext'e kaydediyoruz
                ScenarioContext.setContext(variableName, generatedWord);

                String successMessage = "Rastgele üretilen kelime: '" + generatedWord + "' ve '" + variableName + "' değişkenine kaydedildi.";
                logger.info(successMessage);
                Allure.step(successMessage, Status.PASSED);

            } catch (Exception e) {
                String errorMessage = "Rastgele kelime üretilemedi.";
                logger.error(errorMessage, e);
                Allure.step(errorMessage, Status.FAILED);
            }
        });
    }


}
