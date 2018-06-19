package com.gepardec.esb.prototype.services.client.configuration;

import com.gepardec.esb.prototype.services.app.rest.client.api.service.app.ReportRestServiceApi;
import com.gepardec.esb.prototype.services.client.rest.client.filter.AppendOAuthFilter;
import io.opentracing.contrib.jaxrs2.client.ClientTracingFeature;
import io.opentracing.contrib.jaxrs2.client.ClientTracingFilter;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.client.jaxrs.ProxyBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represents the rest client configuration, which injects config parameter and creates/caches the clients
 * for the {@link com.gepardec.esb.prototype.services.client.rest.client.proxy.PartialRestClientProxyBean} which specifies
 * the retry behavior of a rest client.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/09/18
 */
@ApplicationScoped
public class RestClientConfiguration {

    @Inject
    private AppendOAuthFilter appendOAuthFilter;

    @Inject
    @ConfigProperty(name = "service.app.base-url")
    private String baseUrlAppService;

    private static final Map<Class, Object> cache = new ConcurrentHashMap<>();
    private static final Map<Class, String> typeToBaseUrlCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void postConstruct() {
        // Register all rest clients
        typeToBaseUrlCache.putIfAbsent(ReportRestServiceApi.class, baseUrlAppService);
    }

    public <T> T getOrCreateProxy(Class<T> clazz) {
        T proxy = (T) cache.getOrDefault(clazz,
                                         ProxyBuilder.builder(clazz,
                                                              ResteasyClientBuilder.newClient()
                                                                                   .register(ClientTracingFeature.class)
                                                                                   .register(appendOAuthFilter)
                                                                                   .target(Objects.requireNonNull(typeToBaseUrlCache.get(clazz),
                                                                                                                  String.format("Rest-Client of type '%s' has no registered baseUrl", clazz))))
                                                     .defaultConsumes(MediaType.TEXT_PLAIN)
                                                     .build());
        cache.putIfAbsent(clazz, proxy);

        return proxy;
    }
}
