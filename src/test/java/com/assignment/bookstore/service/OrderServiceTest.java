package com.assignment.bookstore.service;

import com.assignment.bookstore.beans.dto.CustomerDTO;
import com.assignment.bookstore.beans.dto.order.BookOrderLineDTO;
import com.assignment.bookstore.beans.dto.order.OrderRequestDTO;
import com.assignment.bookstore.beans.dto.order.OrderResponseDTO;
import com.assignment.bookstore.exception.ValidationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderServiceTest {

    private @Autowired OrderService orderService;

    private OrderRequestDTO validOrderRequestDTO;

    @BeforeEach
    public void setup() {

        CustomerDTO dummy_customer = CustomerDTO.builder()
                .customerId(1L)
                .build();

        BookOrderLineDTO dummyBookOrderLineDTO1 = BookOrderLineDTO.builder()
                .bookId(1L)
                .orderQuantity(10)
                .build();

        BookOrderLineDTO dummyBookOrderLineDTO2 = BookOrderLineDTO.builder()
                .bookId(2L)
                .orderQuantity(4)
                .build();

        validOrderRequestDTO = OrderRequestDTO.builder()
                .customer(dummy_customer)
                .books(Arrays.asList(dummyBookOrderLineDTO1, dummyBookOrderLineDTO2))
                .build();
    }

    @Test
    @Order(1)
    public void testCreateOrder_UNKNOWN_Book(){

        //given
        validOrderRequestDTO.getBooks().get(0).setBookId(1000L);

        //When
        Exception exception = assertThrows(ValidationException.class, () -> orderService.createOrder(validOrderRequestDTO));

        //then
        String expectedMessage = "Book Not Found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(2)
    public void testCreateOrder_UNKNOWN_Customer(){

        //given
        validOrderRequestDTO.getCustomer().setCustomerId(10L);

        //When
        Exception exception = assertThrows(ValidationException.class, () ->orderService.createOrder(validOrderRequestDTO));

        //then
        String expectedMessage = "customer not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(3)
    public void testCreateOrder(){

        //When
        OrderResponseDTO responseDTO = orderService.createOrder(validOrderRequestDTO);

        //Then
        assertNotNull(responseDTO);
        assertTrue(responseDTO.getCustomerName().equalsIgnoreCase("Tim"));
        assertTrue(responseDTO.getBookDTOList().size() == 2);
        assertTrue(responseDTO.getDispatchAddress().getCity().equalsIgnoreCase("Ashland"));
        assertTrue(responseDTO.getDispatchAddress().getState().equalsIgnoreCase("NewYork"));
        assertTrue(responseDTO.getTotalAmount().compareTo(new BigDecimal("2716.20")) == 0);
    }

    @Test
    @Order(4)
    public void testCreateOrder_OutOfStock(){

        //When
        Exception exception = assertThrows(ValidationException.class, () ->orderService.createOrder(validOrderRequestDTO));

        //then
        String expectedMessage = "some books in order have limited or out of stock";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
