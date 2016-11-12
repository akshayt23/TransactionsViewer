package com.rubberduck.transactionsviewer.data.executor;

import com.rubberduck.transactionsviewer.domain.executor.ThreadExecutor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JobExecutor implements ThreadExecutor {

    private static final int CORE_POOL_SIZE = 3;
    private static final int MAX_POOL_SIZE = 5;

    // Sets the amount of time an idle thread waits before terminating
    private static final int KEEP_ALIVE_TIME = 10;
    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private final BlockingQueue<Runnable> workQueue;
    private final ThreadPoolExecutor threadPoolExecutor;

    @Inject
    JobExecutor() {
        workQueue = new LinkedBlockingQueue<>();
        threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,
                                                    MAX_POOL_SIZE,
                                                    KEEP_ALIVE_TIME,
                                                    KEEP_ALIVE_TIME_UNIT,
                                                    workQueue);
    }

    @Override
    public void execute(final Runnable runnable) {
        if (runnable == null) {
            throw new IllegalArgumentException("Runnable to be executed cannot be null.");
        }

        threadPoolExecutor.execute(runnable);
    }
}
