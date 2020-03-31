package com.assignment.bookstore.beans.dto.order;

import com.assignment.bookstore.beans.dto.AddressDTO;
import com.assignment.bookstore.beans.dto.book.BookDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderResponseDTO {
    private String customerName;
    private AddressDTO addressDTO;
    private BigDecimal totalAmount;
    private List<BookDTO> bookDTOList;
}
