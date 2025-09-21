package org.dw363.cryptofetch;

import org.dw363.cryptofetch.currency.CryptoCurrency;
import org.dw363.cryptofetch.manager.CryptoCacheManager;

import java.math.BigDecimal;

public final class CryptoFetchAPI {
    private CryptoFetchAPI() {}

    public static void init(long updateIntervalSeconds) {
        CryptoCacheManager.setUpdateInterval(updateIntervalSeconds);
    }

    public static double get(String currencyName) {
        return get(CryptoCurrency.fromName(currencyName));
    }

    public static double get(CryptoCurrency currency) {
        String raw = CryptoCacheManager.getPriceString(currency);
        if (raw == null) return -1.0;
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    public static String str(String currencyName) {
        return str(CryptoCurrency.fromName(currencyName));
    }

    public static String str(CryptoCurrency currency) {
        String raw = CryptoCacheManager.getPriceString(currency);
        if (raw == null) return "loading...";

        BigDecimal bd;
        try {
            bd = new BigDecimal(raw);
        } catch (NumberFormatException e) {
            return "n/a";
        }

        String plain = bd.toPlainString();

        if (plain.indexOf('.') >= 0) {
            while (plain.endsWith("0")) {
                plain = plain.substring(0, plain.length() - 1);
            }
            if (plain.endsWith(".")) {
                plain = plain.substring(0, plain.length() - 1);
            }
            if (plain.isEmpty() || plain.equals("-")) {
                plain = "0";
            }
        }

        if (plain.startsWith(".")) plain = "0" + plain;
        if (plain.startsWith("-.")) plain = plain.replaceFirst("-\\.", "-0.");

        return plain + " €";
    }
}