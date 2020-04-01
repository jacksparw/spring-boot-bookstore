package com.assignment.bookstore.beans.dto.order;

import com.assignment.bookstore.beans.dto.AddressDTO;
import com.assignment.bookstore.beans.dto.book.BookDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private String customerName;
    private AddressDTO dispatchAddress;
    private BigDecimal totalAmount;
    private List<BookDTO> bookDTOList;
}
