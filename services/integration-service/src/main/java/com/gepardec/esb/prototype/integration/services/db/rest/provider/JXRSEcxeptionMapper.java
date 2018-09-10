package com.gepardec.esb.prototype.integration.services.db.rest.provider;

import com.gepardec.esb.prototype.integration.services.db.annotation.Logging;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.log.Fields;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;

import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 7/17/2018
 */
public class JXRSEcxeptionMapper {

    @Provider
    @Logging
    public static class ServerExceptionMapper implements ExceptionMapper<ServerErrorException> {

        @Override
        public Response toResponse(ServerErrorException exception) {
            markSpanError(exception);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .header("Content-Type", MediaType.TEXT_PLAIN)
                           .entity(String.format("Server error occurred. exception: %s, msg: %s",
                                                 exception.getClass().getName(),
                                                 exception.getMessage())).build();
        }
    }

    @Provider
    @Logging
    public static class TimeoutExceptionMapper implements ExceptionMapper<TimeoutException> {

        @Override
        public Response toResponse(TimeoutException exception) {
            markSpanError(exception);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .header("Content-Type", MediaType.TEXT_PLAIN)
                           .entity(String.format("Execution took too long. exception: %s, msg: %s",
                                                 exception.getClass().getName(),
                                                 exception.getMessage())).build();
        }
    }

    public static final void markSpanError(Throwable exception) {
        final Scope scope = GlobalTracer.get().scopeManager().active();
        if (scope != null) {
            final Span span = scope.span();
            final String tracingId = span.context().toString().split(":")[0];
            Tags.ERROR.set(span, true);
            span.log(new HashMap<String, Object>() {{
                StringWriter sw = new StringWriter();
                exception.printStackTrace(new PrintWriter(sw));
                put(Fields.EVENT, "error");
                put(Fields.ERROR_KIND, "Exception");
                put(Fields.ERROR_OBJECT, exception);
                put("trace.id", tracingId);
                put(Fields.STACK, sw.toString());
            }});
        }
    }
}
