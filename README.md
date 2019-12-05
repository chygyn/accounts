Сервис предоставляет возможность снятия средств со счета, пополнения, перевода между счетами. 
В ин-мемори БД H2 созданы два аккаунта с ид 1,2 (баланс в копейках).
Account(id=1, name = "Vasya", balance = 10000), Account(id=2, name = "Iiiigor", balance = 5000)
Сваггер доступен дефолтной ссылке по http://localhost:8080/swagger-ui.html#/.

PUT /accounts/replenishment/{accountId}/{amount} - пополнение баланса в копейках по ид счета

PUT /accounts/transfer - перевод между двумя счетами. Тело запроса:
{
  "senderId": 0,
  "receiverId": 0,
  "amount": 0
}

PUT /accounts/withdrawal/{accountId}/{amount} - снятие со счета в копейках по ид счета

Таска bootJar упаковывает в исполняемый jar.
