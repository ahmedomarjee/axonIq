package io.axoniq.training.labs.giftcard.coreapi

import org.axonframework.commandhandling.TargetAggregateIdentifier

data class CardIssuedEvent(val cardId: String, val amount: Int)
data class CardRedeemedEvent(val cardId: String, val transactionId : String, val amount: Int)
data class CardReimbursedEvent(@TargetAggregateIdentifier val cardId: String, val transactionId : String, val amount: Int)
