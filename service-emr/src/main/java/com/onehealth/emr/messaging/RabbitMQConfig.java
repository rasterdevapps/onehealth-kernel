package com.onehealth.emr.messaging;

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

    @Value("${emr.rabbitmq.lis-exchange}")
    private String lisExchange;

    @Value("${emr.rabbitmq.lab-result-routing-key}")
    private String labResultRoutingKey;

    @Value("${emr.rabbitmq.lab-result-queue}")
    private String labResultQueue;

    @Bean
    public TopicExchange emrLisExchange() {
        return new TopicExchange(lisExchange);
    }

    @Bean
    public Queue emrLabResultQueue() {
        return new Queue(labResultQueue, true);
    }

    @Bean
    public Binding emrLabResultBinding(Queue emrLabResultQueue, TopicExchange emrLisExchange) {
        return BindingBuilder.bind(emrLabResultQueue).to(emrLisExchange).with(labResultRoutingKey);
    }

    @Bean
    public MessageConverter emrJsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
