From openjdk:11
copy ./target/kafka-0.0.1-SNAPSHOT.jar kafka.jar
CMD ["java","-jar","kafka.jar"]
