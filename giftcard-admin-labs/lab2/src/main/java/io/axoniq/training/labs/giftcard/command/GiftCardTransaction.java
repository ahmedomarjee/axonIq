package io.axoniq.training.labs.giftcard.command;

import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.EntityId;

public class GiftCardTransaction {

    @EntityId
    private final String giftCardId;
    private final int amount;

    public GiftCardTransaction(String giftCardId, int amount) {
        this.giftCardId = giftCardId;
        this.amount = amount;

    }
}
