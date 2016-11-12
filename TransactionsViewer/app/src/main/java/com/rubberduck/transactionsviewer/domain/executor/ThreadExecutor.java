package com.rubberduck.transactionsviewer.domain.executor;

/**
 * An executor which will execute use cases on a background thread.
 * Executor implementation will vary depending on platform and frameworks.
 */
public interface ThreadExecutor {

    /**
     * This method should call the use case's execute method to start it. This should be called
     * on a background thread as use cases might do lengthy operations.
     *
     * @param runnable The thread to be executed.
     */
    void execute(final Runnable runnable);
}