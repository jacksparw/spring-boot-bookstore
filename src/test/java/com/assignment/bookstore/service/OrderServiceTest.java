package com.assignment.bookstore.service;

import com.assignment.bookstore.beans.dto.order.BookOrderLineDTO;
import com.assignment.bookstore.beans.dto.order.OrderRequestDTO;
import com.assignment.bookstore.beans.dto.order.OrderResponseDTO;
import com.assignment.bookstore.exception.ValidationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
                .bookId(1L)
                .orderQuantity(10)
                .build();

        BookOrderLineDTO dummyBookOrderLineDTO2 = BookOrderLineDTO.builder()
                .bookId(2L)
                .orderQuantity(4)
                .build();

        validOrderRequestDTO = OrderRequestDTO.builder()
                .customerName("Dummy Customer")
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
    @Order(3)
    public void testCreateOrder_OutOfStock(){

        //When
        Exception exception = assertThrows(ValidationException.class, () ->orderService.createOrder(validOrderRequestDTO));

        //then
        String expectedMessage = "some books in order have limited or out of stock";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
