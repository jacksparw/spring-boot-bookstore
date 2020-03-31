package com.assignment.bookstore.repository.specification;

import com.assignment.bookstore.beans.domain.Book;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@AllArgsConstructor
public class BookAuthorSpec implements Specification<Book> {

    private String author;

    @Override
    public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (author == null) {
            return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        }
        return criteriaBuilder.like(
                criteriaBuilder.upper(root.get("author").get("authorName")),
                "%"+this.author.trim().toUpperCase()+"%");
    }
}