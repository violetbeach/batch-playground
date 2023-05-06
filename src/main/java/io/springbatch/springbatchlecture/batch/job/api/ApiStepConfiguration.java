package io.springbatch.springbatchlecture.batch.job.api;

import io.springbatch.springbatchlecture.batch.chunk.processor.ApiItemProcessorTypeOne;
import io.springbatch.springbatchlecture.batch.chunk.processor.ApiItemProcessorTypeThree;
import io.springbatch.springbatchlecture.batch.chunk.processor.ApiItemProcessorTypeTwo;
import io.springbatch.springbatchlecture.batch.chunk.writer.ApiItemWriterTypeOne;
import io.springbatch.springbatchlecture.batch.chunk.writer.ApiItemWriterTypeThree;
import io.springbatch.springbatchlecture.batch.chunk.writer.ApiItemWriterTypeTwo;
import io.springbatch.springbatchlecture.batch.classifier.ProcessorClassifier;
import io.springbatch.springbatchlecture.batch.classifier.WriterClassifier;
import io.springbatch.springbatchlecture.batch.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.batch.domain.ProductVO;
import io.springbatch.springbatchlecture.batch.job.jdbc.QueryGenerator;
import io.springbatch.springbatchlecture.batch.partition.ProductPartitioner;
import io.springbatch.springbatchlecture.service.TypeAService;
import io.springbatch.springbatchlecture.service.TypeBService;
import io.springbatch.springbatchlecture.service.TypeCService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ApiStepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private static final int chunkSize = 10;

    @SneakyThrows
    @Bean
    public Step apiMasterStep() {
        return stepBuilderFactory.get("apiMasterStep")
                .partitioner(apiSlaveStep().getName(), partitioner())
                .step(apiSlaveStep())
                .gridSize(5)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(3);
        taskExecutor.setMaxPoolSize(6);
        taskExecutor.setThreadNamePrefix("api-thread-");
        return taskExecutor;
    }

    @Bean
    public Step apiSlaveStep() throws Exception {
        return stepBuilderFactory.get("apiSlaveStep")
                .<ProductVO, ProductVO> chunk(chunkSize)
                .reader(itemReader(null))
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public ProductPartitioner partitioner() {
        return new ProductPartitioner(dataSource);
    }

    @Bean
    @StepScope
    public ItemReader<ProductVO> itemReader(@Value("#{stepExecutionContext['product']}") ProductVO productVO) throws Exception {
        JdbcPagingItemReader<ProductVO> reader = new JdbcPagingItemReaderBuilder<ProductVO>()
                .dataSource(dataSource)
                .pageSize(chunkSize)
                .rowMapper(new BeanPropertyRowMapper<>(ProductVO.class))
                .build();

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, name, price, type");
        queryProvider.setFromClause("from product");
        queryProvider.setWhereClause("where type = :type");

        HashMap<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("id", Order.DESCENDING);

        queryProvider.setSortKeys(sortKeys);

        reader.setParameterValues(QueryGenerator.getParameterForQuery("type", productVO.type()));
        reader.setQueryProvider(queryProvider);
        reader.afterPropertiesSet();
        return reader;
    }

    @Bean
    public ItemProcessor itemProcessor() {
        ClassifierCompositeItemProcessor<ProductVO, ApiRequestVO> processor = new ClassifierCompositeItemProcessor<>();

        Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap = new HashMap<>();
        processorMap.put("1", new ApiItemProcessorTypeOne());
        processorMap.put("2", new ApiItemProcessorTypeTwo());
        processorMap.put("3", new ApiItemProcessorTypeThree());

        Classifier<ProductVO, ItemProcessor<?, ? extends ApiRequestVO>> processorClassifier = new ProcessorClassifier(processorMap);
        processor.setClassifier(processorClassifier);

        return processor;
    }

    @Bean
    public ItemWriter<ApiRequestVO> itemWriter() {
        ClassifierCompositeItemWriter<ApiRequestVO> writer = new ClassifierCompositeItemWriter<>();

        Map<String, ItemWriter<ApiRequestVO>> writerMap = new HashMap<>();
        writerMap.put("1", new ApiItemWriterTypeOne(new TypeAService()));
        writerMap.put("2", new ApiItemWriterTypeTwo(new TypeBService()));
        writerMap.put("3", new ApiItemWriterTypeThree(new TypeCService()));

        Classifier<ApiRequestVO, ItemWriter<? super ApiRequestVO>> writerClassifier = new WriterClassifier(writerMap);
        writer.setClassifier(writerClassifier);

        return writer;
    }

}
