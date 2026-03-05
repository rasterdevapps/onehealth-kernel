package com.onehealth.lis.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${lis.rabbitmq.exchange}")
    private String exchange;

    @Value("${lis.rabbitmq.queue}")
    private String queue;

    @Value("${lis.rabbitmq.routing-key}")
    private String routingKey;

    @Bean
    public TopicExchange lisExchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Queue labOrderCreatedQueue() {
        return new Queue(queue, true);
    }

    @Bean
    public Binding labOrderBinding(Queue labOrderCreatedQueue, TopicExchange lisExchange) {
        return BindingBuilder.bind(labOrderCreatedQueue).to(lisExchange).with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
