package io.springbatch.springbatchlecture.scheduler;

import org.quartz.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.HashMap;

import static org.quartz.JobBuilder.newJob;

public abstract class JobRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        execute(args);
    }

    protected abstract void execute(ApplicationArguments args);

    public Trigger buildJobTrigger(String scheduleExp) {
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp)).build();
    }

    public JobDetail buildJobDetail(Class<? extends Job> job, String name, String group) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(new HashMap<>());

        return newJob(job).withIdentity(name, group)
                .usingJobData(jobDataMap)
                .build();
    }

}
