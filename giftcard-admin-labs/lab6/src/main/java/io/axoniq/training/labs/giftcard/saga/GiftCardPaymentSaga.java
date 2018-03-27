package io.axoniq.training.labs.giftcard.saga;

import io.axoniq.training.labs.giftcard.coreapi.CardRedeemedEvent;
import io.axoniq.training.labs.giftcard.coreapi.CardReimbursedEvent;
import io.axoniq.training.labs.giftcard.coreapi.RedeemCardCommand;
import io.axoniq.training.labs.order.coreapi.ConfirmGiftCardPaymentCommand;
import io.axoniq.training.labs.order.coreapi.OrderPlacedEvent;
import io.axoniq.training.labs.order.coreapi.RejectGiftCardPaymentCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.saga.EndSaga;
import org.axonframework.eventhandling.saga.SagaEventHandler;
import org.axonframework.eventhandling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.axonframework.eventhandling.saga.SagaLifecycle.associateWith;

@Saga
public class GiftCardPaymentSaga {

    @Autowired
    public transient CommandGateway commandGateway;

    String orderId;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderPlacedEvent event) {
        orderId = event.getOrderId();
        associateWith("cardId", event.getCardId());
        commandGateway.send(new RedeemCardCommand(event.getCardId(), orderId, event.getGiftCardAmount()));
    }

    @SagaEventHandler(associationProperty = "cardId")
    public void handle(CardRedeemedEvent event){
        if (orderId.equals(event.getTransactionId())) {
            commandGateway.send(new ConfirmGiftCardPaymentCommand(orderId, event.getCardId(),event.getAmount()));
        }
    }


}
