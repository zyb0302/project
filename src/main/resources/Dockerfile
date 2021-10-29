FROM java:8

MAINTAINER "jack"<773240670@qq.com>
ENV LANG C.UTF-8
RUN mkdir -p /data/app && mkdir -p /data/logs

WORKDIR /data
ADD blog.jar /data/app/app.jar

EXPOSE 8080
CMD java -jar -server -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:GCPauseIntervalMillis=200 -XX:SurvivorRatio=6 -XX:-UseGCOverheadLimit /data/app/app.jar

