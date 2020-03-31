package com.assignment.bookstore.beans.dto.order;

import lombok.Data;

@Data
public class BookOrderLineDTO {
    private Long bookId;
    private Integer orderQuantity = 0;
}
