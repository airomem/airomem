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
          
            return ctx.proceed();
        } catch (Exception ex) {

            throw new RuntimeException(ex);
        }     
    }
}
