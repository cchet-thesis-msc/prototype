package com.gepardec.esb.prototype.services.client.quartz.jobs;

import com.gepardec.esb.prototype.services.app.rest.client.api.service.app.ReportRestServiceApi;
import com.gepardec.esb.prototype.services.client.annotation.Logging;
import io.jaegertracing.metrics.Tag;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.log.Fields;
import io.opentracing.tag.Tags;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Random;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/14/18
 */
public class TestRunnerJob implements Job {

    @Inject
    private ReportRestServiceApi api;
    @Inject
    private Instance<Scope> scope;

    @Override
    @Logging
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            api.generate1(1L);
        } catch (Throwable e) {
            final Span span = scope.get().span();
            final String tracingId = span.context().toString().split(":")[0];
            Tags.ERROR.set(span, true);
            span.log(new HashMap<String, Object>() {{
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));

                put(Fields.EVENT, "error");
                put(Fields.ERROR_KIND, "Exception");
                put(Fields.ERROR_OBJECT, e);
                put("trace.id", tracingId);
                put(Fields.STACK, sw.toString());
            }});
        }
    }
}
