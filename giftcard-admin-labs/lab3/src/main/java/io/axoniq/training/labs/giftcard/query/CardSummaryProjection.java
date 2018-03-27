package io.axoniq.training.labs.giftcard.query;

import io.axoniq.training.labs.giftcard.coreapi.CardIssuedEvent;
import io.axoniq.training.labs.giftcard.coreapi.CardRedeemedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class CardSummaryProjection {


    @EventHandler
    public void handleCardIssuedEvent(CardIssuedEvent event){
        System.out.println("IssueEvent Handled by CardSummaryProjection CardId:"+ event.getCardId() +"amount:"+ event.getAmount());

    }

    @EventHandler
    public void handleCardRedeemedEvent(CardRedeemedEvent event){
        System.out.println("IssueEvent Handled by CardRedeemed CardId:"+ event.getCardId() +"amount:"+ event.getAmount());

    }
}
