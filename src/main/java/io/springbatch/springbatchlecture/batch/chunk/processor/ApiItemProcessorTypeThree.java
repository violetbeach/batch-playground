package io.springbatch.springbatchlecture.batch.chunk.processor;

import io.springbatch.springbatchlecture.batch.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.batch.domain.ProductVO;
import org.springframework.batch.item.ItemProcessor;

public class ApiItemProcessorTypeThree implements ItemProcessor<ProductVO, ApiRequestVO> {
    @Override
    public ApiRequestVO process(ProductVO productVO) {
        return new ApiRequestVO(
                productVO.id(),
                productVO
        );
    }
}
