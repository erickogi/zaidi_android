package com.dev.zaidi;

import timber.log.Timber;

class NotLoggingTree extends Timber.Tree {
    @Override
    protected void log(final int priority, final String tag, final String message, final Throwable throwable) {
    }
}