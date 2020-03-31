package com.assignment.bookstore.repository.specification;

import com.assignment.bookstore.beans.domain.Book;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@AllArgsConstructor
public class BookISBNSpec implements Specification<Book> {
 
    private String isbn;

    @Override
    public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (isbn == null) {
            return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        }
        return criteriaBuilder.equal(root.get("isbn"), this.isbn.trim());
    }
}