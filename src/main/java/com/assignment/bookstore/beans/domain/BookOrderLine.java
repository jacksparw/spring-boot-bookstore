package com.assignment.bookstore.beans.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
public class BookOrderLine {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookOrderLineId;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private @EqualsAndHashCode.Exclude Order order;

    @ManyToOne
    @JoinColumn(name = "bookId")
    private @EqualsAndHashCode.Exclude Book book;

    @Column
    private @EqualsAndHashCode.Exclude Integer orderQuantity = 0;
}
