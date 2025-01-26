Feature: login
  Background:
  Background:
    * 'USERNAME' ögesine tıklanır.
    * '****' değeri 'USERNAME' alanına yazılır.
    * 'PASSWORD' ögesine tıklanır.
    * '****' değeri yazılır.
    * 'LOGIN BUTONU' ögesine tıklanır.
    * 'OPEN MONEY TRANSFER' ögesine tıklanır.

  @TransferMoney
  Scenario: Para Transfer 100Tl Altı Kontrol Senaryosu
    * 'TRANSFER MONEY BUTTON' ögesine tıklanır.
    * 'SENDER ACCOUNT' ögesine tıklanır.
    * 'SENDER ACCOUNT1' ögesine tıklanır.
    * 'RECIEVER ACCOUNT' ögesine tıklanır.
    * 'RECIEVER ACCOUNT1' ögesine tıklanır.
    * '2' uzunluğunda rastgele bir sayı üretilir ve 'PARA DEGERI' değişkenine kaydedilir
    * 'PARA DEGERI' değişkenindeki değer 'AMOUNT TEXTBOX' alanına yazılır
    * 'SEND BUTTON' ögesine tıklanır.
    * 'PARA DEGERI' değişkenine '.00' değeri eklenir
    * 'LAST TRANSACTION AMOUNT' ögesinin değeri 'PARA DEGERI' değişkenine eşitliği kontrol edilir

  @TransferMoney
  Scenario: Para Transfer 100Tl Üstü Kontrol Senaryosu
    * 'TRANSFER MONEY BUTTON' ögesine tıklanır.
    * 'SENDER ACCOUNT' ögesine tıklanır.
    * 'SENDER ACCOUNT1' ögesine tıklanır.
    * '3' saniye beklenir.
    * 'RECIEVER ACCOUNT' ögesine tıklanır.
    * 'RECIEVER ACCOUNT1' ögesine tıklanır.
    * '3' uzunluğunda rastgele bir sayı üretilir ve 'PARA DEGERI' değişkenine kaydedilir
    * 'PARA DEGERI' değişkenindeki değer 'AMOUNT TEXTBOX' alanına yazılır
    * 'SEND BUTTON' ögesine tıklanır.
    * 'PARA DEGERI' değişkenine '.00' değeri eklenir
    * 'LAST TRANSACTION AMOUNT' ögesinin değeri 'PARA DEGERI' değişkenine eşitliği kontrol edilir