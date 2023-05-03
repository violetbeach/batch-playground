package io.springbatch.springbatchlecture.batch.job.jdbc;

import io.springbatch.springbatchlecture.batch.domain.ProductVO;
import io.springbatch.springbatchlecture.batch.mapper.ProductRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryGenerator {

    public static ProductVO[] getProductList(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<ProductVO> products = jdbcTemplate.query("select type from product group by type", new ProductRowMapper());
        return products.toArray(new ProductVO[]{});
    }

    public static Map<String, Object> getParameterForQuery(String param, String value) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(param, value);
        return params
    }

}
