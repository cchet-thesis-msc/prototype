package com.gepardec.esb.prototype.services.client.rest.impl;

import com.gepardec.esb.prototype.services.client.annotation.Logging;
import com.gepardec.esb.prototype.services.client.quartz.jobs.TestRunnerJob;
import com.gepardec.esb.prototype.services.client.rest.api.TestRestService;
import org.apache.deltaspike.scheduler.spi.Scheduler;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/14/18
 */
@ApplicationScoped
@Logging
public class TestRestServiceimpl implements TestRestService {

    @Inject
    private Scheduler scheduler;

    private org.quartz.Scheduler quartzScheduler;

    @PostConstruct
    public void postConstruct() throws Exception {
        quartzScheduler = (org.quartz.Scheduler) scheduler.unwrap(org.quartz.Scheduler.class);
    }

    @Override
    @Counted(name = "started-tests", monotonic = true)
    public String start(final Integer executorCount,
                        final String group) throws SchedulerException {
        for (int i = 0; i < executorCount; i++) {
            quartzScheduler.scheduleJob(JobBuilder.newJob(TestRunnerJob.class)
                                                  .withIdentity(String.format("test-runner-%d", (i + 1)), group)
                                                  .requestRecovery(true)
                                                  .build(),
                                        TriggerBuilder.<TestRunnerJob>newTrigger()
                                                .withSchedule(CronScheduleBuilder.cronSchedule("* */1 * * * ?"))
                                                .startNow()
                                                .build());
        }

        return String.format("%d executors started", executorCount);
    }

    @Override
    @Counted(name = "stopped-tests", monotonic = true)
    public String stop(final String group) throws SchedulerException {
        quartzScheduler.deleteJobs(new LinkedList<>(quartzScheduler.getJobKeys(GroupMatcher.groupEquals(group))));
        return "Send stop message to test executor";
    }

    @Override
    @Counted(name = "restarted-tests", monotonic = true)
    @Timed(name = "rest-client-method-calls", unit = MetricUnits.MILLISECONDS)
    public String restart(final Integer executorCount,
                          final String group) throws SchedulerException {
        return stop(group) + " --> " + start(executorCount, group);
    }
}
