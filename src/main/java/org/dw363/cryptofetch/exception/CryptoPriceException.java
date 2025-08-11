package org.dw363.cryptofetch.exception;

public class CryptoPriceException extends RuntimeException {
    public CryptoPriceException(String message) {
        super(message);
    }

    public CryptoPriceException(String message, Throwable cause) {
        super(message, cause);
    }
}