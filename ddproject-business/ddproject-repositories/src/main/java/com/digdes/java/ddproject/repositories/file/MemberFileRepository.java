package com.digdes.java.ddproject.repositories.file;

import com.digdes.java.ddproject.common.exceptions.NotFoundException;
import com.digdes.java.ddproject.model.Member;
import com.digdes.java.ddproject.repositories.MemberRepository;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class MemberFileRepository implements MemberRepository {
    private final AtomicLong idGenerator;
    private static final String FILE_NAME = "member.model";
    private static final MemberFileRepository repository = new MemberFileRepository();

    private MemberFileRepository() {
        idGenerator = new AtomicLong(getAll()
                .stream()
                .map(Member::getId)
                .max(Comparator.naturalOrder())
                .orElse(0L)
        );
    }

    public static MemberFileRepository getInstance() {
        return repository;
    }

    @Override
    public Member create(Member member) {
        member.setId(idGenerator.incrementAndGet());
        List<Member> members = getAll();
        members.add(member);
        saveMembers(members);
        return member;
    }

    @Override
    public Member update(Member member) {
        List<Member> members = getAll();
        members = members.stream().map(m -> m.getId().equals(member.getId()) ? member : m).toList();
        saveMembers(members);
        return member;
    }

    @Override
    public Member getById(Long id) throws NotFoundException {
        List<Member> members = getAll();
        Optional<Member> member = members.stream().filter(m -> m.getId().equals(id)).findFirst();
        if (member.isEmpty()) {
            throw new NotFoundException("Member with such id not exist");
        }
        return member.get();
    }

    @Override
    public List<Member> getAll() {
        List<Member> members = new ArrayList<>();
        if (new File(FILE_NAME).isFile()) {
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream("member.model"));
                members = (List<Member>) in.readObject();
                in.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            } catch (IOException e) {
                System.out.println("Error initializing stream");
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found");
                e.printStackTrace();
            }
        }
        return members;
    }

    @Override
    public Member deleteById(Long id) throws NotFoundException {
        List<Member> members = getAll();
        Optional<Member> member = members.stream().filter(m -> m.getId().equals(id)).findFirst();
        if (member.isEmpty()) {
            throw new NotFoundException("Member with such id not exist");
        }
        members.remove(member.get());
        saveMembers(members);
        return member.get();
    }

    private List<Member> saveMembers(List<Member> members) {
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME);
                ObjectOutputStream out = new ObjectOutputStream(fileOutputStream)
        ) {
            out.writeObject(members);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error initializing stream");
            e.printStackTrace();
        }
        return members;
    }
}
