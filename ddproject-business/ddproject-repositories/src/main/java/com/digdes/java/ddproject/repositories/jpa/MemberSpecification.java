package com.digdes.java.ddproject.repositories.jpa;

import com.digdes.java.ddproject.common.enums.MemberStatus;
import com.digdes.java.ddproject.dto.filters.SearchMemberFilter;
import com.digdes.java.ddproject.model.Member;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class MemberSpecification {
    public static Specification<Member> getSpec(SearchMemberFilter filter) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.notEqual(root.get("status"), MemberStatus.DELETED));
            if(!ObjectUtils.isEmpty(filter.getFirstName())){
                predicates.add(criteriaBuilder.like(root.get("firstName"), filter.getFirstName()));
            }
            if(!ObjectUtils.isEmpty(filter.getLastName())){
                predicates.add(criteriaBuilder.like(root.get("lastName"), filter.getLastName()));
            }
            if(!ObjectUtils.isEmpty(filter.getPatronymic())){
                predicates.add(criteriaBuilder.like(root.get("patronymic"), filter.getPatronymic()));
            }
            if(!ObjectUtils.isEmpty(filter.getPosition())){
                predicates.add(criteriaBuilder.like(root.get("position"), filter.getPosition()));
            }
            if(!ObjectUtils.isEmpty(filter.getEmail())){
                predicates.add(criteriaBuilder.like(root.get("email"), filter.getEmail()));
            }
            return query.where(criteriaBuilder.and(predicates.toArray(Predicate[]::new))).getRestriction();
        });
    }
}
