package com.digdes.java.ddproject.repositories.file;

import com.digdes.java.ddproject.dto.filters.SearchMemberFilter;
import com.digdes.java.ddproject.dto.member.AddMemberToProjectDto;
import com.digdes.java.ddproject.model.Member;
import com.digdes.java.ddproject.repositories.MemberRepository;
import lombok.SneakyThrows;


import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class MemberRepositoryFile implements MemberRepository {
    private final AtomicLong idGenerator;
    private final String dataSource;

    public MemberRepositoryFile(String fileName) {
        dataSource = fileName;
        idGenerator = new AtomicLong(getAll()
                .get()
                .stream()
                .map(Member::getId)
                .max(Comparator.naturalOrder())
                .orElse(0L));
    }

    @Override
    public Optional<Member> create(Member member) {
        member.setId(idGenerator.incrementAndGet());
        List<Member> members = getAll().get();
        members.add(member);
        saveMembers(members);
        return Optional.of(member);
    }

    @Override
    public Optional<Member> update(Member member) {
        List<Member> members = getAll().get();
        members = members.stream().map(m -> m.getId().equals(member.getId()) ? member : m).toList();
        saveMembers(members);
        return Optional.of(member);
    }

    @Override
    public Optional<Member> getById(Long id){
        List<Member> members = getAll().get();
        return members.stream().filter(m -> m.getId().equals(id)).findFirst();
    }

    @SneakyThrows
    public Optional<List<Member>> getAll() {
        try (
                FileInputStream fileInputStream = new FileInputStream(dataSource);
                ObjectInputStream in = new ObjectInputStream(fileInputStream);
        ) {
            return Optional.of((List<Member>) in.readObject());
        }
    }

    @Override
    public Optional<Member> deleteById(Long id) {
        List<Member> members = getAll().get();
        Optional<Member> member = members.stream().filter(m -> m.getId().equals(id)).findFirst();
        members.remove(member.get());
        saveMembers(members);
        return member;
    }

    @SneakyThrows
    private List<Member> saveMembers(List<Member> members) {
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(dataSource);
                ObjectOutputStream out = new ObjectOutputStream(fileOutputStream)
        ) {
            out.writeObject(members);
        }
        return members;
    }

    @Override
    public Optional<List<Member>> search(SearchMemberFilter filter) {
        return null;
    }

    @Override
    public Optional<Member> addToProject(AddMemberToProjectDto addMember) {
        return Optional.empty();
    }
}
