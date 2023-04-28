package io.springbatch.springbatchlecture.config;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import java.util.concurrent.atomic.AtomicInteger;

public class CustomDecider implements JobExecutionDecider {

    private AtomicInteger atomicCount = new AtomicInteger(0);

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        int count = this.atomicCount.decrementAndGet();

        if(count % 2 == 0)
            return new FlowExecutionStatus("EVEN");

        return new FlowExecutionStatus("ODD");
    }
}
