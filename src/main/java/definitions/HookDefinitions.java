package definitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.model.TestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HookDefinitions {
    private static final Logger logger = LoggerFactory.getLogger(HookDefinitions.class);
    private static final Map<String, String> scenarioUuidMap = new HashMap<>();
    private final AllureLifecycle lifecycle = Allure.getLifecycle();

    public static Scenario scenario; // Scenario kaydı korunuyor

    @Before
    public void setUp(Scenario scenario) {
        HookDefinitions.scenario = scenario; // Senaryoyu kaydet
        String uuid = UUID.randomUUID().toString();
        scenarioUuidMap.put(scenario.getId(), uuid); // UUID'yi haritaya ekle

        TestResult testResult = new TestResult();
        testResult.setUuid(uuid);
        testResult.setName(scenario.getName());
        lifecycle.scheduleTestCase(testResult);
        lifecycle.startTestCase(uuid);

        logger.info("Test başladı: {} (UUID: {})", scenario.getName(), uuid);
    }


    public void tearDown(Scenario scenario) {
        String uuid = scenarioUuidMap.get(scenario.getId()); // UUID'yi haritadan al
        if (uuid == null) {
            logger.error("Test UUID bulunamadı! Senaryo ID: {}", scenario.getId());
            throw new IllegalStateException("Test UUID bulunamadı! Senaryo ID: " + scenario.getId());
        }

        if (scenario.isFailed()) {
            Allure.step("Senaryo başarısız oldu: " + scenario.getName());
        }

        lifecycle.stopTestCase(uuid);
        lifecycle.writeTestCase(uuid);
        logger.info("Test tamamlandı: {} (UUID: {})", scenario.getName(), uuid);

        // Haritadan UUID'yi kaldır
        scenarioUuidMap.remove(scenario.getId());
    }

    public static String getCurrentTestUuid(Scenario scenario) {
        String uuid = scenarioUuidMap.get(scenario.getId());
        if (uuid == null) {
            throw new IllegalStateException("Test UUID bulunamadı! Senaryo ID: " + scenario.getId());
        }
        return uuid;
    }
    public static Boolean isStepPass(StepResult stepResult) {
        AllureLifecycle lifecycle = Allure.getLifecycle();
        String uuid = scenario.getId();
        if (stepResult == null) {
            lifecycle.updateTestCase(uuid, test -> test.setStatus(Status.FAILED));
            return false;
        }else{
        lifecycle.updateTestCase(uuid, test -> test.setStatus(Status.PASSED));
        return true;
    }
    }
}
