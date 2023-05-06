package io.springbatch.springbatchlecture.batch.partition;

import io.springbatch.springbatchlecture.batch.domain.ProductVO;
import io.springbatch.springbatchlecture.batch.job.jdbc.QueryGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ProductPartitioner implements Partitioner {

    private final DataSource dataSource;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        ProductVO[] productList = QueryGenerator.getProductList(dataSource);
        Map<String, ExecutionContext> result = new HashMap<>();

        for (int i = 0; i < productList.length; i++) {
            ExecutionContext context = new ExecutionContext();
            context.put("product", productList[i]);
            result.put("partition" + i, context);
        }
        return result;
    }

}
