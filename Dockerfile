FROM sequenceiq/pam:ubuntu-14.04

# Java Version
ENV JAVA_VERSION_MAJOR 8
ENV JAVA_VERSION_MINOR 45
ENV JAVA_VERSION_BUILD 14
ENV JAVA_PACKAGE server-jre
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

# Maven version
ENV MAVEN_VERSION 3.3.3

# Install common packages
RUN apt-get -qq update && apt-get -qq -y install curl wget bash && \
    apt-get -qqy autoremove && apt-get -qqy clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# Download and unarchive Java
RUN curl -jksSLH "Cookie: oraclelicense=accept-securebackup-cookie" \
  http://download.oracle.com/otn-pub/java/jdk/${JAVA_VERSION_MAJOR}u${JAVA_VERSION_MINOR}-b${JAVA_VERSION_BUILD}/${JAVA_PACKAGE}-${JAVA_VERSION_MAJOR}u${JAVA_VERSION_MINOR}-linux-x64.tar.gz \
    | gunzip -c - | tar -xf - -C /opt && \
    mkdir -p ${JAVA_HOME} && rmdir ${JAVA_HOME} && \
    ln -s /opt/jdk1.${JAVA_VERSION_MAJOR}.0_${JAVA_VERSION_MINOR} ${JAVA_HOME}


# Download and unarchive Maven
RUN mkdir -p /usr/share/maven \
  && curl -fsSL http://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz \
    | tar -xzC /usr/share/maven --strip-components=1 \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

# Set MAVEN_HOME
ENV MAVEN_HOME /usr/share/maven

# Set path
ENV PATH ${PATH}:${JAVA_HOME}/bin:${MAVEN_HOME}/bin

# Copy project root to container
ADD . /opt/confyrm/build
WORKDIR /opt/confyrm/build

# Build DevelopersDemo
RUN mvn clean package
RUN mv target/appassembler /opt/confyrm/demo

WORKDIR  /opt/confyrm/demo

CMD ["bin/event-producer",  "/data/app.properties", "/data/events.cfg.json", "/data/identities.csv"]
