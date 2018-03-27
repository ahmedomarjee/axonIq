package io.axoniq.training.labs.giftcard;

import com.rabbitmq.client.Channel;
import io.axoniq.training.labs.giftcard.coreapi.CardIssuedEvent;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.MetaData;
import org.axonframework.serialization.Serializer;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class AmqpConfig {

    @Bean
    public SpringAMQPMessageSource springAMQPMessageSource(Serializer serializer) {
        return new SpringAMQPMessageSource(serializer) {
            @RabbitListener(queues = "events")
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                super.onMessage(message, channel);
            }
        };
    }

    @Bean
    public Exchange eventsExchange() {
        return ExchangeBuilder.topicExchange("events").build();
    }

    @Bean
    public Queue eventsQueue() {
        return QueueBuilder.durable("events").build();
    }

    @Bean
    public Binding eventsBinding() {
        return BindingBuilder.bind(eventsQueue()).to(eventsExchange()).with("#").noargs();
    }

    @Autowired
    public void config(AmqpAdmin admin, EventHandlingConfiguration ehConfig, SpringAMQPMessageSource messageSource) {
        admin.declareExchange(eventsExchange());
        admin.declareQueue(eventsQueue());
        admin.declareBinding(eventsBinding());

        ehConfig.registerSubscribingEventProcessor("rabbit-events", c -> messageSource);
    }

    @ProcessingGroup("rabbit-events")
    @Component
    public static class SystemOutHandler {

        @EventHandler
        public void handle(CardIssuedEvent event, MetaData metaData) {
            System.out.println("Received card issued: " + event.getCardId());
            metaData.forEach((k, v) -> System.out.println("    - " + k + ": " + v));
        }
    }
}
