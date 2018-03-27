package io.axoniq.training.labs.giftcard.saga;

import org.axonframework.test.saga.SagaTestFixture;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class GiftCardPaymentSagaTest {

    private SagaTestFixture<GiftCardPaymentSaga> fixture;

    @Before
    public void setUp() {
        this.fixture = new SagaTestFixture<>(GiftCardPaymentSaga.class);
    }

    @Test
    public void shouldRedeemGiftCardWhenOrderIsPlaced() {
        fail("To be implemented");
    }
}
