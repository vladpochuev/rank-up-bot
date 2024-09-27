package com.luxusxc.rank_up.telegram.callbacks;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

public class CallbackTypeTest {
    @Test
    void testGetInstanceInvalid() {
        String command = "123";
        assertThat(CallbackType.getInstance(command), nullValue());
    }

    @Test
    void testGetInstanceNull() {
        String command = null;
        assertThat(CallbackType.getInstance(command), nullValue());
    }
}
