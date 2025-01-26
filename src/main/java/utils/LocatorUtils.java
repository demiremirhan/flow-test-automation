package utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.NoSuchElementException;

public class LocatorUtils {
    private WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(LocatorUtils.class);

    public LocatorUtils(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement findElement(Map<String, String> locator) {
        try {
            WebElement element = null;

            if (locator.containsKey("XPATH")) {
                logger.info("Xpath ile element bulunuyor: {}", locator.get("XPATH"));
                Allure.step("Xpath ile element bulunuyor: " + locator.get("XPATH"));
                element = driver.findElement(By.xpath(locator.get("XPATH")));
            } else if (locator.containsKey("CSS")) {
                logger.info("CssSelector ile element bulunuyor: {}", locator.get("CSS"));
                Allure.step("CssSelector ile element bulunuyor: " + locator.get("CSS"));
                element = driver.findElement(By.cssSelector(locator.get("CSS")));
            } else if (locator.containsKey("ID")) {
                logger.info("ID ile element bulunuyor: {}", locator.get("ID"));
                Allure.step("ID ile element bulunuyor: " + locator.get("ID"));
                element = driver.findElement(By.id(locator.get("ID")));
            } else if (locator.containsKey("CLASS")) {
                logger.info("CLASS ile element bulunuyor: {}", locator.get("CLASS"));
                Allure.step("CLASS ile element bulunuyor: " + locator.get("CLASS"));
                element = driver.findElement(By.className(locator.get("CLASS")));
            } else {
                String errorMessage = "Geçerli bir locator bulunamadı: " + locator;
                logger.error(errorMessage);
                Allure.step(errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }

            Allure.step("Element başarıyla bulundu: " + locator.toString());
            return element;

        } catch (NoSuchElementException e) {
            String errorMessage = "Element bulunamadı. Locator: " + locator;
            logger.error(errorMessage, e);
            Allure.step(errorMessage);
            Allure.addAttachment("Hata Detayları", e.getMessage());
            throw e;
        }
    }
}
