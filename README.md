## Курс по микросервисным архитектурам

##### Преподаватель: Антон

Основнаые темы занятия SOAP и WSDL, REST

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

