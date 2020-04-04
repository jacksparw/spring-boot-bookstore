package com.assignment.bookstore.service;

import com.assignment.bookstore.beans.dto.order.BookOrderLineDTO;
import com.assignment.bookstore.beans.dto.order.OrderRequestDTO;
import com.assignment.bookstore.beans.dto.order.OrderResponseDTO;
import com.assignment.bookstore.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderServiceTest {

    private @Autowired OrderService orderService;

    private OrderRequestDTO validOrderRequestDTO;

    @BeforeEach
    public void setup() {

        BookOrderLineDTO dummyBookOrderLineDTO1 = BookOrderLineDTO.builder()
                .isbn("1")
                .orderQuantity(10)
                .build();

        BookOrderLineDTO dummyBookOrderLineDTO2 = BookOrderLineDTO.builder()
                .isbn("2")
                .orderQuantity(4)
                .build();

        validOrderRequestDTO = OrderRequestDTO.builder()
                .customerName("Dummy Customer")
                .email("dummy@bookstore.com")
                .books(Arrays.asList(dummyBookOrderLineDTO1, dummyBookOrderLineDTO2))
                .build();
    }

    @Test
    @Transactional
    public void testCreateOrder_UNKNOWN_Book(){

        //given
        validOrderRequestDTO.getBooks().get(0).setIsbn("1000");

        //When
        Exception exception = assertThrows(ValidationException.class, () -> orderService.createOrder(validOrderRequestDTO));

        //then
        String expectedMessage = "Book Not Found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Transactional
    public void testCreateOrder(){

        //When
        OrderResponseDTO responseDTO = orderService.createOrder(validOrderRequestDTO);

        //Then
        assertNotNull(responseDTO);
        assertTrue(responseDTO.getCustomerName().equalsIgnoreCase("Dummy Customer"));
        assertTrue(responseDTO.getBookDTOList().size() == 2);
        assertTrue(responseDTO.getTotalAmount().compareTo(new BigDecimal("2716.20")) == 0);
    }

    @Test
    @Transactional
    public void testCreateOrder_OutOfStock(){

        //When
        orderService.createOrder(validOrderRequestDTO);
        Exception exception = assertThrows(ValidationException.class, () -> orderService.createOrder(validOrderRequestDTO));

        //then
        String expectedMessage = "some books in order have limited or out of stock";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
