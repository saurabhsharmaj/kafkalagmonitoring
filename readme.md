mysqldb
elastic

zoo
kafka
-------------------

download Kafka: https://downloads.apache.org/kafka/3.4.0/kafka_2.12-3.4.0.tgz
Extract and create following batch files:

startZoo.bat
-------------------------------------------------------------------------------------------------------------------------------------
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

startBroker.bat
-------------------------------------------------------------------------------------------------------------------------------------
set JAVA_OPTS=%JAVA_OPTS% -Djava.security.auth.login.config=C:\\opt\\kafka\\kafka_2.12-3.2.3\\config\\kafka_server_jaas.conf

set COMMAND=%JAVA% %JAVA_OPTS% %KAFKA_HEAP_OPTS% %KAFKA_JVM_PERFORMANCE_OPTS% %KAFKA_JMX_OPTS% %KAFKA_LOG4J_OPTS% -cp "%CLASSPATH%" %KAFKA_OPTS% %*

.\bin\windows\kafka-server-start.bat .\config\server.properties

-------------------------------------------------------------------------------
Download Start kafdrop: https://github.com/obsidiandynamics/kafdrop/releases/download/3.31.0/kafdrop-3.31.0.jar

Put following lines into kafdropStart.bat
-----------------------------------------------------------------------
set path="C:\opt\equinix\software\jdk-11.0.2\bin";%path%
java -jar kafdrop.jar --SERVER.PORT=9002 --MANAGEMENT.SERVER.PORT=9002

