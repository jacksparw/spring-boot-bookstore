package com.assignment.bookstore.beans.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
public class Book {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(unique = true, nullable = false)
    private String title;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "author", nullable = false)
    private Author author;

    @Column(nullable = false)
    private @EqualsAndHashCode.Exclude BigDecimal price;

    @OneToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinColumn(name = "stockId")
    private @EqualsAndHashCode.Exclude Stock stock;
}
