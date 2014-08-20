/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.setblack.airomem.direct;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import pl.setblack.airomem.direct.impl.ClassContext;
import pl.setblack.badass.Politician;

/**
 *
 * @author jratajsk
 */
@Interceptor
@Persistent
public class PersistenceInterceptor {

    private static final ThreadLocal<Boolean> MARKER = new ThreadLocal<>();

    @AroundInvoke
    public Object preparePersistence(InvocationContext ctx) {
        try {
            if (!Boolean.TRUE.equals(MARKER.get())) {
                final ClassContext classContext = new ClassContext(ctx.getTarget());
                return classContext.performTransaction(ctx.getTarget(), ctx.getMethod());

            } else {
                return Politician.beatAroundTheBush(() -> ctx.proceed());
            }
        } finally {
            MARKER.remove();
        }
    }

    public static void setMarker() {
        MARKER.set(Boolean.TRUE);
    }
}
