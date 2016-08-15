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
* deploy in an Apache Tomcat instance (e.g. rename war file, if wanted, and copy to $TOMCAT_HOME/webapps directory)

--
Test/help page:
--
* [tomcat URL]/[webapp directory]/index.html

--
API endpoint:
--
* [tomcat URL]/[webapp directory]/validate.html

* POST request following parameters
  * sch: schema url (e.g., http://cwrc.ca/schemas/tei_all.rng)
  * type: type of schema (e.g, RNG_XML - Relax-NG)
  * content: {XML document contents}

* note: without the proper API call parameters this url will return a
"request sent by the client was syntactically incorrect" HTTP 400
error
