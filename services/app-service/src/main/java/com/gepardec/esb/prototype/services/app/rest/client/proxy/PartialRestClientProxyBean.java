package com.gepardec.esb.prototype.services.app.rest.client.proxy;

import com.gepardec.esb.prototype.services.app.interceptor.Logging;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.jboss.resteasy.client.jaxrs.ProxyBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

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

    private static final Map<Class, Object> cache = new ConcurrentHashMap<>();

    @Override
    @Logging(mdcConfig = Logging.MDCConfig.GROUP_REST_CLIENT)
    @Retry(delay = 100L, maxRetries = 5, retryOn = {ClientErrorException.class})
    public Object invoke(Object proxy,
                         Method method,
                         Object[] args) throws Throwable {
        try {
            final PartialRetryRestProxy ann = Objects.requireNonNull(proxy.getClass().getAnnotation(PartialRetryRestProxy.class), "We should see the annotation here. Are you missing @Inherited ???");
            final Class<?> clazz = Objects.requireNonNull(ann.interfaceClass(), "Annotation must provide jax-rs class reference");
            return method.invoke(getOrCreateProxy(clazz), args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    public <T> T getOrCreateProxy(Class<T> clazz) {
        T proxy = (T) cache.getOrDefault(clazz,
                                         ProxyBuilder.builder(clazz, ClientBuilder.newClient().target("http://localhost:8080/rest-api"))
                                                     .defaultConsumes(MediaType.APPLICATION_JSON)
                                                     .build());
        cache.putIfAbsent(clazz, proxy);

        return proxy;
    }
}
