package com.pruebatecnica.accountservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public DirectExchange customerEventsExchange(@Value("${messaging.customer.exchange}") String exchangeName) {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Queue customerEventsQueue(@Value("${messaging.customer.queue}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Declarables customerEventBindings(
            DirectExchange customerEventsExchange,
            Queue customerEventsQueue,
            @Value("${messaging.customer.routing-keys.created}") String createdRoutingKey,
            @Value("${messaging.customer.routing-keys.updated}") String updatedRoutingKey,
            @Value("${messaging.customer.routing-keys.status-changed}") String statusChangedRoutingKey,
            @Value("${messaging.customer.routing-keys.deleted}") String deletedRoutingKey
    ) {
        return new Declarables(
                BindingBuilder.bind(customerEventsQueue).to(customerEventsExchange).with(createdRoutingKey),
                BindingBuilder.bind(customerEventsQueue).to(customerEventsExchange).with(updatedRoutingKey),
                BindingBuilder.bind(customerEventsQueue).to(customerEventsExchange).with(statusChangedRoutingKey),
                BindingBuilder.bind(customerEventsQueue).to(customerEventsExchange).with(deletedRoutingKey)
        );
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages(
                "com.pruebatecnica.accountservice.infrastructure.messaging.event",
                "com.pruebatecnica.customerservice.infrastructure.messaging.event"
        );
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}
