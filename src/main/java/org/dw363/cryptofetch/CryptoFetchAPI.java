package org.dw363.cryptofetch;

import org.dw363.cryptofetch.currency.CryptoCurrency;
import org.dw363.cryptofetch.manager.CryptoCacheManager;

public final class CryptoFetchAPI {
    private CryptoFetchAPI() {}

    public static void init(long updateIntervalSeconds) {
        CryptoCacheManager.setUpdateInterval(updateIntervalSeconds);
    }

    public static double get(String currencyName) {
        return CryptoCacheManager.getPrice(CryptoCurrency.fromName(currencyName));
    }

    public static double get(CryptoCurrency currency) {
        return CryptoCacheManager.getPrice(currency);
    }

    public static String str(String currencyName) {
        return String.format("%.2f €", get(currencyName));
    }

    public static String str(CryptoCurrency currency) {
        return String.format("%.2f €", get(currency));
    }
}
