package io.axoniq.training.labs.giftcard;

import io.axoniq.axondb.client.AxonDBConfiguration;
import io.axoniq.axondb.client.axon.AxonDBEventStore;
import io.axoniq.training.labs.giftcard.gui.VaadinSessionDispatchInterceptor;
import io.axoniq.training.labs.giftcard.saga.GiftCardPaymentSaga;
import io.axoniq.training.labs.order.coreapi.OrderCommandHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
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
    public SagaConfiguration<GiftCardPaymentSaga> giftCardPaymentSagaConfiguration() {
        return SagaConfiguration.trackingSagaManager(GiftCardPaymentSaga.class);
    }

    @Bean
    public CommandGateway commandGateway(CommandBus commandBus) {
        return new DefaultCommandGateway(commandBus, new Vaadin
                SessionDispatchInterceptor());
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
