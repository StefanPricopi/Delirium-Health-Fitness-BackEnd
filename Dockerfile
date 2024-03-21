FROM gradle:8.4-jdk17
WORKDIR /opt/app
COPY ./build/libs/IndivBES3-1.0-SNAPSHOT.jar ./

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar IndivBES3-1.0-SNAPSHOT.jar"]