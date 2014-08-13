ben-drasin-java-code-sample
===========================

This is a small sample of Java code I've written for anyone who is interested in learning about my coding style/idioms/etc.  The application is a command-line order fulfillment system which accepts orders in JSON format.  For simplicity and to keep the application self-contained I have not included data storage; all data is in-memory.

### Running the application
On starting the application listens on stdin for orders.  The orders are newline-separated JSON objects with the following fields:
* orderId
* updateId
* status (one of NEW, PROCESSING, ENROUTE, RECEIVED, RETURNED, CANCELED)
* amount (only required for new orders) 

### Valid order state transitions

FROM | TO
---- | ----
NEW | PROCESSING
PROCESSING | ENROUTE
ENROUTE | RECEIVED
RECEIVED | RETURNED 
(any state except RECEIVED or RETURNED) | CANCELLED 

### Other business rules:
* All orders must begin in NEW state
* Orders specifying an invalid state transition are ignored

### Building
If you want to build the project you will need apache Ant
http://ant.apache.org/bindownload.cgi

Set ANT_HOME variable to wherever Ant is, and in the project root directory (the same same directory as this file) run

`$> $ANT_HOME/ant clean`

`$> $ANT_HOME/ant`

This will rebuild the executable jar from source.

### Report
When the program reads an EOF, it prints a summary of all of the orders currently in memory.

### Notes
For convenience I've included an executable jar file if you don't have ANT.  
To run the application you will need a Java runtime installation version 1.6 or higher.
Easiest approach is to just run:

`$> java -jar order-app.jar`

I've included the sample input in a text file so you can pipe the input if you like:

`$> cat input | java -jar order-app.jar`

The default behavior is to silently ignore malformed or invalid requests, however if you add the command line argument -Ddebug=true the failures will appear in stderr as stack traces.


The entry point for the java application is in 
src/org/bendra/yelptest/petespizza/Console.java main[] method

Most of the work is in a singleton class, which uses an in-memory collection to store orders and updates as they are processed:
/src/org/bendra/yelptest/petespizza/service/OrderService.java

===========================

### Licensing and legal issues
  --------------------------

See licenses for accompanying products in the "legal" directory.
* Gson is licensed under the Apache License, version 2.0
* JUnit is licensed under the Eclipse Public License version 1.0
* Hamcrest is licensed under the New BSD License 
