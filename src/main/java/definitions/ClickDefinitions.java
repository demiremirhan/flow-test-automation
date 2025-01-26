package definitions;

import base.BaseDriver;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonReader;
import utils.LocatorUtils;

import java.util.Map;

public class ClickDefinitions {
    private WebDriver driver = BaseDriver.getDriver();
    private LocatorUtils locatorUtils = new LocatorUtils(driver);
    private Map<String, Map<String, String>> elements;
    private static final Logger logger = LoggerFactory.getLogger(ClickDefinitions.class);

    public ClickDefinitions() {
        String featureFileName = HookDefinitions.scenario.getUri().toString();
        featureFileName = featureFileName.substring(featureFileName.lastIndexOf("/") + 1);
        logger.info("Feature dosyası yüklendi: {}", featureFileName);
        this.elements = JsonReader.loadElements(featureFileName);
    }

    @When("^'(.*)' ögesine tıklanır.")
    public void clickElement(String key) {
        Allure.step("Obje tıklama işlemi başlatılıyor: " + key, Status.PASSED);

        if (!elements.containsKey(key)) {
            String errorMessage = "' " + key + " ' JSON dosyasında bulunamadı.";
            logger.error(errorMessage);
            Allure.step(errorMessage, Status.FAILED);
            throw new IllegalArgumentException(errorMessage);
        }
        try {
            String passMessage = "' " + key + " ' ögesine tıklanıyor.";
            Allure.step(passMessage, Status.PASSED);
            WebElement element = locatorUtils.findElement(elements.get(key));
            element.click();
            String successMessage = "' " + key + " ' ögesine başarıyla tıklandı.";
            Allure.step(successMessage, Status.PASSED);
            logger.info(successMessage);
        } catch (Exception e) {
            String failMessage = "' " + key + " ' ögesine tıklanamadı.";
            logger.error(failMessage, e);
            Allure.step(failMessage, Status.FAILED);
            throw e;
        }
    }

    @When("^'(.*)' ögesi varsa tıklanır.")
    public void clickElementIfExist(String key) {
        Allure.step("Obje kontrol ve tıklama işlemi başlatılıyor: " + key, Status.PASSED);

        if (!elements.containsKey(key)) {
            String warnMessage = "' " + key + " ' JSON dosyasında bulunamadı. İşlem atlandı.";
            logger.warn(warnMessage);
            Allure.step(warnMessage, Status.SKIPPED);
            return;
        }
        try {
            WebElement element = locatorUtils.findElement(elements.get(key));
            if (element.isDisplayed()) {
                element.click();
                String passMessage = "' " + key + " ' ögesine başarıyla tıklandı.";
                Allure.step(passMessage, Status.PASSED);
                logger.info(passMessage);
            } else {
                String skippedMessage = "' " + key + " ' ögesi görünür değil. İşlem atlandı.";
                logger.warn(skippedMessage);
                Allure.step(skippedMessage, Status.SKIPPED);
            }
        } catch (Exception e) {
            String failMessage = "' " + key + " ' ögesine tıklanamadı.";
            logger.error(failMessage, e);
            Allure.step(failMessage, Status.FAILED);
        }
    }
    @When("^'(.*)' ögesine tıklanabilir hale gelene kadar tıklanır$")
    public void clickElementWhenClickable(String key) {
        Allure.step("Kontrol ediliyor: '" + key + "' ögesine tıklanabilir hale gelene kadar tıklanır.", () -> {
            if (!elements.containsKey(key)) {
                String errorMessage = "'" + key + "' JSON dosyasında bulunamadı.";
                logger.error(errorMessage);
                Allure.step(errorMessage, Status.FAILED);
                throw new IllegalArgumentException(errorMessage);
            }

            try {
                WebElement element = locatorUtils.findElement(elements.get(key));

                // Eğer element görünür fakat tıklanabilir değilse
                Actions actions = new Actions(driver);
                actions.moveToElement(element).click().perform();  // Elementin üzerine gelerek tıklama yapılır

                String successMessage = "'" + key + "' ögesine başarıyla tıklanmıştır.";
                logger.info(successMessage);
                Allure.step(successMessage, Status.PASSED);

            } catch (Exception e) {
                String errorMessage = "'" + key + "' ögesine tıklanamadı.";
                logger.error(errorMessage, e);
                Allure.step(errorMessage, Status.FAILED);
                Allure.addAttachment("Hata Detayları", "text/plain", e.getMessage(), "UTF-8");
            }
        });
    }

}
