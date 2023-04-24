package io.springbatch.springbatchlecture.config;

import io.springbatch.springbatchlecture.tasklet.CustomTasklet;
import io.springbatch.springbatchlecture.tasklet.ExecutionContextTaskletAfter;
import io.springbatch.springbatchlecture.tasklet.ExecutionContextTaskletBefore;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JobInstanceConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobExecutionListener jobRepositoryListener;

    @Bean
    public Job HelloJob() {
        return jobBuilderFactory.get("helloJob")
                .start(helloStep1())
                .next(failStep())
                .next(helloStep2())
                .listener(jobRepositoryListener)
                .build();
    }

    @Bean
    public Step helloStep1() {
        return stepBuilderFactory.get("helloStep")
                .tasklet(new CustomTasklet())
                .build();
    }

    @Bean
    public Step failStep() {
        return stepBuilderFactory.get("helloStep")
                .tasklet(((stepContribution, chunkContext) -> {
                    throw new RuntimeException("step failed");
                }))
                .build();
    }

    @Bean
    public Step helloStep2() {
        return stepBuilderFactory.get("helloStep")
                .tasklet(((stepContribution, chunkContext) -> {

                    System.out.println("========================");
                    System.out.println(" >> Hello Violet Beach!!");
                    System.out.println("========================");

                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Job flowJob() {
        return jobBuilderFactory.get("flowJob")
                .start(flow())
                .end()
                .build();
    }

    @Bean
    public Flow flow() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow");
        flowBuilder.start(helloStep1())
                .next(helloStep2())
                .end();

        return flowBuilder.build();
    }

    @Bean
    public Step executionContextStepBefore(ExecutionContextTaskletBefore before) {
        return stepBuilderFactory.get("helloStep")
                .tasklet(before)
                .build();
    }

    @Bean
    public Step executionContextStepBefore(ExecutionContextTaskletAfter after) {
        return stepBuilderFactory.get("helloStep")
                .tasklet(after)
                .build();
    }

}
