package com.gepardec.esb.prototype.services.app.configuration;

import com.gepardec.esb.prototype.services.app.interceptor.ApplyRestClientRetryProxy;
import com.gepardec.esb.prototype.services.app.interceptor.Logging;
import com.gepardec.esb.prototype.services.app.rest.api.ReportRestService;
import org.jboss.resteasy.client.jaxrs.ProxyBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Qualifier;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.lang.annotation.*;
import java.util.concurrent.Callable;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
@ApplicationScoped
public class RestClientConfiguration {

    @Inherited
    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
    public @interface RestClient {
    }

    /**
     * Backing Interceptor will wrap client in a proxy which calls retry invoker util bean, which declares retry behavior.
     *
     * @return the produced rest client
     * @see com.gepardec.esb.prototype.services.app.util.RetryInvoker#invokeRestClient(Callable)
     * @see com.gepardec.esb.prototype.services.app.util.RestClientRetryProxy
     */
    @Produces
    @RequestScoped
    @RestClient
    @Logging
    @ApplyRestClientRetryProxy
    ReportRestService createReportRestServiceClient() {
        return ProxyBuilder.builder(ReportRestService.class, ClientBuilder.newClient().target("http://localhost:8080/rest-api"))
                           .defaultConsumes(MediaType.APPLICATION_JSON)
                           .build();
    }
}
