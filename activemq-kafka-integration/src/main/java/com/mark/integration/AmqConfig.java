package com.mark.integration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.connection.CachingConnectionFactory;

/**
 * Created by mark on 17/10/2018.
 */
@Configuration
public class AmqConfig {

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory(@Value("${activemq.brokerUrl:tcp://10.1.2.10:61616}") String brokerUrl){
        return new ActiveMQConnectionFactory(brokerUrl);
    }

    @Bean
    @Primary
    public CachingConnectionFactory connectionFactory(ActiveMQConnectionFactory activeMQConnectionFactory,
                                                      @Value("${activemq.sessionCacheSize:10}") int sessionCacheSize,
                                                      @Value("${activemq.cacheConsumers:false}") boolean cacheConsumers){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(activeMQConnectionFactory);
        connectionFactory.setSessionCacheSize(sessionCacheSize);
        connectionFactory.setCacheConsumers(cacheConsumers);
        return connectionFactory;
    }

    @Bean
    public ActiveMQQueue requestQueue(@Value("${activemq.requestQueue:queue.request}") String requestQueue){
        return new ActiveMQQueue(requestQueue);
    }

    @Bean
    public ActiveMQQueue responseQueue(@Value("${activemq.responseQueue:queue.response}") String responseQueue){
        return new ActiveMQQueue(responseQueue);
    }

    @Bean
    public ActiveMQTopic requestTopic(@Value("${activemq.requestTopic:topic.request}") String requestTopic) {
        return new ActiveMQTopic(requestTopic);
    }

    //TODO: Poller integration!!!

}
