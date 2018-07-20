package com.gepardec.esb.prototype.services.client.quartz.jobs;

import com.gepardec.esb.prototype.services.app.rest.client.api.service.app.ReportRestServiceApi;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.contrib.cdi.Traced;
import io.opentracing.log.Fields;
import io.opentracing.tag.Tags;
import org.apache.deltaspike.cdise.api.ContextControl;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/14/18
 */
public class TestRunnerJob implements Job {

    @Inject
    private ContextControl ctxCtrl;
    @Inject
    private ReportRestServiceApi api;
    @Inject
    private Tracer tracer;
    @Inject
    private Logger log;

    public static final AtomicInteger COUNTER = new AtomicInteger(0);

    @Override
    @Traced
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ctxCtrl.startContext(RequestScoped.class);
        final int count = COUNTER.incrementAndGet();
        log.info("Running test job");
        final Scope scope =
                tracer.buildSpan(String.format("test/execute/%d", count))
                      .ignoreActiveSpan()
                      .startActive(true);
        final Span span = scope.span();
        span.setTag("trace.id", span.context().toString().split(":")[0]);
        try {
            api.generate1(1L);
        } catch (Throwable e) {
            log.error("error during test rest call", e);
            Tags.ERROR.set(span, true);
            span.log(new HashMap<String, Object>() {{
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));

                put(Fields.EVENT, "error");
                put(Fields.ERROR_KIND, "Exception");
                put(Fields.ERROR_OBJECT, e);
                put(Fields.STACK, sw.toString());
            }});
        } finally {
            span.finish();
            ctxCtrl.stopContext(RequestScoped.class);
        }
    }
}
