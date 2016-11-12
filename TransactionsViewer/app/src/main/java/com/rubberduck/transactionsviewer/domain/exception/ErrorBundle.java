package com.rubberduck.transactionsviewer.domain.exception;

/**
 * Wrapper around an exception to manage errors
 */
public class ErrorBundle {

    private final Exception exception;
    private final String errorMessage;

    public ErrorBundle(Exception exception, String errorMessage) {
        this.exception = exception;
        this.errorMessage = errorMessage;
    }

    public Exception getException() {
        return exception;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
