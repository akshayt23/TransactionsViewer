package com.rubberduck.transactionsviewer.presentation.view;

import android.os.Handler;
import android.os.Looper;

import com.rubberduck.transactionsviewer.domain.executor.MainThread;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UIThread implements MainThread {

    private Handler handler;

    @Inject
    UIThread() {
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        handler.post(runnable);
    }
}
