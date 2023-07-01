package kz.gaziz.java.exam.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RequestThrottlingModel {
    private final Map<String, RequestCounter> ipRequestCounters = new ConcurrentHashMap<>();
    private int maxRequests;
    private long intervalInMillis;

    @Value("${throttling.limit.requests}")
    public void setMaxRequests(int maxRequests) {
        this.maxRequests = maxRequests;
    }

    @Value("${throttling.limit.minutes}")
    public void setIntervalInMillis(long intervalInMillis) {
        this.intervalInMillis = TimeUnit.MINUTES.toMillis(intervalInMillis);
    }

    public boolean allowRequest(String ipAddress) {
        long currentTime = System.currentTimeMillis();
        RequestCounter requestCounter = ipRequestCounters.computeIfAbsent(ipAddress, key -> new RequestCounter(currentTime));
        synchronized (requestCounter) {
            if (currentTime - requestCounter.getTimeStamp() > intervalInMillis) {
                requestCounter.reset(currentTime);
            }
            if (requestCounter.getCount().get() > maxRequests) {
                return false;
            }
            requestCounter.incrementCount();
        }
        return true;
    }

    @Getter
    private static class RequestCounter {
        private final AtomicInteger count;
        private long timeStamp;

        public RequestCounter(long timeStamp) {
            this.count = new AtomicInteger(1);
            this.timeStamp = timeStamp;
        }

        public void incrementCount() {
            count.incrementAndGet();
        }

        public void reset(long newTimeStamp) {
            count.set(1);
            timeStamp = newTimeStamp;
        }
    }
}
