# innso tech challenge
Build a REST API (ideally RESTful) with Spring boot which can manage the following items:


**Message**'s properties

* Author’s name;
* Message creation date;
* Message content;
* Channel (The possible values are: `MAIL`, `SMS`, `FACEBOOK` and `TWITTER`);


**Client Case**'s properties

* Client’s name;
* Client creation date;
* Reference;
* Message list;



### Scenario's steps

1. Create a message from _`« Jérémie Durand »`_ with the following content: _`« Hello, I have an issue with my new phone »`_
2. Create a client case, with the client name _`« Jérémie Durand »`_, and having the previously created message in its messages list.
3. Creation of a message from _`« Sonia Valentin »`_, with the following content: _`« I am Sonia, and I will do my best to help you. What is your phone brand and model? »`_ This message will be linked the previously created client case.
4. Modification of the client case adding the client reference _`« KA-18B6 »`_. This will validate the client case modification feature.
5. Fetching of all client cases.
The result will only contains one client case, the one we created before.


## Built with
- Spring boot : project packaging, embedded Tomcat server
- Spring data rest : create RESTful repositories for our entities
- Spring data jpa : manage database persistency
- H2 database : in memory, embedded database
- JUnit 5 : unit/integration tests

## How to package?

Maven 3 must be installed to package the application.
You must open a terminal on the application main folder, and execute the following maven command:

```
mvn package
```

It will build the solution in the folder "target".

To start the application, execute the command:

```
java -jar clientbox-api-0.0.1-SNAPSHOT.jar
```

The application will be starting on port 8080.

### Testing Scenario's steps

The curl commands for the test scenario are:

#step 01
```
curl -X POST -H "Content-Type: application/json" -d '{"authorName":"J\u00e9r\u00e9mie Durand", "content":"Hello, I have an issue with my new phone", "channel":"SMS"}' http://localhost:8080/messages
```

#step 02
```
curl -X POST -H "Content-Type: application/json" -d '{"clientName":"J\u00e9r\u00e9mie Durand"}' http://localhost:8080/messages/1/client
```

#step 03
```
curl -X POST -H "Content-Type: application/json" -d '{"authorName":"Sonia Valentin", "content":"I am Sonia, and I will do my best to help you. What is your phone brand and model?", "channel":"TWITTER"}' http://localhost:8080/messages
```

```
curl -i -X PUT -H "Content-Type:text/uri-list" -d "http://localhost:8080/clientCases/1" http://localhost:8080/messages/2/clientCase
```


#step 04
```
curl -i -X PATCH -H "Content-Type: application/json" -d '{"reference":"KA-18B6"}' http://localhost:8080/clientCases/1
```

#step 05
```
curl http://localhost:8080/clientCases
```

### Unit tests / Integration tests

Also, the scenario has been implemented as an integration test in the class: ClientBoxApiApplicationTests
You can execute the tests with the maven command: mvn test


