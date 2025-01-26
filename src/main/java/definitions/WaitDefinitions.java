package definitions;

import base.BaseDriver;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonReader;
import utils.LocatorUtils;

import java.time.Duration;
import java.util.Map;

public class WaitDefinitions {

    private WebDriver driver = BaseDriver.getDriver();
    private LocatorUtils locatorUtils = new LocatorUtils(driver);
    private Map<String, Map<String, String>> elements;
    private static final Logger logger = LoggerFactory.getLogger(WaitDefinitions.class);

    public WaitDefinitions() {
        String featureFileName = HookDefinitions.scenario.getUri().toString();
        featureFileName = featureFileName.substring(featureFileName.lastIndexOf("/") + 1);
        logger.info("Feature dosyası yüklendi: {}", featureFileName);
        this.elements = JsonReader.loadElements(featureFileName);
    }

    @When("^'(.*)' saniye beklenir.$")
    public void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            String message = seconds + " saniye bekleniyor.";
            Allure.step(message, Status.PASSED);
            logger.info(message);
        } catch (InterruptedException e) {
            String errorMessage = "Bekleme sırasında hata oluştu.";
            logger.error(errorMessage, e);
            Allure.step(errorMessage, Status.FAILED);
            Allure.addAttachment("Hata Detayları", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    @When("^'(.*)' ögesi görünene kadar beklenir.$")
    public void waitUntilVisible(String key) {
        if (!elements.containsKey(key)) {
            String warningMessage = "'" + key + "' ögesi JSON dosyasında bulunamadı. İşlem atlandı.";
            logger.warn(warningMessage);
            Allure.step(warningMessage, Status.SKIPPED);
            return;
        }
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            WebElement element = wait.until(ExpectedConditions.visibilityOf(locatorUtils.findElement(elements.get(key))));
            String successMessage = "'" + key + "' ögesi görünür hale geldi.";
            Allure.step(successMessage, Status.PASSED);
            logger.info(successMessage);
        } catch (TimeoutException e) {
            String errorMessage = "'" + key + "' ögesi bekleme süresince görünür olmadı.";
            logger.error(errorMessage, e);
            Allure.step(errorMessage, Status.FAILED);
            Allure.addAttachment("Hata Detayları", e.getMessage());
        }
    }
}
