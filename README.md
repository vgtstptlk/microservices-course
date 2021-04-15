## Курс по микросервисным архитектурам

##### Преподаватель: Антон

Основнаые темы занятия 1 (22.02.20) SOAP и WSDL, REST

**Дисклеймер**: Теорию пишу сам по темам занятий, так что за правильность не ручаюсь, используйте информацию осюда на свой страх и риск. За ваши проваленные собеседования я не отвечаю :)

[TOC]



### SOAP

SOAP -- протокол обмена структурированными сообщениями  в распределенной вычислительной среде. 

Протокол используется для обмена сообщениями в формате XML, раньше использовался для вызова удаленных функций. SOAP может работать с множеством разных протоколов, но чаще всего используется поверх **HTTP**. 

#### Структура протокола 

- **Envelope** -- корневой элемент, который определяет сообщение и простарнство имен, использующиеся в документе. 
- **Header** -- содержит атрибуты сообщения, например, информацию о безопасности или сетевой маршрутизации 
- **Body** -- тело сообщения 
- **Fault** -- необязательный элемент, который содержит инфомацию об ошибках, которые произошли. 

Ниже пример xml запроса

```xml
<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
     <getProductDetails xmlns="http://warehouse.example.com/ws">
       <productID>12345</productID>
     </getProductDetails>
   </soap:Body>
</soap:Envelope>
```

Ответ на запрос

```xml
<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
     <getProductDetailsResponse xmlns="http://warehouse.example.com/ws">
       <getProductDetailsResult>
         <productID>12345</productID>
         <productName>Стакан граненый</productName>
         <description>Стакан граненый. 250 мл.</description>
         <price>9.95</price>
         <currency>
             <code>840</code>
             <alpha3>USD</alpha3>
             <sign>$</sign>
             <name>US dollar</name>
             <accuracy>2</accuracy>
         </currency>
         <inStock>true</inStock>
       </getProductDetailsResult>
     </getProductDetailsResponse>
   </soap:Body>
</soap:Envelope>
```

### WSDL

WSDL -- язык описания веб-сервисов и доступа к ним, основанный на xml 

Его основное преимущество в использование, например в отличие от JSON -- строгая типизация. Мы явно указываем значения, которые должны передаться. Таким образом, клиент или сервер не сможет неверно интерпретатировать данные. 

Также еще одним важным свойством SOAP, в частности WSDL является наличие **иерархии**. Представим ситуацию, когда мы передаем get запрос в адресной строке напрямую. Примерно это выглядит так: http://www.server.ru/page.php?name=Vasya&age=20&sex=male&street=Gagarin. Но в таком виде не ясно, что это всё относится к одному объекту, и используя SOAP/WSDL мы четко указываем иерархию. 

Документ WSDL можно разбить на следующие части:

- определение типов данных (types)
- самих элементов данных (message)
- операции, точнее их прототипы, которые могут быть применены к данным (portType)
-  связывание сервисов (binding) -- способ, которым будет доставлено соощение

Пример WSDL

```xml
<message name="getTermRequest">
   <part name="term" type="xs:string"/>
</message>

<message name="getTermResponse">
   <part name="value" type="xs:string"/>
</message>

<portType name="glossaryTerms">
  <operation name="getTerm">
      <input message="getTermRequest"/>
      <output message="getTermResponse"/>
  </operation>
</portType>
```

Какие же есть плюсы и минусы? 

#### Плюсы:

- Описание методов и свойств в большинстве случаев происходит автоматически
- Автоматическая валидация. Если проблема на стороне клиента, то нам не нужно будет это проверять вручную, т.к. за нас это сделает xml валидация 

#### Минусы:

- Большой размер сообщения
- Все методы должны быть атомарными

> Атомарная операция -- операция, которая должна либо полностью выполниться, либо не выполниться вовсе



### REST

REST -- архитектурный стиль построения информационных систем в сети. 

Для веб-служб, построенных по принципам REST используют термин RESTful. 

#### Принципы REST архитектуры:

- Клиент-серверная архитектура 
- Отсутствие состояния
- Кэширование
- Единообразие интерфейса

#### HATEOAS

**HATEOAS** (**H**ypermedia **a**s **t**he **E**ngine **o**f **A**pplication **S**tate) -- ограничения для REST приложений. С помощью HATEOAS REST возвращает не только данные, но и методы, которые можно применить к этим методам. 

Благодаря HATEOAS клиенту заранее не надо знать, как взаимодействовать с приложением или сервером за пределом гипермедиа.

```xml
HTTP/1.1 200 OK
Content-Type: application/xml
Content-Length: ...

<?xml version="1.0"?>
<account>
    <account_number>12345</account_number>
    <balance currency="usd">100.00</balance>
    <link rel="deposit" href="https://bank.example.com/accounts/12345/deposit" />
    <link rel="withdraw" href="https://bank.example.com/accounts/12345/withdraw" /> 
    <link rel="transfer" href="https://bank.example.com/accounts/12345/transfer" />
    <link rel="close" href="https://bank.example.com/accounts/12345/close" />
</account>
```

HATEOAS позволяет реализовать слабую связь, мы можем подменить какие-то методы без изменений клиента.  

#### SOAP Service in Java

@WebMethod

@WebResult 



### SQL (Postgres)

1. Конс данных
2. Атомарность операций

#### Сущности

1. Table 
2. View
3. Procedure
4. Function 
5. Constraint - условие (например, при вставке будет проверять значения на нулл, если нуля, то выйдет исключение)
6. Trigger -- при каком-то условии сработает и выполнит какую-то операцию (например, )
7. Index -- Primary Key

В  table and view можно вставлять, удалять данные. 

Procedure, Function могут возвращать таблицу



#### Языки:

1. DML (INSERT UPDATE SELECT DELETE)
2. DDL (CREATE DROP ALTER)
3. DCL -- раздача прав (CREATE USER, GRANT, REVOKE)
4. TCL -- транзакции (BEGIN SAVE ROLLBACK END COMMIT)
5. Procedure SQL -- язык написания проседур в таблице (с ветвлениями и т. д.)

**OLTP** -- БД для записи 

**OLAP** -- БД для аналитики 

#### Нормальные формы

В нормальных формах все сущности должны лежать отдельности и только один раз

##### Первая нормальная форма

Основным правилом первой формы является необходимость недилимости значения в каждом поле (столбце) строки -- **атоматорность** значений. 

Также должны выполняться дополнительные условия:

- Строки не должны зависеть друг от друга. Т. е. записи не должны влиять друг на друга 
- Аналогично со столбцами 
- Каждая строка должна быть уникальна

##### Вторая нормальная форма 

Условие второй формы -- отстутствие зависимостей неключевых полей от части составного ключа. 

##### Третья нормальная форма 

Третья нормальная форма схожа со второй, только с добавлением условия, что неключевые поля не зависят от других неключевых полей. 

#### Курсоры 

#### Оконные функции

Оконная функция выполняет вычисления для набора строк, сгруппированых по определенному параметру. 

Пример, который показывает среднюю зарплату по отделам. 

```sql
SELECT depname, empno, salary, avg(salary) OVER (PARTITION BY depname)
  FROM empsalary;
```

Результат

```
  depname  | empno | salary |          avg          
-----------+-------+--------+-----------------------
 develop   |    11 |   5200 | 5020.0000000000000000
 develop   |     7 |   4200 | 5020.0000000000000000
 develop   |     9 |   4500 | 5020.0000000000000000
 develop   |     8 |   6000 | 5020.0000000000000000
 develop   |    10 |   5200 | 5020.0000000000000000
 personnel |     5 |   3500 | 3700.0000000000000000
 personnel |     2 |   3900 | 3700.0000000000000000
 sales     |     3 |   4800 | 4866.6666666666666667
 sales     |     1 |   5000 | 4866.6666666666666667
 sales     |     4 |   4800 | 4866.6666666666666667
(10 rows)
```

Также можно отсортировать по убыванию 

```sql
SELECT depname, empno, salary,
       rank() OVER (PARTITION BY depname ORDER BY salary DESC)
FROM empsalary;
```

```
  depname  | empno | salary | rank 
-----------+-------+--------+------
 develop   |     8 |   6000 |    1
 develop   |    10 |   5200 |    2
 develop   |    11 |   5200 |    2
 develop   |     9 |   4500 |    4
 develop   |     7 |   4200 |    5
 personnel |     2 |   3900 |    1
 personnel |     5 |   3500 |    2
 sales     |     1 |   5000 |    1
 sales     |     4 |   4800 |    2
 sales     |     3 |   4800 |    2
(10 rows)
```

Если нужно отфильтровать данные после вычисления оконных функций, то можно использовать вложенные запросы. 

```sql
SELECT depname, empno, salary, enroll_date
FROM
  (SELECT depname, empno, salary, enroll_date,
    rank() OVER (PARTITION BY depname ORDER BY salary DESC, empno) AS pos
   FROM empsalary
  ) AS ss
WHERE pos < 3;
```

Когда в функциях несколько оконных функций лучше выделить их в отдельное предложение WINDOW, и потом ссылаться на него в OVER

```sql
SELECT sum(salary) OVER w, avg(salary) OVER w
  FROM empsalary
  WINDOW w AS (PARTITION BY depname ORDER BY salary DESC);
```

### Транзакции

Транзакции -- важное понятие для всех СУБД. Транзакции сильно связаны с понятием атомарность, т. е. при выполнение запроса выполнится либо всё, либо ничего. 

Представим себе такую ситуацию, где нужно снять с двух счетов деньги и зачислить их на третий счет. 

```sql
UPDATE users SET balance = balance - 100
	WHERE username = 'Max'
UPDATE users SET balance = balance - 100
	WHERE username = 'Nikita'
	
UPDATE users SET balance = balance + 200
	WHERE username = 'Oleg'
```

В таком запросе может случиться, что с первого счёта снимутся деньги, а во втором запросе сработает проверка, и деньги не спишутся. Тогда возникнет ситуация, когда запросы до конца не выполнятся, но с первого счёта деньги уже снимутся. Именно для того, чтобы такого не случилось существуют **транзакции**. 

В Postgres транзакция определяется с помощью команд **BEGIN** и **COMMIT**

```sql
BEGIN; 
UPDATE users SET balance = balance - 100
	WHERE username = 'Max'
COMMIT;
```

Также мы можем создавать **точки сохранения** через **SAVEPOINT <savepoint_name>**. В этом случае при откате через команду **ROLLBACK TO <savepoint_name>** результат возвращается к точке сохранения. 

