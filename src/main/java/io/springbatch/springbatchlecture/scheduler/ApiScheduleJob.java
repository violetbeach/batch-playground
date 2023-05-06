package io.springbatch.springbatchlecture.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class ApiScheduleJob extends QuartzJobBean {

    private final Job apiJob;
    private final JobLauncher jobLauncher;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        JobParameters jobParams = new JobParametersBuilder()
                .addLong("id", new Date().getTime())
                .toJobParameters();
        jobLauncher.run(apiJob, jobParams);
    }
}
