package com.rubberduck.transactionsviewer.presentation.internal.di;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A scoping annotation for objects related to a single product.
 */
@Scope
@Retention(RUNTIME)
public @interface ProductScope {
}
