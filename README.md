# vote-api 
Application web for vote 

## GITHUB: https://github.com/joaopaulopereira/vote-api

Rest api for creating and customizing polls and questionnaires, with a configurable time that allows "Sim" and "Não" and CPF value as answers and generates the result

An additional external CPF validation was requested at the time of adding a vote.


The API documentation can be found at:
https://new-vote-app.herokuapp.com/vote-api/swagger-ui.html#/

### Project architecture:
* Spring boot for Java 11 API
* Postgresql as database
* Heroku 
* Swagger - API documentation
* JMeter - Performance test
* Junit - Unit tests

The challenge is to develop an application that supports the volume of hundreds of thousands of simultaneous requests and remains stable. The solution discussed involves the asynchronous processing of polls and their votes, so that the number of server threads DOES NOT SCALE with the increase in requests. This is the great differential of the solution.


### Setup
* Java JDK 11 
* Postgres database(set username, password, url and database name in resources/application.properties)
* Execute maven build and run java application into target folder with 'java -jar'  

### Results performance tests
* Before init tests:

![alt text](https://i.ibb.co/9g7vZQD/Captura-de-Tela-20220303124309.png)
https://ibb.co/XLq4YcQ
* Init tests

![img](https://i.ibb.co/K5VvHy4/Captura-de-Tela-20220303125454.png)
https://ibb.co/3TSZHh6

* With >100.000 simultaneous requests and 0.00% error
![img](https://i.ibb.co/bLWQP8P/Captura-de-Tela-plasmashell-20220303124610.png)
https://ibb.co/MkPBMTb
