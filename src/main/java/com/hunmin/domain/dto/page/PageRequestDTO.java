package com.hunmin.domain.dto.page;


import lombok.Getter;
import lombok.Builder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import jakarta.validation.constraints.Min;

@Getter
@Builder
public class PageRequestDTO {
    @Builder.Default
    @Min(1)
    private int page = 1;

    @Builder.Default
    @Min(10)
    private int size = 10;

    public Pageable getPageable(Sort sort) {
        int pageNum = page < 0 ? 1 : page - 1;
        int sizeNum = size < 10 ? 10 : size;

        return PageRequest.of(pageNum, sizeNum, sort);
    }
}
