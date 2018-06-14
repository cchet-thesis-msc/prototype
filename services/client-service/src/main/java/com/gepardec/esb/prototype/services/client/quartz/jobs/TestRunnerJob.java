package com.gepardec.esb.prototype.services.client.quartz.jobs;

import com.gepardec.esb.prototype.services.app.rest.client.api.service.app.ReportRestServiceApi;
import com.gepardec.esb.prototype.services.client.annotation.Logging;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.inject.Inject;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/14/18
 */
public class TestRunnerJob implements Job {

    @Inject
    private ReportRestServiceApi api;

    @Override
    @Logging
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        api.generate1(1L);
    }
}
