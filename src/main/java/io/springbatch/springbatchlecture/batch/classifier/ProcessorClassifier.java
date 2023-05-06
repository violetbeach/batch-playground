package io.springbatch.springbatchlecture.batch.classifier;

import io.springbatch.springbatchlecture.batch.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.batch.domain.ProductVO;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

import java.util.Map;

@RequiredArgsConstructor
public class ProcessorClassifier implements Classifier<ProductVO, ItemProcessor<?, ? extends ApiRequestVO>> {

    private final Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap;

    @Override
    public ItemProcessor<ProductVO, ? extends ApiRequestVO> classify(ProductVO productVO) {
        return processorMap.get(productVO.type());
    }
}
