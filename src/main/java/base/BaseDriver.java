package base;

import definitions.HookDefinitions;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AllureReportManager;

import java.time.Duration;
import java.util.UUID;

public class BaseDriver {
    private static WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(BaseDriver.class);
    private static final ThreadLocal<String> testUuid = new ThreadLocal<>();

    @Before
    public void setUp(Scenario scenario) {
        logger.info("Senaryo Başlatıldı: {}", scenario.getName());

        // UUID oluştur ve Allure raporlamasını başlat
        String uuid = UUID.randomUUID().toString();
        testUuid.set(uuid);
        AllureReportManager.startTest(scenario.getName(), uuid);

        getDriver();
    }

    public static WebDriver getDriver() {
        if (driver == null) {
            String os = System.getProperty("os.name").toLowerCase();
            String browser = ConfigReader.getProperty("browser").toLowerCase();
            String baseUrl = ConfigReader.getProperty("baseUrl");
            int timeout = Integer.parseInt(ConfigReader.getProperty("page.load.timeout"));

            logger.info("İşletim Sistemi: {}", os);
            logger.info("Seçilen Tarayıcı: {}", browser);

            switch (browser) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--remote-allow-origins=*");
                    chromeOptions.addArguments("--no-sandbox");
                    driver = new ChromeDriver(chromeOptions);
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;
                case "edge":
                    if (os.contains("win")) {
                        WebDriverManager.edgedriver().setup();
                        EdgeOptions edgeOptions = new EdgeOptions();
                        driver = new EdgeDriver(edgeOptions);
                    } else {
                        throw new UnsupportedOperationException("Edge sadece Windows işletim sistemini destekler.");
                    }
                    break;
                case "safari":
                    if (os.contains("mac")) {
                        WebDriverManager.safaridriver().setup();
                        driver = new SafariDriver();
                    } else {
                        throw new UnsupportedOperationException("Safari sadece Mac işletim sistemini destekler.");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Geçersiz tarayıcı seçimi: " + browser);
            }
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
            driver.manage().window().maximize();

            if (baseUrl != null && !baseUrl.isEmpty()) {
                driver.get(baseUrl);
                logger.info("Tarayıcı {} adresine yönlendirildi.", baseUrl);
            } else {
                logger.warn("Adres belirtilmedi. Boş sayfa açıldı.");
            }
        }

        return driver;
    }
    @AfterStep
    public void addStepDelay() {
        int delay = ConfigReader.getStepDelay();
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Delay interrupted: " + e.getMessage());
            }
        }
    }
    @After
    public void tearDown(Scenario scenario) {
        String uuid = HookDefinitions.getCurrentTestUuid(scenario);  // Test UUID'yi al
        HookDefinitions hookDefinitions = new HookDefinitions();  // Hook sınıfı üzerinden test sonlandırma işlemi yapılacak
        hookDefinitions.tearDown(scenario);  // Scenario için ilgili cleanup işlemi yapılır

        AllureLifecycle lifecycle = Allure.getLifecycle();

        if (uuid != null) {
            Allure.step("Senaryo tamamlandı: " + scenario.getName());  // Senaryo başarıyla tamamlandı

            // Durum kontrolü: Eğer senaryo başarısız olduysa FAILED, başarılıysa PASSED olarak ayarlanır.
            if (scenario.isFailed()) {
                lifecycle.updateTestCase(test -> test.setStatus(Status.FAILED));
            } else {
                lifecycle.updateTestCase(test -> test.setStatus(Status.PASSED));
            }
            AllureReportManager.stopTest(uuid, scenario.isFailed());  // Testi sonlandır ve durumunu kaydet
        } else {
            logger.error("Test UUID bulunamadı!");  // UUID bulunamadığında hata logları
        }

        if (driver != null) {
            driver.quit();  // Tarayıcıyı kapat
            driver = null;
            logger.info("Tarayıcı başarıyla kapatıldı.");
        }
    }
}