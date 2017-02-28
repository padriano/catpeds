FROM frolvlad/alpine-oraclejdk8:slim
MAINTAINER Nuno Padriano <padriano@gmail.com>
VOLUME /tmp
COPY catpeds-rest-1.0-SNAPSHOT.jar app.jar
COPY lib lib
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
