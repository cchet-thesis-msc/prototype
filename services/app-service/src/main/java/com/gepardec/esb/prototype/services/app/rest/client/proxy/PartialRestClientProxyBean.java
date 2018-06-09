package com.gepardec.esb.prototype.services.app.rest.client.proxy;

import com.gepardec.esb.prototype.services.app.annotation.PartialRetryRestProxy;
import com.gepardec.esb.prototype.services.app.configuration.RestClientConfiguration;
import com.gepardec.esb.prototype.services.app.annotation.Logging;
import org.eclipse.microprofile.faulttolerance.Retry;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    private RestClientConfiguration restClientConfiguration;

    @Override
    @Logging(mdcConfig = Logging.MDCConfig.GROUP_REST_CLIENT)
    @Retry(delay = 100L, maxRetries = 5, retryOn = {WebApplicationException.class, ProcessingException.class})
    public Object invoke(Object proxy,
                         Method method,
                         Object[] args) throws Throwable {
        try {
            final PartialRetryRestProxy ann = Objects.requireNonNull(proxy.getClass().getAnnotation(PartialRetryRestProxy.class), "We should see the annotation here. Are you missing @Inherited ???");
            final Class<?> clazz = Objects.requireNonNull(ann.interfaceClass(), "Annotation must provide jax-rs class reference");
            return method.invoke(restClientConfiguration.getOrCreateProxy(clazz), args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
