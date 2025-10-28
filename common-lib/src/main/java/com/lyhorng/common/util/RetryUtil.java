package com.lyhorng.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

/**
 * Utility class for retry operations
 */
@Slf4j
public class RetryUtil {

    private RetryUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Execute a supplier with retry logic
     *
     * @param supplier The operation to retry
     * @param maxRetries Maximum number of retries
     * @param retryDelayMs Delay between retries in milliseconds
     * @param <T> Return type
     * @return Result of the supplier
     * @throws RuntimeException If all retries fail
     */
    public static <T> T executeWithRetry(Supplier<T> supplier, int maxRetries, long retryDelayMs) {
        return executeWithRetry(supplier, maxRetries, retryDelayMs, null);
    }

    /**
     * Execute a supplier with retry logic and exception handling
     *
     * @param supplier The operation to retry
     * @param maxRetries Maximum number of retries
     * @param retryDelayMs Delay between retries in milliseconds
     * @param retryOnException Exception types to retry on (null = retry on all exceptions)
     * @param <T> Return type
     * @return Result of the supplier
     * @throws RuntimeException If all retries fail
     */
    public static <T> T executeWithRetry(Supplier<T> supplier, int maxRetries, 
                                         long retryDelayMs, Class<? extends Exception> retryOnException) {
        int attempts = 0;
        Exception lastException = null;

        while (attempts < maxRetries) {
            try {
                return supplier.get();
            } catch (Exception e) {
                lastException = e;
                attempts++;

                if (retryOnException != null && !retryOnException.isInstance(e)) {
                    log.error("Exception not in retry list, throwing immediately: {}", e.getMessage());
                    throw new RuntimeException(e);
                }

                if (attempts < maxRetries) {
                    log.warn("Attempt {} failed, retrying in {}ms: {}", attempts, retryDelayMs, e.getMessage());
                    try {
                        Thread.sleep(retryDelayMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Retry interrupted", ie);
                    }
                }
            }
        }

        log.error("All {} retry attempts failed", maxRetries);
        throw new RuntimeException("Operation failed after " + maxRetries + " attempts", lastException);
    }

    /**
     * Execute a runnable with retry logic
     *
     * @param runnable The operation to retry
     * @param maxRetries Maximum number of retries
     * @param retryDelayMs Delay between retries in milliseconds
     */
    public static void executeWithRetry(Runnable runnable, int maxRetries, long retryDelayMs) {
        executeWithRetry(() -> {
            runnable.run();
            return null;
        }, maxRetries, retryDelayMs);
    }
}

