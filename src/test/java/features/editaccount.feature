Feature: editaccount

  Background:
    * 'USERNAME' ögesine tıklanır.
    * '****' değeri 'USERNAME' alanına yazılır.
    * 'PASSWORD' ögesine tıklanır.
    * '****' değeri yazılır.
    * 'LOGIN BUTONU' ögesine tıklanır.
    * 'OPEN MONEY TRANSFER' ögesine tıklanır.

  @HesapAdiDegistir
  Scenario: Hesap Adı Değiştirme Kontrolü
    * 'ACCOUNT NAME DASHBOARD' ögesinin içindeki değer 'EXISTING ACCOUNT NAME' değişkenine kaydedilir
    * 'EDIT ACCOUNT BUTTON' ögesine tıklanır.
    * 'EDIT ACCOUNT EXISTING ACCOUNT NAME' ögesinin 'value' özelliği alınır ve 'EXISTING ACCOUNT NAME POP UP' değişkenine kaydedilir
    * 'EXISTING ACCOUNT NAME' değişkenindeki değer 'EXISTING ACCOUNT NAME POP UP' değişkenindeki değerle karşılaştırılır
    * '6' uzunluğunda rastgele bir kelime üretilir ve 'NEW ACCOUNT NAME VAR' değişkenine kaydedilir
    * 'NEW ACCOUNT NAME VAR' değişkenindeki değer 'EDIT ACCOUNT EXISTING ACCOUNT NAME' alanına yazılır
    * 'UPDATE BUTTON' ögesine tıklanır.
    * 'ACCOUNT NAME DASHBOARD' ögesinin içindeki değer 'NEW ACCOUNT NAME' değişkenine kaydedilir
    * 'NEW ACCOUNT NAME' değişkenindeki değer 'NEW ACCOUNT NAME VAR' değişkenindeki değerle karşılaştırılır
