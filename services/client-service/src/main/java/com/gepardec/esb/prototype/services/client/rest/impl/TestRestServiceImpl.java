package com.gepardec.esb.prototype.services.client.rest.impl;

import com.gepardec.esb.prototype.services.client.annotation.Logging;
import com.gepardec.esb.prototype.services.client.quartz.jobs.TestRunnerJob;
import com.gepardec.esb.prototype.services.client.rest.api.TestRestService;
import org.apache.deltaspike.scheduler.spi.Scheduler;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Random;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/14/18
 */
@ApplicationScoped
@Logging
public class TestRestServiceImpl implements TestRestService {

    @Inject
    private Scheduler scheduler;

    private org.quartz.Scheduler quartzScheduler;

    private static final Random rand = new Random(System.currentTimeMillis());

    @PostConstruct
    public void postConstruct() throws Exception {
        quartzScheduler = (org.quartz.Scheduler) scheduler.unwrap(org.quartz.Scheduler.class);
        quartzScheduler.standby();
    }

    @Override
    @Counted(name = "started-tests", monotonic = true)
    public String start(final Integer executorCount,
                        Integer intervalSeconds,
                        Integer waitIntervalMillis,
                        final String group) throws SchedulerException {
        intervalSeconds = (intervalSeconds == null) ? 5 : intervalSeconds;
        waitIntervalMillis = (waitIntervalMillis == null) ? 500 : waitIntervalMillis;

        quartzScheduler.start();

        for (int i = 0; i < executorCount; i++) {
            try {
                Thread.sleep(rand.nextInt(waitIntervalMillis) + 1);
            } catch (InterruptedException e) {
                throw new IllegalStateException("sleep got interrupted", e);
            }
            quartzScheduler.scheduleJob(JobBuilder.newJob(TestRunnerJob.class)
                                                  .withIdentity(String.format("test-runner-%d", (i + 1)), group)
                                                  .requestRecovery(true)
                                                  .build(),
                                        TriggerBuilder.<TestRunnerJob>newTrigger()
                                                .withSchedule(CronScheduleBuilder.cronSchedule(String.format("0/%d * * * * ?", intervalSeconds)))
                                                .startNow()
                                                .build());
        }


        return String.format("%d executors started", executorCount);
    }

    @Override
    @Counted(name = "stopped-tests", monotonic = true)
    public String stop(final String group) throws SchedulerException {
        quartzScheduler.clear();
        quartzScheduler.standby();
        TestRunnerJob.COUNTER.set(0);

        return "Send stop message to test executor";
    }

    @Override
    @Counted(name = "restarted-tests", monotonic = true)
    @Timed(name = "rest-client-method-calls", unit = MetricUnits.MILLISECONDS)
    public String restart(final Integer executorCount,
                          final String group) throws SchedulerException {
        return stop(group) + " --> " + start(executorCount, null, null, group);
    }
}
