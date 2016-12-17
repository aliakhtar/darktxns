# Apache Kafka

FROM 134217665810.dkr.ecr.us-east-1.amazonaws.com/swdc/jdk:latest
#FROM 984785553193.dkr.ecr.us-west-2.amazonaws.com/swdc/jdk:latest

ENV KAFKA_HOME /opt/kafka

COPY ./start.sh ./
COPY ./server.properties ./

RUN mkdir -p $KAFKA_HOME \
    && chmod +x ./start.sh \
    && curl -L -O http://www-us.apache.org/dist/kafka/0.10.0.1/kafka_2.11-0.10.0.1.tgz \
    && tar -xvf kafka*.tgz \
    && rm -f kafka*.tgz \
    && mv ./kafka*/* $KAFKA_HOME

EXPOSE 9092

# VOLUME ["/var/lib/kafka"]

CMD ["./start.sh"]