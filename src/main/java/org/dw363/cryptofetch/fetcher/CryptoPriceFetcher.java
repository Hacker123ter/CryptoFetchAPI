package org.dw363.cryptofetch.fetcher;

import org.dw363.cryptofetch.currency.CryptoCurrency;
import org.dw363.cryptofetch.exception.CryptoPriceException;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public final class CryptoPriceFetcher {

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .version(HttpClient.Version.HTTP_2)
            .build();

    private static final String API_TEMPLATE = "https://api.coingecko.com/api/v3/simple/price?ids=%s&vs_currencies=eur";

    private static final Pattern EUR_PATTERN =
            Pattern.compile("\"eur\"\\s*:\\s*([0-9]+(?:\\.[0-9]+)?(?:[eE][+-]?[0-9]+)?)");

    private CryptoPriceFetcher() {}

    public static String getPriceStringSync(CryptoCurrency currency) {
        String url = String.format(API_TEMPLATE, currency.apiId());
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "CryptoFetchAPI/1.0.2 (+https://github.com/Hacker123ter/CryptoFetchAPI)")
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();
        try {
            HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() != 200) {
                throw new CryptoPriceException("API error: HTTP " + resp.statusCode());
            }
            return parsePriceString(resp.body());
        } catch (IOException | InterruptedException e) {
            throw new CryptoPriceException("Error fetching price", e);
        }
    }

    public static CompletableFuture<String> getPriceStringAsync(CryptoCurrency currency) {
        return CompletableFuture.supplyAsync(() -> getPriceStringSync(currency));
    }

    private static String parsePriceString(String body) {
        var matcher = EUR_PATTERN.matcher(body);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new CryptoPriceException("EUR price not found in response: " + body);
    }
}