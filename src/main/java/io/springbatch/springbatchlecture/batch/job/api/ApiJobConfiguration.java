package io.springbatch.springbatchlecture.batch.job.api;

import io.springbatch.springbatchlecture.batch.listener.JobListener;
import io.springbatch.springbatchlecture.batch.tasklet.ApiEndTasklet;
import io.springbatch.springbatchlecture.batch.tasklet.ApiStartTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApiJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ApiStartTasklet apiStartTasklet;
    private final ApiEndTasklet apiEndTasklet;
    private final Step jobStep;

    @Bean
    public Job apiJob() {
        return jobBuilderFactory.get("apiJob")
                .listener(new JobListener())
                .start(apiStepBefore())
                .next(jobStep)
                .next(apiStepAfter())
                .build();
    }

    @Bean
    public Step apiStepBefore() {
        return stepBuilderFactory.get("apiStepBefore")
                .tasklet(apiStartTasklet)
                .build();
    }

    @Bean
    public Step apiStepAfter() {
        return stepBuilderFactory.get("apiStepAfter")
                .tasklet(apiEndTasklet)
                .build();
    }


}
