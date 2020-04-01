package com.assignment.bookstore.beans.dto.order;

import com.assignment.bookstore.beans.dto.CustomerDTO;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.assignment.bookstore.util.MessageConstants.ErrorMessage.*;

@Data
@Builder
public class OrderRequestDTO {
    private @NotNull(message = CUSTOMER_DETAILS_MISSING) @Valid CustomerDTO customer;
    private @NotNull(message = BOOKS_DETAILS_MISSING) List<@Valid BookOrderLineDTO> books;
}
