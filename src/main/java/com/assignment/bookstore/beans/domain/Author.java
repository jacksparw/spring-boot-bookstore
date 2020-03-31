package com.assignment.bookstore.beans.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Author {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorId;

    @Column(name = "AUTHORNAME", unique = true, nullable = false)
    private String authorName;

    @Column
    private String description;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private @ToString.Exclude List<Book> book;
}
