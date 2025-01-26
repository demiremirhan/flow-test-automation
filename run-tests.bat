@echo off
REM Testleri çalıştır
mvn test

REM Allure raporlarını oluştur
allure generate target/allure-results --clean -o target/allure-report

REM Raporları servis et
allure open target/allure-report