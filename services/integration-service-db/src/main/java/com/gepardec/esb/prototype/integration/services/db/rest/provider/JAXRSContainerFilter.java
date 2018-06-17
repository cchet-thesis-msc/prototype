package com.gepardec.esb.prototype.integration.services.db.rest.provider;

import com.gepardec.esb.prototype.integration.services.db.annotation.Logging;
import io.opentracing.Scope;
import io.opentracing.SpanContext;
import org.slf4j.Logger;
import org.slf4j.MDC;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Collections;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 4/30/18
 */
public class JAXRSContainerFilter {

    private static final String MDC_TX_ID = "trace.id";

    static {
        MDC.put(MDC_TX_ID, "undefined");
    }

    @Provider
    @Logging
    public static class CorsResponseFilter implements ContainerResponseFilter {

        @Override
        public void filter(ContainerRequestContext requestContext,
                           ContainerResponseContext responseContext) throws IOException {
            responseContext.getHeaders().put(
                    "Access-Control-Allow-Origin", Collections.singletonList("*"));
        }
    }

    @Provider
    @Logging
    public static class MDCContainerRequestFilter implements ContainerRequestFilter {
        @Inject
        private Logger log;
        // if directly injected, then we get the first instance and no new one anymore,
        // because the filter gets instantiated only once and the bean is create with @Dependent scope.
        @Inject
        private Instance<SpanContext> spanContextInstance;
        @Inject
        private Instance<Scope> scopeInstance;

        @Override
        public void filter(ContainerRequestContext requestContext) throws IOException {
            try {
                // Get current span context
                final String tracingId = spanContextInstance.get().toString().split(":")[0];
                // Uber SpanContext implementation does format the id like this 'aaa:ffff:0:1', so here we are implementation dependent,
                // because the io.opentrace spec does not expose any id
                scopeInstance.get().span().setTag(MDC_TX_ID, true);
                log.info("Setting MDC transaction id");
                MDC.put(MDC_TX_ID, tracingId);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
