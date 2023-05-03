package io.springbatch.springbatchlecture.batch.mapper;

import io.springbatch.springbatchlecture.batch.domain.ProductVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<ProductVO> {

    @Override
    public ProductVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ProductVO(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("price"),
                rs.getString("type")
        );
    }

}
