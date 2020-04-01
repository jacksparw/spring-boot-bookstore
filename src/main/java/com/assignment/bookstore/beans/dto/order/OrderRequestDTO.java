package com.assignment.bookstore.beans.dto.order;

import com.assignment.bookstore.beans.dto.CustomerDTO;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
@Builder
public class OrderRequestDTO {
    private  @Valid CustomerDTO customer;
    private List<@Valid BookOrderLineDTO> book;
}
