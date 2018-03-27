package io.axoniq.training.labs.giftcard.command;

import com.google.common.collect.Lists;
import io.axoniq.training.labs.giftcard.coreapi.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.commandhandling.model.AggregateMember;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.List;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
public class GiftCard {


    @AggregateIdentifier
    private String giftCardId;

    @AggregateMember
    List<GiftCardTransaction> transactions = Lists.newArrayList();

    private int remainingValue;

    public GiftCard() {
    }

    @CommandHandler
    public GiftCard(IssueCardCommand cmd) {
        apply(new CardIssuedEvent(cmd.getCardId(),cmd.getAmount() ));
    }

    @CommandHandler
    public void handleRedeem(RedeemCardCommand cmd){
        apply(new CardRedeemedEvent(cmd.getCardId(),cmd.getTransactionId(),cmd.getAmount()));
    }

    @CommandHandler
    public void handleReimburse(ReimburseCardCommand cmd){


        apply(new CardReimbursedEvent(cmd.getCardId(),cmd.getTransactionId(),0));
    }

    @EventSourcingHandler
    public void on(CardIssuedEvent evt) {
        giftCardId = evt.getCardId();
        remainingValue = evt.getAmount();
    }

    @EventSourcingHandler
    public void on(CardRedeemedEvent evt) {
        remainingValue -= evt.getAmount();
        transactions.add(new GiftCardTransaction(evt.getTransactionId(), evt.getAmount()));
    }


}
