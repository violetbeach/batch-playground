package io.springbatch.springbatchlecture.config;

import org.springframework.batch.item.*;

import java.util.List;

public class CustomItemWriter implements ItemStreamWriter {

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        System.out.println("");
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        System.out.println("update");
    }

    @Override
    public void close() throws ItemStreamException {
        System.out.println("close");
    }

    @Override
    public void write(List list) throws Exception {
        System.out.println("update");
    }
}
