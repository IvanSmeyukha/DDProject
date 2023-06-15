package com.digdes.java.ddproject.repositories.jpa.specifications;

import com.digdes.java.ddproject.common.enums.MemberStatus;
import com.digdes.java.ddproject.model.Member;
import com.digdes.java.ddproject.repositories.filters.SearchMemberFilter;
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
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("firstName")),
                        String.format("%%%s%%", filter.getFirstName().toLowerCase())));
            }
            if(!ObjectUtils.isEmpty(filter.getLastName())){
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("lastName")),
                        String.format("%%%s%%", filter.getLastName().toLowerCase())));
            }
            if(!ObjectUtils.isEmpty(filter.getPatronymic())){
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("patronymic")),
                        String.format("%%%s%%", filter.getPatronymic().toLowerCase())));
            }
            if(!ObjectUtils.isEmpty(filter.getPosition())){
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("position")),
                        String.format("%%%s%%", filter.getPosition().toLowerCase())));
            }
            if(!ObjectUtils.isEmpty(filter.getEmail())){
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")),
                        String.format("%%%s%%", filter.getEmail().toLowerCase())));
            }
            return query.where(criteriaBuilder.and(predicates.toArray(Predicate[]::new))).getRestriction();
        });
    }
}
