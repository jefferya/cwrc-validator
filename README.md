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

--
Note: Compatibility: if the .war file is exploded in Tomcat 1.6 with java 1.8.91 or higher you may receive the following error: "org.apache.jasper.JasperException: Unable to compile class for JSP: An error occurred at line: 1 in the generated java file. The type java.io.ObjectInputStream cannot be resolved. It is indirectly referenced from required .class files"

--

Changelog

* 2021-04-23: update guava
