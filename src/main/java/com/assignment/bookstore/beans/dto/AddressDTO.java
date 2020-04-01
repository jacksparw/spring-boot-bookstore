package com.assignment.bookstore.beans.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDTO {
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
}
