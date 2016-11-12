package com.rubberduck.transactionsviewer.domain.usecase;

import com.rubberduck.transactionsviewer.domain.exception.ErrorBundle;

/**
 * Base class for all {@link UseCase} callbacks.
 *
 * @param <T> Type of data the callback is returned on success.
 */
public interface UseCaseCallback<T> {

    public abstract void onSuccess(T t);

    public abstract void onFailure(ErrorBundle errorBundle);

}
