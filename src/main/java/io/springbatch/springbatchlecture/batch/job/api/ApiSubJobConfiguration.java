package io.springbatch.springbatchlecture.batch.job.api;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApiSubJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final Step apiStep;
    private final JobLauncher jobLauncher;

    @Bean
    public Step jobStep() {
        return stepBuilderFactory.get("jobStep")
                .job(subJob())
                .launcher(jobLauncher)
                .build();
    }

    @Bean
    public Job subJob() {
        return jobBuilderFactory.get("subJob")
                .start(apiStep)
                .build();
    }

}
