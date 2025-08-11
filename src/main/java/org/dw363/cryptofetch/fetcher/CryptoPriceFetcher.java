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
    private static final Pattern EUR_PATTERN = Pattern.compile("\"eur\"\\s*:\\s*([0-9]+(?:\\.[0-9]+)?)");

    private CryptoPriceFetcher() {}

    public static double getPriceSync(CryptoCurrency currency) {
        String url = String.format(API_TEMPLATE, currency.apiId());
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "CryptoPriceLib/1.0 (+https://github.com/YourGitHubName/crypto-price-lib)")
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();
        try {
            HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() != 200) {
                throw new CryptoPriceException("API error: HTTP " + resp.statusCode());
            }
            return parsePrice(resp.body());
        } catch (IOException | InterruptedException e) {
            throw new CryptoPriceException("Error fetching price", e);
        }
    }

    public static CompletableFuture<Double> getPriceAsync(CryptoCurrency currency) {
        return CompletableFuture.supplyAsync(() -> getPriceSync(currency));
    }

    private static double parsePrice(String body) {
        var matcher = EUR_PATTERN.matcher(body);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }
        throw new CryptoPriceException("EUR price not found in response: " + body);
    }
}
