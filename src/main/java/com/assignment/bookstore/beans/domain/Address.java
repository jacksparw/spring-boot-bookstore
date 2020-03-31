package com.assignment.bookstore.beans.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
public class Address {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Column(nullable = false)
    private String addressLine1;

    @Column
    private String addressLine2;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @OneToOne(mappedBy = "address")
    private @ToString.Exclude Customer customer;
}
