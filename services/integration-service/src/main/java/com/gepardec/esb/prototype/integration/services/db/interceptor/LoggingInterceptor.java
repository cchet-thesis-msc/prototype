package com.gepardec.esb.prototype.integration.services.db.interceptor;

import com.gepardec.esb.prototype.integration.services.db.annotation.Logging;
import org.jboss.logging.Logger;
import org.jboss.logging.MDC;

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
        if (!Logging.MDCConfig.DEFAULT.equals(mdcConfig)) {
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
        final Logger log = Logger.getLogger(ic.getTarget().getClass());
        log.infof("Entering method: %s ...", methodStr);

        boolean error = false;
        try {
            result = ic.proceed();
        } catch (Throwable t) {
            error = true;
            log.infof("Left method: %s -> exception: %s | message: %s", methodStr, t.getClass(), t.getMessage());
            throw t;
        } finally {
            if (!Logging.MDCConfig.DEFAULT.equals(mdcConfig)) {
                MDC.remove(mdcConfig.key);
            }
            if (!error) {
                final String finalResult = (voidReturnType) ? "void"
                        : (!ann.skipResult() && result != null) ? result.toString()
                        : (ann.skipResult() && result != null) ? "skipped" : "null";
                log.infof("Left method: %s -> %s", methodStr, finalResult);
            }
        }

        return result;
    }
}
