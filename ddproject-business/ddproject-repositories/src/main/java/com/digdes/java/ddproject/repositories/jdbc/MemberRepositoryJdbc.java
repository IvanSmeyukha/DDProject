package com.digdes.java.ddproject.repositories.jdbc;

import com.digdes.java.ddproject.common.enums.MemberStatus;
import com.digdes.java.ddproject.dto.filters.SearchMemberFilterDto;
import com.digdes.java.ddproject.dto.project.AddMemberToProjectDto;
import com.digdes.java.ddproject.model.Member;
import com.digdes.java.ddproject.model.UserAccount;
import com.digdes.java.ddproject.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.*;


@RequiredArgsConstructor
public class MemberRepositoryJdbc implements MemberRepository {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    private static final String CREATE_MEMBER_QUERY = """
            insert into member(first_name, last_name, sur_name, position, account, email, status)
            values (?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_MEMBER_QUERY = """
            update member
            set first_name = ?, last_name = ?, sur_name = ?, position = ?, account = ?, email = ?, status = ?
            where id = ? and status != 'DELETED'
            """;

    private static final String GET_MEMBER_BY_ID_QUERY = """
            select * from member
            where id = ? and status != 'DELETED'
            """;


    private static final String DELETE_MEMBER_BY_ID_QUERY = """
            update member
            set status = 'DELETED'
            where id = ?
            """;

    private static final String ADD_MEMBER_TO_PROJECT_QUERY = """
            insert into project_team(member_id, project_id, role)
            values (?, ?, ?)
            """;

    @SneakyThrows
    @Override
    public Optional<Member> create(Member member) {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(CREATE_MEMBER_QUERY, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, member.getFirstName());
            ps.setString(2, member.getLastName());
            ps.setObject(3, member.getPatronymic(), Types.VARCHAR);
            ps.setObject(4, member.getPosition(), Types.VARCHAR);
            ps.setObject(5, member.getAccount(), Types.BIGINT);
            ps.setObject(6, member.getEmail(), Types.VARCHAR);
            ps.setObject(7, member.getStatus(), Types.VARCHAR);
            ps.executeUpdate();
            if (ps.getGeneratedKeys().next()) {
                member.setId(ps.getGeneratedKeys().getLong(1));
            }
        }
        return Optional.of(member);
    }

    @SneakyThrows
    @Override
    public Optional<Member> update(Member member) {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_MEMBER_QUERY)
        ) {
            ps.setString(1, member.getFirstName());
            ps.setString(2, member.getLastName());
            ps.setObject(3, member.getPatronymic(), Types.VARCHAR);
            ps.setObject(4, member.getPosition(), Types.VARCHAR);
            ps.setObject(5, member.getAccount(), Types.BIGINT);
            ps.setObject(6, member.getEmail(), Types.VARCHAR);
            ps.setObject(7, member.getStatus(), Types.VARCHAR);
            ps.setLong(8, member.getId());
            ps.executeUpdate();
        }
        return Optional.of(member);
    }

    @SneakyThrows
    @Override
    public Optional<Member> getById(Long id) {
        Member member = Member.builder().build();
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_MEMBER_BY_ID_QUERY)
        ) {
            ps.setLong(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    member = fromResultSet(resultSet);
                }
            }
        }
        return Optional.of(member);
    }

    @SneakyThrows
    private Member fromResultSet(ResultSet resultSet) {
        return Member.builder()
                .id(resultSet.getLong("id"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .patronymic(resultSet.getString("sur_name"))
                .account(UserAccount.builder().id(resultSet.getLong("account")).build())
                .position(resultSet.getString("position"))
                .email(resultSet.getString("email"))
                .status(MemberStatus.valueOf(resultSet.getString("status")))
                .build();
    }


    @SneakyThrows
    @Override
    public Optional<List<Member>> search(SearchMemberFilterDto filter) {
        List<Member> members = new ArrayList<>();
        Map<Integer, Object> parameterMap = new HashMap<>();
        String sql = buildSearchQuery(filter, parameterMap);
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            for (Map.Entry<Integer, Object> parameterEntry : parameterMap.entrySet()) {
                ps.setObject(parameterEntry.getKey(), parameterEntry.getValue());
            }
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    Member member = fromResultSet(resultSet);
                    members.add(member);
                }
            }
        }
        return Optional.of(members);
    }

    @SneakyThrows
    public Optional<Member> deleteById(Long id) {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_MEMBER_BY_ID_QUERY)
        ) {
            Optional<Member> member = getById(id);
            ps.setLong(1, id);
            ps.executeUpdate();
            return member;
        }
    }

    @SneakyThrows
    public Optional<Member> addToProject(Long projectId, AddMemberToProjectDto addMember) {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(ADD_MEMBER_TO_PROJECT_QUERY)
        ) {
            ps.setLong(1, addMember.getMemberId());
            ps.setLong(2, projectId);
            ps.setString(3, addMember.getRole().toString());
            ps.executeUpdate();
            return getById(addMember.getMemberId());
        }
    }

    private String buildSearchQuery(SearchMemberFilterDto filter, Map<Integer, Object> parameterMap) {
        int parameterIndex = 1;
        String sql = "select id, first_name, last_name, sur_name, position, account, email, status from member";
//        if (filter.getProjectId() != null) {
//            sql += " join project_team on member.id = project_team.member_id where project_team.project_id = ? and";
//            parameterMap.put(parameterIndex++, filter.getProjectId());
//        } else {
//            sql += " where";
//        }
        sql += " member.status != 'DELETED'";
        if (filter.getFirstName() != null) {
            sql += " and member.first_name = ?";
            parameterMap.put(parameterIndex++, filter.getFirstName());
        }
        if (filter.getLastName() != null) {
            sql += " and member.last_name = ?";
            parameterMap.put(parameterIndex++, filter.getLastName());
        }
        if (filter.getPatronymic() != null) {
            sql += " and member.sur_name = ?";
            parameterMap.put(parameterIndex++, filter.getPatronymic());
        }
        if (filter.getPosition() != null) {
            sql += " and member.position = ?";
            parameterMap.put(parameterIndex++, filter.getPosition());
        }
        if (filter.getEmail() != null) {
            sql += " and member.email = ?";
            parameterMap.put(parameterIndex++, filter.getEmail());
        }
        return sql;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager
                .getConnection(dbUrl, dbUser, dbPassword);
    }
}
