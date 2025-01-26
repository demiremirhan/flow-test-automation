Feature: addmoney

  Background:
    * 'USERNAME' ögesine tıklanır.
    * '****' değeri 'USERNAME' alanına yazılır.
    * 'PASSWORD' ögesine tıklanır.
    * '****' değeri yazılır.
    * 'LOGIN BUTONU' ögesine tıklanır.
    * 'OPEN MONEY TRANSFER' ögesine tıklanır.

  @ParaEkleme
  Scenario: Para Ekleme 100 Tl altı Senaryo
    * 'ADD MONEY BUTTON' ögesine tıklanır.
    * 'CARD NUMBER TEXTBOX' ögesine tıklanır.
    * '1234123412341234' değeri yazılır.
    * 'CARD HOLDER TEXTBOX' ögesine tıklanabilir hale gelene kadar tıklanır
    * '12345ABC' değeri 'CARD HOLDER TEXTBOX' alanına yazılır.
    * 'EXPIRY DATE TEXTBOX' ögesine tıklanır.
    * '1026' değeri yazılır.
    * 'CVV TEXTBOX' ögesine tıklanır.
    * '110' değeri yazılır.
    * 'AMOUNT TEXTBOX ADD MONEY' ögesine tıklanır.
    * '2' uzunluğunda rastgele bir sayı üretilir ve 'ADD MONEY AMOUNT' değişkenine kaydedilir
    * 'ADD MONEY AMOUNT' değişkenindeki değer 'AMOUNT TEXTBOX ADD MONEY' alanına yazılır
    * 'ADD BUTTON' ögesine tıklanır.
    * 'BACK BUTTON' ögesine tıklanır.
    * 'OPEN MONEY TRANSFER' ögesine tıklanır.
    * '3' saniye beklenir.
    * 'ADD MONEY AMOUNT' değişkenine '.00' değeri eklenir
    * 'LAST TRANSACTION AMOUNT' ögesinin değeri 'ADD MONEY AMOUNT' değişkenine eşitliği kontrol edilir


  @ParaEkleme
  Scenario: Para Ekleme 100 Tl Üstü Senaryo
    * 'ADD MONEY BUTTON' ögesine tıklanır.
    * 'CARD NUMBER TEXTBOX' ögesine tıklanır.
    * '1234123412341234' değeri yazılır.
    * 'CARD HOLDER TEXTBOX' ögesine tıklanır.
    * '12345ABC' değeri 'CARD HOLDER TEXTBOX' alanına yazılır.
    * '12345ABC' değeri yazılır.
    * 'EXPIRY DATE TEXTBOX' ögesine tıklanır.
    * '1026' değeri yazılır.
    * 'CVV TEXTBOX' ögesine tıklanır.
    * '110' değeri yazılır.
    * 'AMOUNT TEXTBOX ADD MONEY' ögesine tıklanır.
    * '3' uzunluğunda rastgele bir sayı üretilir ve 'ADD MONEY AMOUNT' değişkenine kaydedilir
    * 'ADD MONEY AMOUNT' değişkenindeki değer 'AMOUNT TEXTBOX ADD MONEY' alanına yazılır
    * 'ADD BUTTON' ögesine tıklanır.
    * 'BACK BUTTON' ögesine tıklanır.
    * 'OPEN MONEY TRANSFER' ögesine tıklanır.
    * '3' saniye beklenir.
    * 'ADD MONEY AMOUNT' değişkenine '.00' değeri eklenir
    * 'LAST TRANSACTION AMOUNT' ögesinin değeri 'ADD MONEY AMOUNT' değişkenine eşitliği kontrol edilir

