package com.digdes.java.ddproject.mapping.filters;

import com.digdes.java.ddproject.dto.filters.SearchMemberFilterDto;
import com.digdes.java.ddproject.repositories.filters.SearchMemberFilter;
import org.springframework.stereotype.Component;

@Component
public class SearchMemberFilterMapper {
    public SearchMemberFilter fromDto(SearchMemberFilterDto dto) {
        return SearchMemberFilter.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .patronymic(dto.getPatronymic())
                .position(dto.getPosition())
                .email(dto.getEmail())
                .build();
    }

}
