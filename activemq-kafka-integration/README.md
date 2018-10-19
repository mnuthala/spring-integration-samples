# Spring Integration (ActiveMq-kafka-integration)
> Integrating [StdIn, ActiveMQ, Kafka]

Using spring integration framework, (based on the profile existing - activemq), ActiveMQ or IBMMQ integration is wired in.
The integration flow :  StdIn -> Channel -> ActiveMQ -> Channel -> filter -> Channel -> Kafka (Topic)

## External dependencies

Need Kafka Server and ActiveMQ to be up and running.