# Spring Integration (mq-kafka-integration)
> Integrating [StdIn, ActiveMQ / IBM MQ, Kafka]

Using spring integration framework, (based on the profile existing - activemq), ActiveMQ or IBMMQ integration is wired in.
The integration flow :  StdIn -> Channel -> IBMMQ / ActiveMQ -> Channel -> filter -> Channel -> Kafka (Topic)

## External dependencies

Need Kafka Server and IBM MQ / ActiveMQ to be up and running.