package com.digdes.java.ddproject.mapping.filters;

import com.digdes.java.ddproject.dto.filters.SearchTaskFilterDto;
import com.digdes.java.ddproject.repositories.filters.SearchTaskFilter;
import org.springframework.stereotype.Component;

@Component
public class SearchTaskFilterMapper {

    public SearchTaskFilter fromDto(SearchTaskFilterDto dto) {
        return SearchTaskFilter.builder()
                .title(dto.getTitle())
                .authorId(dto.getAuthorId())
                .executorId(dto.getExecutorId())
                .creationDateMin(dto.getCreationDateMin())
                .creationDateMax(dto.getCreationDateMax())
                .deadlineMin(dto.getDeadlineMin())
                .deadlineMax(dto.getDeadlineMax())
                .statuses(dto.getStatuses())
                .build();
    }
}
