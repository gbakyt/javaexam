package kz.gaziz.java.exam;

import kz.gaziz.java.exam.model.RequestThrottlingModel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RequestThrottlingModelTest {
    private final RequestThrottlingModel requestThrottlingModel = new RequestThrottlingModel(); // Limit: 5 requests per 1 minute

    @Test
    public void testRequestLimiter() throws InterruptedException {
        requestThrottlingModel.setMaxRequests(5);
        requestThrottlingModel.setIntervalInMillis(1);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                String ipAddress = generateRandomIpAddress();
                boolean allowed = requestThrottlingModel.allowRequest(ipAddress);
                assertTrue(allowed);
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
    }

    private String generateRandomIpAddress() {
        // Generate a random IP address (for test purposes)
        return "192.168.0." + Math.random() * 256;
    }
}

