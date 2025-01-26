Test Otomasyonu Projesi

Bu proje, Cucumber, Allure, Selenium WebDriver gibi araçları kullanarak bir web uygulamasının otomatik testlerini gerçekleştirmek için yazılmıştır. Bu testler, kullanıcı girişi, hesap yönetimi, para transferi ve diğer işlemleri kapsar. Her testin sonucu Allure raporu ile görselleştirilir.

Proje İçeriği
Proje Hedefi
Projenin amacı, web uygulamasının testlerini otomatikleştirerek kullanıcı giriş işlemi, hesap düzenleme, para ekleme ve transfer gibi temel işlemleri test etmektir. Bu işlemler için senaryolar yazılacaktır ve her adım için test sonuçları Allure dashboard'ına raporlanacaktır.

Gerekli Araçlar
Selenium WebDriver: Web tarayıcısını kontrol etmek için kullanılır.
Cucumber: BDD (Behavior Driven Development) ile test senaryolarının yazılmasını sağlar.
Allure Report: Testlerin görsel raporlanmasını sağlar.
WebDriverManager: Tarayıcı sürücülerinin yönetimi için kullanılır.
Maven: Bağımlılık yönetimi ve proje yapılandırması için kullanılır.


Proje Hiyerarşisi


src/

├── main/

│   ├── java/

│   │   ├── base/

│   │   │   ├── BaseDriver.java

│   │   ├── definitions/

│   │   │   ├── HookDefinitions.java

│   │   │   ├── InputDefinitions.java

│   │   │   ├── ControlDefinitions.java

│   ├── resources/

│   │   ├── features/

│   │   │   ├── login.feature

│   │   │   ├── account.feature

│   │   ├── config.properties

│   ├── test/

│   │   ├── java/

│   │   │   ├── StepDefinitions.java

│   │   ├── resources/

│   │   │   ├── allure-results/

│   │   │   ├── allure-report/

│   ├── target/

├── pom.xml

BaseDriver.java: WebDriver'ın oluşturulması ve yönetilmesinden sorumlu sınıf.

HookDefinitions.java: Senaryo başlatma ve sonlandırma işlemleri ile ilgili hook'lar.

InputDefinitions.java: Kullanıcı adı, şifre gibi bilgileri input olarak alacak tanımlamalar.

ControlDefinitions.java: Element varlığı, text kontrolü gibi doğrulama işlemleri.

login.feature: Cucumber'ın feature dosyası, giriş işlemi için test senaryoları.

config.properties: Çevre ve proje yapılandırma ayarları.

allure-results/: Testlerin raporlarının oluşturulacağı klasör.

allure-report/: Allure raporunun html çıktısını alacağınız klasör.

Mantık

Proje, Cucumber'ın BDD (Behavior Driven Development) modelini kullanarak test senaryolarını yazmakta ve Selenium WebDriver ile bu senaryoları uygulamaktadır. Testlerin her bir adımı, Allure raporu için görsel olarak işlenir.

Test Senaryoları Akışı:
Kullanıcı Girişi:

login.feature dosyasında yer alan senaryolarla, kullanıcı adı ve şifre bilgileri alınıp giriş yapılır.
Giriş işlemi doğrulandıktan sonra, kullanıcıya ait özellikler test edilir.
Hesap İşlemleri:

Hesap ismi düzenleme, para ekleme ve transfer işlemleri yapılır.
Her adımın sonucunda başarı veya hata durumu Allure dashboard'ına yansıtılır.
Testlerin Yürütülmesi ve Raporlanması:

Testler tamamlandıktan sonra, tüm adımların detaylı sonuçları Allure raporunda görselleştirilir.
Başarı ve hata adımları detaylı bir şekilde izlenebilir.
Nasıl Çalıştırılır
1. Bağımlılıkları İndirin
   Projeyi çalıştırmak için Maven bağımlılıklarını indirin:

bash
Copy
Edit
mvn clean install
2. Testi Çalıştırın
   Testleri çalıştırmak için aşağıdaki komutu kullanabilirsiniz:

bash
Copy
Edit
mvn test
Testler, src/test/resources/features altında belirtilen senaryolara göre yürütülecektir.

3. Allure Raporunu Görüntüleyin
   Testler çalıştırıldıktan sonra, Allure raporları oluşturulacaktır. Rapora ulaşmak için aşağıdaki komutu kullanabilirsiniz:

bash
Copy
Edit
allure serve target/allure-results
Bu komut, raporu tarayıcıda görüntüleyecektir.

Örnek Feature Dosyası (login.feature)
gherkin
Copy
Edit
Feature: Kullanıcı Giriş Fonksiyonu

Scenario: Başarılı Giriş

Given Kullanıcı adı "testuser" ve şifre "password123" girilir

When "Giriş Yap" butonuna tıklanır

Then Kullanıcı başarılı bir şekilde anasayfaya yönlendirilir


Scenario: Başarısız Giriş

Given Kullanıcı adı "testuser" ve şifre "yanlispassword" girilir

When "Giriş Yap" butonuna tıklanır

Then Hata mesajı gösterilir

Örnek Step Definition (InputDefinitions.java)


@When("^'(.*)' değeri '(.*)' alanına yazılır$")

public void writeValueToElement(String value, String key) {

WebElement element = locatorUtils.findElement(elements.get(key));

element.clear();

element.sendKeys(value);

Allure.step("'" + value + "' değeri '" + key + "' alanına başarıyla yazıldı.");

}

Test Senaryoları

Kullanıcı Girişi Senaryoları:

Başarılı Giriş: Kullanıcı adı ve şifre doğru girildiğinde, sistemin anasayfaya yönlendirdiği doğrulanacaktır.

Başarısız Giriş: Yanlış kullanıcı adı veya şifre girildiğinde, sistemin hata mesajı verdiği doğrulanacaktır.

Hesap Yönetimi Senaryoları:

Hesap Adı Değiştirme: Hesap adı boş veya sadece numara olduğunda hata alması sağlanacaktır.

Para Eklemek ve Transfer Etmek: Hesaplara para ekleme ve bir hesaptan diğerine para transferi işlemi test edilecektir.
