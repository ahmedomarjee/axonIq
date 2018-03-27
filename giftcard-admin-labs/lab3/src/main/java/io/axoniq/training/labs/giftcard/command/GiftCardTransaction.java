package io.axoniq.training.labs.giftcard.command;

import io.axoniq.training.labs.giftcard.coreapi.CardReimbursedEvent;
import io.axoniq.training.labs.giftcard.coreapi.ReimburseCardCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.EntityId;

import javax.persistence.*;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"transactionId", "giftCardId"}))
public class GiftCardTransaction {

    @Id
    @GeneratedValue
    private String id;

    @EntityId
    private String transactionId;

    private String giftCardId;

    private int transactionValue;
    private boolean reimbursed = false;

    protected GiftCardTransaction() {
    }

    public GiftCardTransaction(String giftCardId, String transactionId, int transactionValue) {
        this.giftCardId = giftCardId;
        this.transactionId = transactionId;
        this.transactionValue = transactionValue;
    }

    @CommandHandler
    public void handle(ReimburseCardCommand cmd) {
        if (reimbursed) throw new IllegalStateException("Transaction already reimbursed");
        reimbursed = true;
        apply(new CardReimbursedEvent(cmd.getCardId(), transactionId, transactionValue));
    }

    public String getTransactionId() {
        return transactionId;
    }

}
