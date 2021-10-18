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



### Testing Scenarios

1. Create a message from _`« Jérémie Durand »`_ with the following content: _`« Hello, I have an issue with my new phone »`_
2. Create a client case, with the client name _`« Jérémie Durand »`_, and having the previously created message in its messages list.
3. Creation of a message from _`« Sonia Valentin »`_, with the following content: _`« I am Sonia, and I will do my best to help you. What is your phone brand and model? »`_ This message will be linked the previously created client case.
4. Modification of the client case adding the client reference _`« KA-18B6 »`_. This will validate the client case modification feature.
5. Fetching of all client cases.
The result will only contains one client case, the one we created before.






