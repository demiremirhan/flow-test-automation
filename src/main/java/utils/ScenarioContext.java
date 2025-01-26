package utils;

import io.qameta.allure.Allure;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {
    private static final Map<String, Object> context = new HashMap<>();

    public static void setContext(String key, Object value) {
        context.put(key, value);
        // Allure'a adım ekleme
        Allure.step("Değişken kaydedildi: [" + key + "] = " + value);
    }

    public static Object getContext(String key) {
        Object value = context.get(key);
        // Allure'a adım ekleme
        if (value != null) {
            Allure.step("Değişken alındı: [" + key + "] = " + value);
        } else {
            Allure.step("Değişken bulunamadı: [" + key + "]");
        }
        return value;
    }

    public static boolean contains(String key) {
        boolean exists = context.containsKey(key);
        // Allure'a adım ekleme
        Allure.step("Değişken kontrol edildi: [" + key + "] - Bulundu mu? " + exists);
        return exists;
    }
}
