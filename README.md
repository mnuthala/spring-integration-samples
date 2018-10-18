# spring-integration-samples
Samples based on Spring integration (DSL)

## Module (activemq-kafka-integration)
It is based on the spring integration framework, where it takes input from StdIn and pushes it to ActiveMQ Topic/Queue
and then an ActiveMQ consumer reads the messages from Queue/Topic and fitlers the messages based on the filter expression.
The filtered messages are then published on to Kafka topic.

## Module (mq-kafka-integration)
Using spring integration framework, (based on the profile existing - activemq), ActiveMQ or IBMMQ integration is wired in.
The integration flow :  StdIn -> Channel -> IBMMQ / ActiveMQ -> Channel -> filter -> Channel -> Kafka (Topic)