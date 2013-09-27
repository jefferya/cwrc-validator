--
CWRC XML Validator API
--

RESTful API service for XML validation against a schema


--
Source Code:
--

https://github.com/cwrc/cwrc-validator

--
Install
--

* compile: mvn compile
* build distribution: mvn war:war

--
Test/help page:
--
* [tomcat URL]/[webapp directory]/index.html

--
API endpoint:
--
* [tomcat URL]/[webapp directory]/validate.html

* note: without the proper API call parameters this url will return a
"request sent by the client was syntactically incorrect" HTTP 400
error
