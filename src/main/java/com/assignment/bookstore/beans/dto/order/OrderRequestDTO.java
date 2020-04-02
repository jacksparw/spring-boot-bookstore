package com.assignment.bookstore.beans.dto.order;

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

    private @NotEmpty(message = CUSTOMER_NAME_IS_MANDATORY) String customerName;
    private @NotNull(message = BOOKS_DETAILS_MISSING) List<@Valid BookOrderLineDTO> books;
}
