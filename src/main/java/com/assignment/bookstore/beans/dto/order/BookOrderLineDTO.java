package com.assignment.bookstore.beans.dto.order;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static com.assignment.bookstore.util.MessageConstants.ErrorMessage.*;

@Data
@Builder
public class BookOrderLineDTO {

    @NotEmpty(message = ISBN_IS_MANDATORY)
    private String isbn;

    @NotNull(message = QUANTITY_IS_MISSING) @Min(value = 1, message = INVALID_QUANTITY)
    private Integer orderQuantity;
}
