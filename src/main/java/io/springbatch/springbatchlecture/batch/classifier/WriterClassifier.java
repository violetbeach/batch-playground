package io.springbatch.springbatchlecture.batch.classifier;

import io.springbatch.springbatchlecture.batch.domain.ApiRequestVO;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

import java.util.Map;

@RequiredArgsConstructor
public class WriterClassifier implements Classifier<ApiRequestVO, ItemWriter<? super ApiRequestVO>> {

    private final Map<String, ItemWriter<ApiRequestVO>> writerMap;

    @Override
    public ItemWriter<? super ApiRequestVO> classify(ApiRequestVO apiRequestVO) {
        return writerMap.get(apiRequestVO.productVO().type());
    }
}
