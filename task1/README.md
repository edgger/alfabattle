## Задача 1 - Где же банкомат?
### Условие
Требуется реализовать сервис для получения данных о банкоматах Альфа-Банка.

Сервис должен предоставлять REST API на http://IP:8080.

Файл с описанием API – api.json.

Для получение базовых данных необходима интеграция решения с Альфа-Банк API Сервис банкоматов.

Документация: 
* https://api.alfabank.ru/node/238
* https://api.alfabank.ru/man_cert_rsa

Для доступа к API необходима регистрация и несложная настройка.

Детали: https://api.alfabank.ru/start

### Задачи
#### 1. Получение данных банкомата по deviceId

Запрос: GET http://IP:8080/atms/{deviceId}

Ответ:
- 200 AtmResponse
- 404 ErrorResponse (если банкомат не найден)

Пример: GET http://IP:8080/atms/153463

200
```json
{
  "deviceId": 153463,
  "latitude": "55.6610213",
  "longitude": "37.6309405",
  "city": "Москва",
  "location": "Старокаширское ш., 4, корп. 10",
  "payments": false
}
```

Пример: GET http://IP:8080/atms/1

404

```json
{
  "status": "atm not found"
}
```

Следующая задача доступна после получения 18 баллов.

### 2. Получение ближайшего к указанным координатам банкомата (по-глупому)
По глупому - потому что считаем Землю плоской.

А в Задаче 3 Свободная касса! надо будет по-умному.

Если передан параметр payments=true, ищутся только банкоматы с поддержкой оплаты товаров и услуг (см. Альфа-Банк API Сервис банкоматов ATMServices).

Запрос: GET http://IP:8080/atms/nearest?latitude=string&longitude=string&payments=boolean
Ответ: 200 AtmResponse

Пример: GET http://IP:8080/atms/nearest?latitude=55.66&longitude=37.63
200
```json
{
  "deviceId": 153463,
  "latitude": "55.6610213",
  "longitude": "37.6309405",
  "city": "Москва",
  "location": "Старокаширское ш., 4, корп. 10",
  "payments": false
}
```

Пример: GET http://IP:8080/atms/nearest?latitude=55.66&longitude=37.63&payments=true
200
```json
{
  "deviceId": 210612,
  "latitude": "55.66442",
  "longitude": "37.628051",
  "city": "Москва",
  "location": "Каширское шоссе, д. 14",
  "payments": true
}
```


Следующая задача доступна после получения 36 баллов.

### 3. Получение списка ближайших банкоматов для снятия Альфиков
В каждом банкомате доступно для снятия определенное количество чудо-валюты Альфик.

Нужно получить список ближайших банкоматов, в которых можно суммарно снять указанное число Альфиков.

Запрос: GET http://IP:8080/atms/nearest-with-alfik?latitude=string&longitude=string&alfik=int

Ответ: 200 [AtmResponse]

Узнать число доступных Альфиков для указанного банкомата можно через внешний сервис, предоставляющий WebSocket STOMP интерфейс.
Для запуска:
    • mkdir task1 ; cd task1
    • wget https://raw.githubusercontent.com/evgenyshiryaev/alfa-battle-resources/master/task1/docker-compose.yml
    • docker-compose up -d
Для остановки (в папке task1):
    • docker-compose down
Адрес: ws://IP:8100
Запрос: { “deviceId”: 0 }
Ответ на /topic/alfik: { “deviceId”: 0, “alfik”: 0 }

Пример: GET http://IP:8080/atms/nearest-with-alfik?latitude=55.66&longitude=37.63&alfik=300000

200
```json
[{
  "deviceId": 153463,
  "latitude": "55.6610213",
  "longitude": "37.6309405",
  "city": "Москва",
  "location": "Старокаширское ш., 4, корп. 10",
  "payments": false
}]
```

(т.к. в банкомате 153463 - 383026 Альфиков)

Пример: GET http://IP:8080/atms/nearest-with-alfik?latitude=55.66&longitude=37.63&alfik=400000

200
```json
[{
  "deviceId": 153463,
  "latitude": "55.6610213",
  "longitude": "37.6309405",
  "city": "Москва",
  "location": "Старокаширское ш., 4, корп. 10",
  "payments": false
},
{
  "deviceId": 153465,
  "latitude": "55.6602801",
  "longitude": "37.633823",
  "city": "Москва",
  "location":" Каширское ш., 18",
  "payments": false
}]
```
(Альфиков уже не хватает, нужен следующий банкомат)
