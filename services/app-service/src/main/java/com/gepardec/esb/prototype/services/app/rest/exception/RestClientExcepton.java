package com.gepardec.esb.prototype.services.app.rest.exception;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 7/17/2018
 */
public class RestClientExcepton extends RuntimeException {

    public RestClientExcepton() {
    }

    public RestClientExcepton(String message) {
        super(message);
    }

    public RestClientExcepton(String message,
                              Throwable cause) {
        super(message, cause);
    }

    public RestClientExcepton(Throwable cause) {
        super(cause);
    }

    public RestClientExcepton(String message,
                              Throwable cause,
                              boolean enableSuppression,
                              boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
