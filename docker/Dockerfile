FROM openjdk:17-oracle
MAINTAINER chocoh
VOLUME /tmp
ADD gsdn-community.jar /gsdn-community.jar
RUN bash -c 'touch /gsdn-community.jar'
ENTRYPOINT ["java", "-jar", "/gsdn-community.jar"]
EXPOSE 8080