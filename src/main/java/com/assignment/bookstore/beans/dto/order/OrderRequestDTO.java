package com.assignment.bookstore.beans.dto.order;

import com.assignment.bookstore.beans.dto.CustomerDTO;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    private CustomerDTO customer;
    private List<BookOrderLineDTO> book;
}
