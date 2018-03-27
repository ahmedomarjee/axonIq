package io.axoniq.training.labs.giftcard.command;

import io.axoniq.training.labs.giftcard.coreapi.CardReimbursedEvent;
import io.axoniq.training.labs.giftcard.coreapi.ReimburseCardCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.EntityId;

import javax.persistence.*;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCardTransaction that = (GiftCardTransaction) o;
        return transactionValue == that.transactionValue &&
                reimbursed == that.reimbursed &&
                Objects.equals(id, that.id) &&
                Objects.equals(transactionId, that.transactionId) &&
                Objects.equals(giftCardId, that.giftCardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transactionId, giftCardId, transactionValue, reimbursed);
    }
}
