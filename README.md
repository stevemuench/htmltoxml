# HTMLTOXML
Simple Java class that uses JTidy to turn HTML into XHTML

Provided as is as sample code only.

Example PL/SQL function spec to use this from PL/SQL:
```
function to_xml(
    input_html clob) 
    return     clob
is language java name 
'com.stevemuench.utils.HTMLToXHTML.toXML(oracle.sql.CLOB) return oracle.sql.CLOB';
```
