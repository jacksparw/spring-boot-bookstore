package com.assignment.bookstore.controller;

import com.assignment.bookstore.beans.dto.GenericResponseDTO;
import com.assignment.bookstore.beans.dto.order.OrderRequestDTO;
import com.assignment.bookstore.exception.ValidationException;
import com.assignment.bookstore.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.assignment.bookstore.util.MessageConstants.RequestStatus.STATUS_SUCCESS;
import static com.assignment.bookstore.util.MessageConstants.SuccessMessage.PURCHASE_DETAILS;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(value = "${paths.buyBook}", consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
	public ResponseEntity<GenericResponseDTO> buyBook(@RequestBody @Valid OrderRequestDTO requestDTO, BindingResult result) {

        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                throw new ValidationException(error.getDefaultMessage());
            }
        }

        GenericResponseDTO response = createSuccessResponse(PURCHASE_DETAILS);
        response.setData(orderService.createOrder(requestDTO));

        return ResponseEntity.ok(response);
	}

    private static GenericResponseDTO createSuccessResponse(String s) {
        GenericResponseDTO response = new GenericResponseDTO();
        response.setMessage(s);
        response.setStatus(STATUS_SUCCESS);
        return response;
    }
}
