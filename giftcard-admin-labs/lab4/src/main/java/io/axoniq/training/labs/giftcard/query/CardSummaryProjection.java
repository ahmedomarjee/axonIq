package io.axoniq.training.labs.giftcard.query;

import io.axoniq.training.labs.giftcard.coreapi.*;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

@Component
public class CardSummaryProjection {

    private static final Logger logger = LoggerFactory.getLogger(CardSummaryProjection.class);
    private final EntityManager entityManager;

    public CardSummaryProjection(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @EventHandler
    public void on(CardIssuedEvent evt) {
        logger.info("Card {} has been issued with value {}", evt.getCardId(), evt.getAmount());
        entityManager.persist(new CardSummary(evt.getCardId(),evt.getAmount()));
    }

    @EventHandler
    public void on(CardRedeemedEvent evt) {
        logger.info("Card {} has been redeemed for {}", evt.getCardId(), evt.getAmount());

        CardSummary cardSummary = entityManager.find(CardSummary.class, evt.getCardId());
        cardSummary.redeem(evt.getAmount());
    }

    @EventHandler
    public void on(CardReimbursedEvent evt) {
        logger.info("Transaction {} has been reimbursed for card", evt.getTransactionId(), evt.getCardId());
        CardSummary cardSummary = entityManager.find(CardSummary.class, evt.getCardId());
        cardSummary.reinburse(evt.getAmount());
    }

    @QueryHandler
    public FindCardSummariesResponse handle(FindCardSummariesQuery query) {
        List<CardSummary> result = entityManager.createQuery("SELECT c FROM CardSummary c ORDER BY c.cardId",
                CardSummary.class)
                .setFirstResult(query.getOffset())
                .setMaxResults(query.getLimit()).getResultList();
        return new FindCardSummariesResponse(result);
    }

    @QueryHandler
    public CountCardSummariesResponse handle(CountCardSummariesQuery query) {

        return new CountCardSummariesResponse(5);
    }
}
