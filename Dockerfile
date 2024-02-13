# docker build --build-arg SPRING_ACTIVE_PROFILE=dev -t micrometer-boot .
FROM openjdk:11
VOLUME /tmp
ARG JAR_FILE
ARG SPRING_ACTIVE_PROFILE
RUN echo $SPRING_ACTIVE_PROFILE
ENV USE_PROFILE=${SPRING_ACTIVE_PROFILE}
ADD target/micrometer-boot-0.0.1-SNAPSHOT.jar app.jar
ENV JAVA_OPTS="-Dfile.encoding=UTF-8 -Djava.security.egd=file:/dev/./urandom"
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT exec java ${JAVA_OPTS} -Dspring.profiles.active=${USE_PROFILE} -jar app.jar
