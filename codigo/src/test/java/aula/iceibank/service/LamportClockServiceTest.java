package aula.iceibank.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LamportClockServiceTest {

    @Test
    void shouldApplyAllLamportRules() {
        LamportClockService clock = new LamportClockService();

        assertThat(clock.localEvent()).isEqualTo(1);
        assertThat(clock.sendEvent()).isEqualTo(2);
        assertThat(clock.receiveEvent(7)).isEqualTo(8);
        assertThat(clock.receiveEvent(3)).isEqualTo(9);
    }
}
