package io.axoniq.training.labs.giftcard.query;

import org.axonframework.commandhandling.model.EntityId;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
public class CardSummary {

    @Id
    private String cardId;

    private int initialValue;
    private Instant issuedAt;
    private int remainingValue;


    public CardSummary() {
    }

    public CardSummary(String cardId, int initialValue) {
        this.cardId = cardId;
        this.initialValue = initialValue;
        this.issuedAt = Instant.now();
        this.remainingValue = initialValue;
    }



    public String getCardId() {
        return cardId;
    }

    public int getInitialValue() {
        return initialValue;
    }

    public Instant getIssuedAt() {
        //return Instant.now();
        return issuedAt;
    }

    public int getRemainingValue() {
        return remainingValue;
    }


    public void redeem(int amount) {
        remainingValue = getRemainingValue() - amount;
    }

    public void reinburse(int amount) {
        remainingValue = getRemainingValue() + amount;
    }
}
