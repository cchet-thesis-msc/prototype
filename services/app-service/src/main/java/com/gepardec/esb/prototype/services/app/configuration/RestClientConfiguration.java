package com.gepardec.esb.prototype.services.app.configuration;

import com.gepardec.esb.prototype.services.app.rest.client.api.integration.database.ReportRestServiceApi;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.client.jaxrs.ProxyBuilder;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represents the rest client configuration, which injects config parameter and creates/caches the clients
 * for the {@link com.gepardec.esb.prototype.services.app.rest.client.proxy.PartialRestClientProxyBean} which specifies
 * the retry behavior of a rest client.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/09/18
 */
@ApplicationScoped
public class RestClientConfiguration {

    @Inject
    @ConfigProperty(name = "service.db.base-url")
    private String baseUrlIntegrationDb;

    private static final Map<Class, Object> cache = new ConcurrentHashMap<>();
    private static final Map<Class, String> typeToBaseUrlCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void postConstruct() {
        // Register all rest clients
        typeToBaseUrlCache.putIfAbsent(ReportRestServiceApi.class, baseUrlIntegrationDb);
    }


    public <T> T getOrCreateProxy(Class<T> clazz) {
        T proxy = (T) cache.getOrDefault(clazz,
                                         ProxyBuilder.builder(clazz,
                                                              ClientBuilder.newClient()
                                                                           .target(Objects.requireNonNull(typeToBaseUrlCache.get(clazz),
                                                                                                          String.format("Rest-Client of type '%s' has no registered baseUrl", clazz))))
                                                     .defaultConsumes(MediaType.APPLICATION_JSON)
                                                     .build());
        cache.putIfAbsent(clazz, proxy);

        return proxy;
    }
}
