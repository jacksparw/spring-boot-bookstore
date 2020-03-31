package com.assignment.bookstore.beans.dto.mapper;

import com.assignment.bookstore.beans.domain.Order;
import com.assignment.bookstore.beans.dto.order.OrderRequestDTO;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {

    Order orderRequestDtoToOrder(OrderRequestDTO dto);
}
