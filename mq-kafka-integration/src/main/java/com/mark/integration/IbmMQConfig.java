package com.mark.integration;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.spring.boot.MQAutoConfiguration;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jms.connection.CachingConnectionFactory;

@Profile("!activemq")
@Import(MQAutoConfiguration.class)
@Configuration
public class IbmMQConfig {
    @Bean
    @Primary
    public CachingConnectionFactory connectionFactory(MQConnectionFactory mqConnectionFactory,
                                                      @Value("${ibmMQ.sessionCacheSize:10}") int sessionCacheSize,
                                                      @Value("${ibmMQ.cacheConsumers:false}") boolean cacheConsumers){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(mqConnectionFactory);
        connectionFactory.setSessionCacheSize(sessionCacheSize);
        connectionFactory.setCacheConsumers(cacheConsumers);
        return connectionFactory;
    }

    @Bean
    @SneakyThrows
    public MQQueue requestQueue(@Value("${integration.requestQueue:DEV.QUEUE.1}") String requestQueue){
        return new MQQueue(requestQueue);
    }
}
