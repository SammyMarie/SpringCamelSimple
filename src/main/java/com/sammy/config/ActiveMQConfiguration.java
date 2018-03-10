package com.sammy.config;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;

@Configuration
public class ActiveMQConfiguration {

    @Autowired
    CamelContext camelContext;

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String jmsUser;

    @Value("${spring.activemq.password}")
    private String jmsPassword;

    @Bean
    public ActiveMQComponent activeMQComponent() {
        ActiveMQComponent activeMQComponent= new ActiveMQComponent(camelContext);
        activeMQComponent.setConfiguration(jmsConfig());
        return activeMQComponent;
    }

    @Bean
    public ConnectionFactory jmsConnectionFactory() {

        PooledConnectionFactory pool = new PooledConnectionFactory();

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setUserName(jmsUser);
        connectionFactory.setPassword(jmsPassword);

        pool.setConnectionFactory(connectionFactory);
        return pool;
    }

    @Bean
    public JmsConfiguration jmsConfig(){
        return new JmsConfiguration(jmsConnectionFactory());
    }
}