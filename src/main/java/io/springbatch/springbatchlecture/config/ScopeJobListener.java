package io.springbatch.springbatchlecture.config;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class ScopeJobListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        jobExecution.getExecutionContext().putString("name", "user1");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

    }
}
