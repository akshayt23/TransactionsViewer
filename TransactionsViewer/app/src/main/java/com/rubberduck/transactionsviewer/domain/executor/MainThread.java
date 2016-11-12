package com.rubberduck.transactionsviewer.domain.executor;

/**
 * An abstraction for the main thread of the application.
 * Useful for running operations on the main (UI) thread.
 */
public interface MainThread {

    /**
     * Post runnable to the main UI thread.
     *
     * @param runnable The runnable to run.
     */
    void post(final Runnable runnable);
}