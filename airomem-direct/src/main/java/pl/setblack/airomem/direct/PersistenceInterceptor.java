/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.setblack.airomem.direct;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import pl.setblack.airomem.direct.impl.ClassContext;

/**
 *
 * @author jratajsk
 */
@Interceptor
@Persistent
public class PersistenceInterceptor {

    @AroundInvoke
    public Object preparePersistence(InvocationContext ctx) {
        try {
            final ClassContext classContext = new ClassContext(ctx.getTarget());
            return classContext.performTransaction(ctx.getTarget(), ctx.getMethod());

        } catch (Exception ex) {

            throw new RuntimeException(ex);
        }
    }
}
