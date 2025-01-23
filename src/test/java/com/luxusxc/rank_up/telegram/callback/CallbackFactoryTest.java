package com.luxusxc.rank_up.telegram.callback;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CallbackFactoryTest {
    private final CallbackFactory callbackFactory = new CallbackFactory(null);

    @Test
    void testGetCallbackNull() {
        assertThrows(IllegalArgumentException.class, () -> callbackFactory.getCallback(null));
    }
}
