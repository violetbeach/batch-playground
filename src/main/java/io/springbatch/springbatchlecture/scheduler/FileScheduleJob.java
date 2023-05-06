package io.springbatch.springbatchlecture.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FileScheduleJob extends QuartzJobBean {

    private final Job fileJob;
    private final JobLauncher jobLauncher;

    private final JobExplorer jobExplorer;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        String requestDate = jobExecutionContext.getJobDetail().getJobDataMap().get("requestDate").toString();

        int jobInstanceCount = jobExplorer.getJobInstanceCount(fileJob.getName());
        List<JobInstance> jobInstances = jobExplorer.getJobInstances(fileJob.getName(), 0, jobInstanceCount);
        for (JobInstance jobInstance : jobInstances) {
            List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
            boolean match = jobExecutions
                    .stream()
                    .anyMatch(jobExecution -> Objects.equals(jobExecution.getJobParameters().getString("requestDate"), requestDate));

            if(match)
                throw new JobExecutionException(requestDate + " already exists");
        }

        JobParameters jobParams = new JobParametersBuilder()
                .addLong("id", new Date().getTime())
                .addString("requestDate", requestDate)
                .toJobParameters();

        jobLauncher.run(fileJob, jobParams);
    }
}
