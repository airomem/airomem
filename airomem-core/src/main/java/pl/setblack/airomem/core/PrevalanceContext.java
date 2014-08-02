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

    PrevalanceContext(Date date) {
        time = date.toInstant();
    }
}
