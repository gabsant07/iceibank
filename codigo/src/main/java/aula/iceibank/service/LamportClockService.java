package aula.iceibank.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class LamportClockService {

    private final AtomicLong clock = new AtomicLong();

    public long localEvent() {
        return clock.incrementAndGet();
    }

    public long sendEvent() {
        return clock.incrementAndGet();
    }

    public long receiveEvent(long receivedTimestamp) {
        return clock.updateAndGet(current -> Math.max(current, receivedTimestamp) + 1);
    }

    public long current() {
        return clock.get();
    }

    public long advanceTo(long timestamp) {
        return clock.updateAndGet(current -> Math.max(current, timestamp));
    }
}
