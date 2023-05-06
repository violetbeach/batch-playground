package io.springbatch.springbatchlecture.service;

import io.springbatch.springbatchlecture.batch.domain.ApiResponseVO;
import io.springbatch.springbatchlecture.domain.ApiInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class TypeAService extends ApiService {
    @Override
    protected ApiResponseVO doApiService(RestTemplate restTemplate, ApiInfo apiInfo) {
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8081/api/product/1", apiInfo, String.class);
        return new ApiResponseVO(
                response.getStatusCodeValue(),
                response.getBody());
    }
}
