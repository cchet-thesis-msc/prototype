package com.gepardec.esb.prototype.services.app.interceptor;

import com.gepardec.esb.prototype.services.app.annotation.Logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This interceptor performs generic logging of a called method and supports MDC context settings.
 *
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
        Logging ann = ic.getMethod().getAnnotation(Logging.class);
        if (ann == null) {
            ann = Objects.requireNonNull(ic.getTarget().getClass().getAnnotation(Logging.class), "Annotation Logging not present either on method or type. Are missing @Inherited maybe ???");
        }
        final Logging.MDCConfig mdcConfig = Objects.requireNonNull(ann.mdcConfig(), "MDCConfig enum not present. Should not be null");

        // Prepare MDC context
        if (!Logging.MDCConfig.EMPTY.equals(mdcConfig)) {
            MDC.put(mdcConfig.key, mdcConfig.value);
        }

        final Method method = ic.getMethod();
        final boolean voidReturnType = method.getReturnType().getSimpleName().equalsIgnoreCase("void");
        final String methodStr = String.format("%s %s.%s (%s)",
                                               method.getReturnType().getSimpleName(),
                                               (ic.getTarget().getClass().getName().endsWith("Proxy$_$$_WeldSubclass"))
                                                       ? ic.getTarget().getClass().getSuperclass().getSimpleName()
                                                       : ic.getTarget().getClass().getSimpleName(),
                                               method.getName(),
                                               Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(", ")));
        final Logger log = LoggerFactory.getLogger(ic.getTarget().getClass());
        log.info("Entering method: {} ...", methodStr);

        try {
            return (result = ic.proceed());
        } finally {
            if (!Logging.MDCConfig.EMPTY.equals(mdcConfig)) {
                MDC.remove(mdcConfig.key);
            }
            final String finalResult = (voidReturnType) ? "void"
                    : (!ann.skipResult() && result != null) ? result.toString()
                    : (ann.skipResult() && result != null) ? "skipped" : "null";
            log.info("Left method: {} -> {}", methodStr, finalResult);
        }
    }
}
