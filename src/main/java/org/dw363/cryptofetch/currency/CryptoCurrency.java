package org.dw363.cryptofetch.currency;

public enum CryptoCurrency {
    BITCOIN("bitcoin"),
    ETHEREUM("ethereum"),
    LITECOIN("litecoin"),
    DOGECOIN("dogecoin");

    private final String apiId;

    CryptoCurrency(String apiId) {
        this.apiId = apiId;
    }

    public String apiId() {
        return apiId;
    }

    public static CryptoCurrency fromName(String name) {
        if (name == null) throw new IllegalArgumentException("Currency name is null");
        String lower = name.trim().toLowerCase();
        for (CryptoCurrency c : values()) {
            if (c.apiId.equals(lower) || c.name().equalsIgnoreCase(lower)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Unknown currency: " + name);
    }
}