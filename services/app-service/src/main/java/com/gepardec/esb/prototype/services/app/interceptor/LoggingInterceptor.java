package com.gepardec.esb.prototype.services.app.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This interceptor performs a logging of the called method.
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
@Priority(Interceptor.Priority.APPLICATION)
@Interceptor
@Logging
public class LoggingInterceptor {

    @AroundInvoke
    public Object aroundInvoke(final InvocationContext ic) throws Exception {
        Object result = null;
        final Method method = ic.getMethod();
        final String methodStr = String.format("calling method: %s %s#%s (%s)", method.getReturnType().getSimpleName(),
                                               method.getName(),
                                               ic.getTarget().getClass().getSimpleName(),
                                               Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(",")));
        final Logger log = LoggerFactory.getLogger(ic.getTarget().getClass());
        log.info("Entering method: {} ...", methodStr);

        try {
            return (result = ic.proceed());
        } finally {
            log.info("Left method: {}\nResult: %s", methodStr, result);
        }
    }
}
