package io.springbatch.springbatchlecture.domain;

import io.springbatch.springbatchlecture.batch.domain.ApiRequestVO;

import java.util.List;

public record ApiInfo (
        List<? extends ApiRequestVO> apiRequestList
){}
