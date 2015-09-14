FROM docker.dev.confyrm.com/java8:2

ADD target/appassembler /opt/confyrm/demo/

WORKDIR  /opt/confyrm/demo

CMD ["bin/event-producer",  "/data/app.properties", "/data/events.cfg.json", "/data/identities.csv"]