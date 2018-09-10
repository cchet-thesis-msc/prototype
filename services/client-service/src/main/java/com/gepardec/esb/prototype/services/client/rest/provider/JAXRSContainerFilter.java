package com.gepardec.esb.prototype.services.client.rest.provider;

import com.gepardec.esb.prototype.services.client.annotation.Logging;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import org.jboss.logging.Logger;
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

        @Inject
        private Instance<Scope> scope;

        @Override
        public void filter(ContainerRequestContext requestContext) throws IOException {
            try {
                Span span = scope.get().span();
                log.infof("Extracting MDC transaction id from span: %s", span.context().toString());
                // Get current span context
                final String tracingId = span.context().toString().split(":")[0];
                // Uber SpanContext implementation does format the id like this 'aaa:ffff:0:1', so here we are implementation dependent,
                // because the io.opentrace spec does not expose any id
                log.infof("Setting MDC transaction id: %s", tracingId);
                MDC.put(MDC_TX_ID, tracingId);
                span.setTag("trace.id", tracingId);
                scope.get().span().setTag(MDC_TX_ID, tracingId);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
