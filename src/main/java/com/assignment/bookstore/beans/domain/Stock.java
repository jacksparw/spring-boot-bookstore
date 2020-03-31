package com.assignment.bookstore.beans.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Stock {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    @Column
    private @EqualsAndHashCode.Exclude Integer bookCount;

    @OneToMany(mappedBy = "stock", cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH})
    private @EqualsAndHashCode.Exclude @ToString.Exclude List<Book> books;

    public Stock(Integer bookCount) {
        this.bookCount = bookCount;
    }
}
