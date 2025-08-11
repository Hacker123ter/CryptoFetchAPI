package org.dw363.cryptofetch.manager;

import org.dw363.cryptofetch.currency.CryptoCurrency;
import org.dw363.cryptofetch.fetcher.CryptoPriceFetcher;

import java.util.Map;
import java.util.concurrent.*;

public final class CryptoCacheManager {

    private static final Map<CryptoCurrency, Double> CACHE = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "CryptoFetchAPI-AutoUpdater");
        t.setDaemon(true);
        return t;
    });

    private static long updateIntervalSec = 60;
    private static ScheduledFuture<?> updateTask;

    static {
        startScheduler();
    }

    private static void startScheduler() {
        if (updateTask != null && !updateTask.isCancelled()) {
            updateTask.cancel(false);
        }
        updateTask = SCHEDULER.scheduleAtFixedRate(() -> {
            for (CryptoCurrency currency : CACHE.keySet()) {
                CryptoPriceFetcher.getPriceAsync(currency).thenAccept(price ->
                        CACHE.put(currency, price)
                ).exceptionally(ex -> {
                    System.err.println("[CryptoFetchAPI] Failed to update " + currency + ": " + ex.getMessage());
                    return null;
                });
            }
        }, 0, updateIntervalSec, TimeUnit.SECONDS);
    }

    private CryptoCacheManager() {}

    public static double getPrice(CryptoCurrency currency) {
        if (!CACHE.containsKey(currency)) {
            CACHE.put(currency, -1.0);
            CryptoPriceFetcher.getPriceAsync(currency).thenAccept(price ->
                    CACHE.put(currency, price)
            ).exceptionally(ex -> {
                System.err.println("[CryptoFetchAPI] Initial fetch failed: " + ex.getMessage());
                return null;
            });
        }
        return CACHE.get(currency);
    }

    public static void setUpdateInterval(long seconds) {
        updateIntervalSec = seconds;
        startScheduler();
    }
}
