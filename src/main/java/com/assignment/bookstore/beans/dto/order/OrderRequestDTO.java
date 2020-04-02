package com.assignment.bookstore.beans.dto.order;

import com.assignment.bookstore.validators.annotation.EmailConstraint;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.assignment.bookstore.util.MessageConstants.ErrorMessage.BOOKS_DETAILS_MISSING;
import static com.assignment.bookstore.util.MessageConstants.ErrorMessage.CUSTOMER_NAME_IS_MANDATORY;

@Data
@Builder
public class OrderRequestDTO {

    @NotEmpty(message = CUSTOMER_NAME_IS_MANDATORY)
    private String customerName;

    @EmailConstraint
    private String email;

    @NotNull(message = BOOKS_DETAILS_MISSING)
    private List<@Valid BookOrderLineDTO> books;
}
