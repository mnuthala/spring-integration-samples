package com.mark.integration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.integration.stream.CharacterStreamReadingMessageSource;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.MessageChannel;

import javax.jms.Queue;

/**
 * Created by mark on 17/10/2018.
 */
@SpringBootApplication
public class MqAdapterToKafkaApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(MqAdapterToKafkaApp.class).run(args);
        //SpringApplication.run(AmqAdapterApp.class, args);
    }

    @Bean
    public MessageChannel jmsInputChannel(){
        return new DirectChannel();
    }

    @Bean
    public MessageChannel kafkaInputChannel(){
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow fromStdInToStdInputChannel(){
        return IntegrationFlows.from(CharacterStreamReadingMessageSource.stdin(),
                e -> e.poller(p -> p.fixedDelay(10)))
                //.log(LoggingHandler.Level.INFO)
                .channel("jmsInputChannel")
                .get();
    }

    @Bean
    public IntegrationFlow fromStdInputChannelToMQ(JmsTemplate jmsTemplate, Queue requestQueue){
        return IntegrationFlows.from("jmsInputChannel")
                //.log(LoggingHandler.Level.INFO)
                .handle(Jms.outboundAdapter(jmsTemplate).destination(requestQueue))
                .get();
    }

    @Bean IntegrationFlow fromMQToKafkaInputChannel(CachingConnectionFactory connectionFactory,
                                                          Queue requestQueue,
                                                          @Value("${filter.expression}") String expression) {
        return IntegrationFlows.from(Jms.messageDrivenChannelAdapter(connectionFactory).destination(requestQueue))
                //.log(LoggingHandler.Level.INFO)
                .filter(expression)
                .channel("kafkaInputChannel")
                .get();
    }

    @Bean IntegrationFlow fromKafkaInputChannelToKafkaTopic(KafkaTemplate kafkaTemplate,
                                                            @Value("${integration.responseTopic}") String topic){
        return IntegrationFlows.from("kafkaInputChannel")
                .log(LoggingHandler.Level.INFO)
                .handle(Kafka.outboundChannelAdapter(kafkaTemplate).topic(topic))
                .get();
    }


}
