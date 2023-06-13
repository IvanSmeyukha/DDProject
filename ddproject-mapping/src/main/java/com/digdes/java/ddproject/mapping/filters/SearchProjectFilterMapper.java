package com.digdes.java.ddproject.mapping.filters;

import com.digdes.java.ddproject.dto.filters.SearchProjectFilterDto;
import com.digdes.java.ddproject.repositories.filters.SearchProjectFilter;
import org.springframework.stereotype.Component;

@Component
public class SearchProjectFilterMapper {

    public SearchProjectFilter fromDto(SearchProjectFilterDto dto) {
        return SearchProjectFilter.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .statuses(dto.getStatuses())
                .build();
    }
}
