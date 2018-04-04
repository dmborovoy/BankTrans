# mt940
There are 2 independent modules:
**daemon** and **ui**

# daemon
 is a standalone application that triggers by cron schedule email checking 
defined in property file. Grabs all emails from the last checking, extracts attachments
and parses data and uploads it into DB
Also there is second cron schedule that checks for failed transaction - transactions that
was parsed incorrectly. It can be disabled in property file if needed 

to run:
1. mvn clean install -> you will get uber jar file target/daemon.jar
2. java -jar daemon.jar
3. you can override any property via command line e.g. -DdataSource.user=user1
4. or you can even pass whole property file e.g. -Dext.prop.file=<path to ext file>


#ui
 is a standalone web application that should be deployed into servlet container
(e.g. tomcat). It is just a represntation of data stored in DB. Logically it consists
of several depenent pages:
Login page
List of emails
List of attachments
List of statements
List transactions
Admin page for basic editing users and Instances
All List pages have paging and reach feachuters of filtering. Also they contains links
to dependent entities so you can easily navigate to page with transctions from particular
email   

to run:
1. mvn clean install -> you will get war file target/ui.war
2. deploy it to tomcat