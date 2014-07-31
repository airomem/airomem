/* Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package pl.setblack.airomem.core;

/**
 * Extra helper method check if PrevalanceContext was initialized.
 *
 * @author jarekr
 */
public class WriteChecker {

    private static final ThreadLocal<PrevalanceContext> CONTEXT_STORE = new ThreadLocal<>();

    public static void setContext(final PrevalanceContext ctx) {
        assert !hasPrevalanceContext() : "Context was initialized before";
        CONTEXT_STORE.set(ctx);
    }

    public static PrevalanceContext getContext() {
        return CONTEXT_STORE.get();
    }

    public static void clearContext() {
        assert hasPrevalanceContext() : "Context has to be initialized first";
        CONTEXT_STORE.remove();
    }

    public static boolean hasPrevalanceContext() {
        return CONTEXT_STORE.get() != null;
    }

}
