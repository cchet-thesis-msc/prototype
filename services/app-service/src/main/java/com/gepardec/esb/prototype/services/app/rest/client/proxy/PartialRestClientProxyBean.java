package com.gepardec.esb.prototype.services.app.rest.client.proxy;

import com.gepardec.esb.prototype.services.app.annotation.Logging;
import com.gepardec.esb.prototype.services.app.annotation.PartialRetryRestProxy;
import com.gepardec.esb.prototype.services.app.configuration.RestClientConfiguration;
import com.gepardec.esb.prototype.services.app.rest.exception.RestClientExcepton;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metric;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * This class implements the {@link PartialRetryRestProxy} annotated JAX-RS interface and delegates to the
 * resteasy client proxy, but applies retries behaviours as intended for all rest clients.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
@ApplicationScoped
@PartialRetryRestProxy
public class PartialRestClientProxyBean implements InvocationHandler {

    @Inject
    @Metric(name = "rest-client-method-failed")
    private Counter counter;

    @Inject
    private RestClientConfiguration restClientConfiguration;

    // getMyProp = MY_PROP
    @Override
    @Logging(mdcConfig = Logging.MDCConfig.GROUP_REST_CLIENT)
//    @Retry(delay = 100L, maxRetries = 3, retryOn = {RestClientExcepton.class})
    @Counted(name = "rest-client-method-calls", monotonic = true, reusable = true)
    @Timed(name = "duration-rest-client-method-calls", unit = MetricUnits.SECONDS, reusable = true)
//    @Timeout(value = 5L, unit = ChronoUnit.SECONDS)
    public Object invoke(Object proxy,
                         Method method,
                         Object[] args) throws Throwable {
        try {
            final PartialRetryRestProxy ann = Objects.requireNonNull(proxy.getClass().getAnnotation(PartialRetryRestProxy.class), "We should see the annotation here. Are you missing @Inherited ???");
            final Class<?> clazz = Objects.requireNonNull(ann.interfaceClass(), "Annotation must provide jax-rs class reference");
            return method.invoke(restClientConfiguration.getOrCreateProxy(clazz), args);
        } catch (InvocationTargetException e) {
            counter.inc();
            throw new RestClientExcepton(e.getTargetException());
        } catch (Throwable e) {
            counter.inc();
            throw new RestClientExcepton(e);
        }
    }
}
