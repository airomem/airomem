/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core;

/**
 * Indicated problem appeared during restore.
 *
 * Mostly it happens when classes changed so much they cannot be restored from
 * stream. Helpful might be serialVersionUID.
 *
 * If You work on test sysem You can simply delete Prevayler folder and this
 * should clean all outdated data.
 *
 */
public class RestoreException extends RuntimeException {

    public RestoreException(Throwable cause) {
        super(cause);
    }

}
