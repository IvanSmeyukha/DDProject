package com.digdes.java.ddproject.mapping.member;

import com.digdes.java.ddproject.common.enums.MemberStatus;
import com.digdes.java.ddproject.model.Member;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberResultSetMapper {

    private MemberResultSetMapper(){
    }

    public static Member map(ResultSet resultSet) throws SQLException {
        return Member.builder()
                .id(resultSet.getLong("id"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .surName(resultSet.getString("sur_name"))
                .account(resultSet.getLong("account"))
                .position(resultSet.getString("position"))
                .email(resultSet.getString("email"))
                .status(MemberStatus.valueOf(resultSet.getString("status")))
                .build();
    }
}
