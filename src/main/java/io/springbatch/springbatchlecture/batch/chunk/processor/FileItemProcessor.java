package io.springbatch.springbatchlecture.batch.chunk.processor;

import io.springbatch.springbatchlecture.batch.domain.Product;
import io.springbatch.springbatchlecture.batch.domain.ProductVO;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

public class FileItemProcessor implements ItemProcessor<ProductVO, Product> {

    @Override
    public Product process(ProductVO productVO) throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(productVO, Product.class);
    }
}
