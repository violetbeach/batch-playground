package io.springbatch.springbatchlecture.config;

import io.springbatch.springbatchlecture.tasklet.ExecutionContextTaskletAfter;
import io.springbatch.springbatchlecture.tasklet.ExecutionContextTaskletBefore;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ExecutionContextJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ExecutionContextTaskletBefore before;
    private final ExecutionContextTaskletAfter after;

    @Bean
    public Job HelloJob() {
        return jobBuilderFactory.get("executionContextJob")
                .start(executionContextStepBefore())
                .next(executionContextStepAfter())
                .build();
    }

    @Bean
    public Step executionContextStepBefore() {
        return stepBuilderFactory.get("helloStep")
                .tasklet(before)
                .build();
    }

    @Bean
    public Step executionContextStepAfter() {
        return stepBuilderFactory.get("helloStep")
                .tasklet(after)
                .build();
    }

}
