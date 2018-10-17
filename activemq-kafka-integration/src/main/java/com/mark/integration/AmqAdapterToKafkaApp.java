package com.mark.integration;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
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

/**
 * Created by mark on 17/10/2018.
 */
@SpringBootApplication
public class AmqAdapterToKafkaApp implements CommandLineRunner{
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(AmqAdapterToKafkaApp.class).run(args);
        //SpringApplication.run(AmqAdapterApp.class, args);
    }

    @Override
    public void run(String... strings){

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
    public IntegrationFlow fromStdInputChannelToActiveMQ(JmsTemplate jmsTemplate, ActiveMQQueue requestQueue){
        return IntegrationFlows.from("jmsInputChannel")
                //.log(LoggingHandler.Level.INFO)
                .handle(Jms.outboundAdapter(jmsTemplate).destination(requestQueue))
                .get();
    }


    @Bean IntegrationFlow fromActiveMQToKafkaInputChannel(CachingConnectionFactory connectionFactory,
                                                          ActiveMQQueue requestQueue,
                                                          @Value("${filter.expression}") String expression) {
        return IntegrationFlows.from(Jms.messageDrivenChannelAdapter(connectionFactory).destination(requestQueue))
                //.log(LoggingHandler.Level.INFO)
                .filter(expression)
                .channel("kafkaInputChannel")
                .get();
    }

    @Bean IntegrationFlow fromKafkaInputChannelToKafkaTopic(KafkaTemplate kafkaTemplate){
        return IntegrationFlows.from("kafkaInputChannel")
                .log(LoggingHandler.Level.INFO)
                .handle(Kafka.outboundChannelAdapter(kafkaTemplate).topic("trades.input"))
                .get();
    }


}
