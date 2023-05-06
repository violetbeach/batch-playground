package io.springbatch.springbatchlecture.scheduler;

import lombok.RequiredArgsConstructor;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileJobRunner extends JobRunner {

    private final Scheduler scheduler;

    @Override
    protected void execute(ApplicationArguments args) {
        JobDetail jobDetail = buildJobDetail(FileScheduleJob.class, "fileJob","batch");
        Trigger trigger = buildJobTrigger("0 * * * * ?");

        String[] sourceArgs = args.getSourceArgs();
        jobDetail.getJobDataMap().put("requestDate", sourceArgs[0]);

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            System.out.println(e.getMessage());
        }
    }

}
