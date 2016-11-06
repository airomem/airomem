/* Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package pl.setblack.airomem.core;

import java.time.Instant;
import java.util.Date;

/**
 * Context object.
 *
 * May be used to get time of command.
 *
 * @author jratajsk
 */
public class PrevalanceContext {

    /**
     * Use this time object whenewer actual time was needed in command.
     */
    public final Instant time;

    public final boolean safe;

    public PrevalanceContext(final Date date) {
        this( date.toInstant(), false);
    }
    private PrevalanceContext(final Instant time, final boolean safe) {
        this.time = time;
        this.safe = safe;
    }

    public PrevalanceContext safe() {
        return new PrevalanceContext(this.time, true);
    }

    public PrevalanceContext unsafe() {
        return new PrevalanceContext(this.time, false);
    }
}
