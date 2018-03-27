package io.axoniq.training.labs.giftcard.command;

import io.axoniq.training.labs.giftcard.coreapi.CardIssuedEvent;
import io.axoniq.training.labs.giftcard.coreapi.CardRedeemedEvent;
import io.axoniq.training.labs.giftcard.coreapi.IssueCardCommand;
import io.axoniq.training.labs.giftcard.coreapi.RedeemCardCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateMember;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Entity
@Aggregate
public class GiftCard {

    @Id
    @AggregateIdentifier
    private String id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="giftCardId")
    @AggregateMember
    private List<GiftCardTransaction> transactions = new ArrayList<>();

    private int remainingValue;

    public GiftCard() {
    }

    @CommandHandler
    public GiftCard(IssueCardCommand cmd) {
        if (cmd.getAmount() <= 0) throw new IllegalArgumentException("amount <= 0");

        apply(new CardIssuedEvent(cmd.getCardId(), cmd.getAmount()));
    }

    @CommandHandler
    public void handle(RedeemCardCommand cmd) {
        if (cmd.getAmount() <= 0) throw new IllegalArgumentException("amount <= 0");
        if (cmd.getAmount() > remainingValue) throw new IllegalStateException("amount > remaining value");
        if (transactions.stream().map(GiftCardTransaction::getTransactionId).anyMatch(cmd.getTransactionId()::equals))
            throw new IllegalStateException("TransactionId must be unique");

        apply(new CardRedeemedEvent(id, cmd.getTransactionId(), cmd.getAmount()));
    }

    @EventSourcingHandler
    protected void on(CardIssuedEvent event){
        id = event.getCardId();
        remainingValue = event.getAmount();
    }

    @EventSourcingHandler
    protected void on(CardRedeemedEvent event){
        id = event.getCardId();
        remainingValue = event.getAmount();
    }

}
