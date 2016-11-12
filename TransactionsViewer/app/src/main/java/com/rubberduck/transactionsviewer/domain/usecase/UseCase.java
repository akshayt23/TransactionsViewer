package com.rubberduck.transactionsviewer.domain.usecase;

import com.rubberduck.transactionsviewer.domain.executor.MainThread;
import com.rubberduck.transactionsviewer.domain.executor.ThreadExecutor;

/**
 * Base class for all use cases. Executes given requests on a background thread and returns results
 * back to the main thread.
 *
 * @param <T> Type of data the use case returns as a result.
 */
abstract class UseCase<T> {

    private final ThreadExecutor threadExecutor;
    protected final MainThread mainThread;

    /**
     * @param threadExecutor A threadExecutor which it uses to execute itself on a background thread.
     * @param mainThread     Typically the main application / UI thread on which the results can be dispatched.
     */
    UseCase(ThreadExecutor threadExecutor, MainThread mainThread) {
        this.threadExecutor = threadExecutor;
        this.mainThread = mainThread;
    }

    /**
     * Starts the execution of the current use case.
     */
    public void execute(final UseCaseCallback<T> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("UseCaseCallback cannot be null.");
        }

        threadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                performUseCase(callback);
            }
        });
    }

    /**
     * This method contains the actual implementation details of the use case.
     * It contains the business logic for the operation to be performed.
     * The classes implementing this method should dispatch results to the callback on the {@link MainThread}.
     *
     * @param callback The callback to which the results should be dispatched
     */
    abstract void performUseCase(UseCaseCallback<T> callback);
}
