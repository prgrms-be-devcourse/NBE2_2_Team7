package com.hunmin.domain.dto.page;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Builder
public class WordPageRequestDTO {

    @Builder.Default
    @Min(1)
    private int page = 1;

    @Builder.Default
    @Min(20)
    @Max(100)
    private int size = 20;

    public Pageable getPageable(Sort sort){
        return PageRequest.of(page - 1, size, sort);
    }
}
