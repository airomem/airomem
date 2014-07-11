/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.setblack.airomem.core;

import java.time.Instant;
import java.util.Date;


/**
 *
 * @author jratajsk
 */
public class PrevalanceContext {
    public final Instant time;

    PrevalanceContext(Date date) {
        time = date.toInstant();
    }
}
