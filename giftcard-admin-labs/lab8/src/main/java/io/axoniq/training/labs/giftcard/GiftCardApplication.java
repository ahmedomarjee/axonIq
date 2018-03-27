package io.axoniq.training.labs.giftcard;

import io.axoniq.axondb.client.AxonDBConfiguration;
import io.axoniq.axondb.client.axon.AxonDBEventStore;
import io.axoniq.training.labs.giftcard.saga.GiftCardPaymentSaga;
import io.axoniq.training.labs.order.coreapi.OrderCommandHandler;
import org.axonframework.config.SagaConfiguration;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.upcasting.event.EventUpcaster;
import org.axonframework.serialization.upcasting.event.EventUpcasterChain;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class GiftCardApplication {

    public static void main(String[] args) {
        SpringApplication.run(GiftCardApplication.class, args);
    }

    @Bean
    public OrderCommandHandler orderCommandHandler() {
        return new OrderCommandHandler();
    }

    @Bean
    public EventStore eventStore(Serializer eventSerializer, List<EventUpcaster> upcasters) {
        return new AxonDBEventStore(axonDBConfig(), eventSerializer, new EventUpcasterChain(upcasters));
    }

    @Bean
    public AxonDBConfiguration axonDBConfig() {
        return new AxonDBConfiguration();
    }

}
