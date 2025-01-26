package utils;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.TestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.UUID;

public class AllureReportManager {

    private static final Logger logger = LoggerFactory.getLogger(AllureReportManager.class);
    private static final String ALLURE_RESULTS_PATH = System.getProperty("user.dir") + "/target/allure-results";

    public static void initReport() {
        createReportsFolder();
        logger.info("Allure raporları için klasör oluşturma işlemi tamamlandı.");
    }

    private static void createReportsFolder() {
        File reportsDir = new File(ALLURE_RESULTS_PATH);
        if (!reportsDir.exists() && reportsDir.mkdirs()) {
            logger.info("allure-results klasörü başarıyla oluşturuldu.");
        } else if (reportsDir.exists()) {
            logger.info("allure-results klasörü zaten mevcut.");
        } else {
            logger.error("allure-results klasörü oluşturulamadı.");
            throw new RuntimeException("allure-results klasörü oluşturulamadı.");
        }
    }

    public static void startTest(String name, String testName) {
        // Test UUID'si oluşturuluyor
        String uuid = UUID.randomUUID().toString();

        // TestResult nesnesi oluşturuluyor
        TestResult testResult = new TestResult()
                .setUuid(uuid)
                .setName(testName)
                .setDescription("Test started: " + testName)
                .setStatusDetails(new StatusDetails());

        // Test planlanıyor ve başlatılıyor
        Allure.getLifecycle().scheduleTestCase(testResult);
        Allure.getLifecycle().startTestCase(uuid);

        // Log bilgisi
        logger.info("Test başlatıldı: {}", testName);
    }


    public static void stopTest(String uuid, boolean isPassed) {
        if (isPassed) {
            Allure.step("Test passed successfully.");
        } else {
            Allure.step("Test failed.");
        }
        Allure.getLifecycle().stopTestCase(uuid);
        Allure.getLifecycle().writeTestCase(uuid);
    }



    public static void step(String stepName, Runnable stepAction) {
        Allure.step(stepName, () -> {
            try {
                stepAction.run();
                logger.info("Adım tamamlandı: {}", stepName);
            } catch (Exception e) {
                logger.error("Adım başarısız: {}", stepName, e);
                Allure.step("Adım başarısız: " + stepName, Status.FAILED);
                throw e;
            }
        });
    }
}
