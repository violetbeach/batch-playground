package io.springbatch.springbatchlecture.batch.chunk.writer;

import io.springbatch.springbatchlecture.batch.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@RequiredArgsConstructor
public class ApiItemWriterTypeTwo implements ItemWriter<ApiRequestVO> {

    private final ApiService apiService;

    @Override
    public void write(List<? extends ApiRequestVO> list) {
        apiService.execute(list);
    }
}
