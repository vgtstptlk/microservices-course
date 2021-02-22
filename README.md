## Курс по микросервисным архитектурам

##### Преподаватель: Антон

Основнаые темы занятия 1 (22.02.20) SOAP и WSDL, REST

**Дисклеймер**: Теорию пишу сам по темам занятий, так что за правильность не ручаюсь, используйте информацию осюда на свой страх и риск. За ваши проваленные собеседования я не отвечаю :)

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