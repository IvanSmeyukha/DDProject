package com.digdes.java.ddproject.mapping.useraccount;

import com.digdes.java.ddproject.dto.security.UserAccountDto;
import com.digdes.java.ddproject.model.UserAccount;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class UserAccountMapper {

    public UserAccount fromUserAccountDto(UserAccountDto dto) {
        return !ObjectUtils.isEmpty(dto) ? UserAccount.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build() :
                null;
    }

    public UserAccountDto toUserAccountDto(UserAccount user) {
        return !ObjectUtils.isEmpty(user) ? UserAccountDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .build() :
                new UserAccountDto();
    }
}
