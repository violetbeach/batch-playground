package io.springbatch.springbatchlecture.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class JdbcCursorJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final int chunkSize = 10;
    private final DataSource dataSource;

    @Bean
    public Job batchJob() {
        return jobBuilderFactory.get("jdbcCursorJob")
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Customer, Customer>chunk(chunkSize)
                .reader(cursorItemReader())
                .writer(list -> {
                    System.out.println("list = " + list);
                })
                .build();
    }

    // 쉽게 생각하면 DB의 내용을 애플리케이션에 하나씩 빨아들인다.
    @Bean
    public JdbcCursorItemReader<Customer> cursorItemReader() {
        return new JdbcCursorItemReaderBuilder<Customer>()
                .name("jdbcCursorItemReader")
                .fetchSize(chunkSize) // 메모리에 가져올 사이즈. ChunkSize와 맞추는 것이 좋다. 커밋 단위와 일치해야 관리가 수월하기 때문, 쿼리는 분할 처리 없이 실행되고 내부적으로 FetchSize만큼 가져와서 read()
                .sql("select id, name from customer where name = ? order by id no desc")
                .beanRowMapper(Customer.class)
                .queryArguments("Violet", Types.VARCHAR)
                .maxItemCount(20) // 조회할 최대 Item 수
                .currentItemCount(5) // 조회 Item의 시작 지점
                .maxRows(100) // ResultSet 오브젝트가 포함할 수 있는 최대 행수
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println("step2 has executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    static class Customer {
        private Long id;
        private String name;
    }

}
