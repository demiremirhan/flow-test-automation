Feature: login

  Background:
    * 'USERNAME' ögesine tıklanır.
    * '****' değeri 'USERNAME' alanına yazılır.
    * 'PASSWORD' ögesine tıklanır.
    * '****' değeri yazılır.
    * 'LOGIN BUTONU' ögesine tıklanır.
    * 'OPEN MONEY TRANSFER' ögesine tıklanır.

  @DashboardKontrol
  Scenario: Dashboard Kontrolleri
    * 'MY ACCOUNT TITLE' ögesinin varlığı kontrol edilir
    * 'ACCOUNT NAME DASHBOARD' ögesinin text'inin dolu olup olmadığı kontrol edilir
    * 'ACCOUNT TYPE DASHBOARD' ögesinin text'inin dolu olup olmadığı kontrol edilir
    * 'CREATION TIME DASHBOARD' ögesinin text'inin dolu olup olmadığı kontrol edilir
    * 'AMOUNT DASHBOARD' ögesinin text'inin dolu olup olmadığı kontrol edilir
    * 'TRANSACTIONS FIRST LINE' ögesinin text'inin dolu olup olmadığı kontrol edilir