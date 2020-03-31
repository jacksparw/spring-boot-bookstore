package com.assignment.bookstore.beans.dto.book;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static com.assignment.bookstore.util.MessageConstants.ErrorMessage.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    @NotEmpty(message = TITLE_IS_MANDATORY)
    private String title;

    @NotEmpty(message = ISBN_IS_MANDATORY)
    private String isbn;

    @NotNull @Min(value = 1, message = INVALID_PRICE)
    private BigDecimal price;
}
