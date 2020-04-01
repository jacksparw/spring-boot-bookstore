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

    @OneToOne(mappedBy = "stock", cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH})
    private @ToString.Exclude Book book;

    @Column
    private @EqualsAndHashCode.Exclude Integer bookCount;

    public Stock(Integer bookCount) {
        this.bookCount = bookCount;
    }
}
