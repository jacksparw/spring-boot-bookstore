package com.assignment.bookstore.beans.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.assignment.bookstore.util.MessageConstants.ErrorMessage.*;

@Data
@Builder
public class CustomerDTO {

    @NotNull(message = CUSTOMER_ID_IS_MANDATORY) @Min(value = 1, message = CUSTOMER_ID_IS_INVALID)
    private Long customerId;
    private String name;
}
