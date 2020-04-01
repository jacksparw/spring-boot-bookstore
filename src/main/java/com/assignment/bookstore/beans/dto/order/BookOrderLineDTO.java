package com.assignment.bookstore.beans.dto.order;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.assignment.bookstore.util.MessageConstants.ErrorMessage.*;

@Data
@Builder
public class BookOrderLineDTO {

    @NotNull(message = BOOK_ID_IS_MISSING) @Min(value = 1, message = INVALID_BOOK_ID)
    private Long bookId;

    @NotNull(message = QUANTITY_IS_MISSING) @Min(value = 1, message = INVALID_QUANTITY)
    private Integer orderQuantity;
}
