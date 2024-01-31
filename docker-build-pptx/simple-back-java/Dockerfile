FROM eclipse-temurin:17-jre
COPY target/appassembler/bin /opt/simple-app/bin
COPY target/appassembler/jars /opt/simple-app/jars
RUN mkdir /opt/simple-app/var
EXPOSE 4567
ENTRYPOINT ["java", "-cp", "/opt/simple-app/jars/*", "org.example.Main", "-data", "/opt/simple-app/var"]