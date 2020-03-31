package com.assignment.bookstore.service;

import com.assignment.bookstore.beans.dto.order.OrderRequestDTO;
import com.assignment.bookstore.beans.dto.order.OrderResponseDTO;

public interface OrderService {

    OrderResponseDTO createOrder(OrderRequestDTO requestDTO);
}
