package com.digdes.java.ddproject.repositories.jpa;

import com.digdes.java.ddproject.model.Task;
import com.digdes.java.ddproject.repositories.filters.SearchTaskFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {
    public static Specification<Task> getSpec(SearchTaskFilter filter) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!ObjectUtils.isEmpty(filter.getTitle())) {
                predicates.add(criteriaBuilder.like(root.get("title"), filter.getTitle()));
            }
            if (!ObjectUtils.isEmpty(filter.getAuthorId())) {
                predicates.add(criteriaBuilder.equal(root.get("author").get("id"), filter.getAuthorId()));
            }
            if (!ObjectUtils.isEmpty(filter.getExecutorId())) {
                predicates.add(criteriaBuilder.equal(root.get("executor").get("id"), filter.getExecutorId()));
            }
            if (!ObjectUtils.isEmpty(filter.getDeadlineMin()) ) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("deadline"), filter.getDeadlineMin()));
            }
            if (!ObjectUtils.isEmpty(filter.getDeadlineMax()) ) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("deadline"), filter.getDeadlineMax()));
            }
            if (!ObjectUtils.isEmpty(filter.getCreationDateMin()) ) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("creationDate"), filter.getCreationDateMin()));
            }
            if (!ObjectUtils.isEmpty(filter.getCreationDateMax()) ) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("creationDate"), filter.getCreationDateMax()));
            }
            if (!ObjectUtils.isEmpty(filter.getStatuses())) {
                predicates.add(criteriaBuilder.or(
                        filter.getStatuses()
                                .stream()
                                .map(s -> criteriaBuilder.like(root.get("status").as(String.class), s.name()))
                                .toArray(Predicate[]::new))
                );
            }
            query.orderBy(criteriaBuilder.desc(root.get("creationDate")));
            if (CollectionUtils.isEmpty(predicates)) {
                return query.where().getRestriction();
            }
            return query.where(criteriaBuilder.and(predicates.toArray(Predicate[]::new))).getRestriction();
        });
    }
}
