FROM cloudesire/java:8

RUN  \
  export DEBIAN_FRONTEND=noninteractive && \
  apt-get update && \
  apt-get install -y maven

ADD . /opt/confyrm/build

WORKDIR /opt/confyrm/build

RUN mvn clean package
RUN mv target/appassembler /opt/confyrm/demo

WORKDIR  /opt/confyrm/demo

CMD ["bin/event-producer",  "/data/app.properties", "/data/events.cfg.json", "/data/identities.csv"]
