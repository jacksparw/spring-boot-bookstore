package com.assignment.bookstore.controller.order;

import com.assignment.bookstore.TestUtil;
import com.assignment.bookstore.beans.dto.AddressDTO;
import com.assignment.bookstore.beans.dto.CustomerDTO;
import com.assignment.bookstore.beans.dto.book.BookDTO;
import com.assignment.bookstore.beans.dto.order.BookOrderLineDTO;
import com.assignment.bookstore.beans.dto.order.OrderRequestDTO;
import com.assignment.bookstore.beans.dto.order.OrderResponseDTO;
import com.assignment.bookstore.controller.OrderController;
import com.assignment.bookstore.exception.ValidationException;
import com.assignment.bookstore.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class BuyBookOperation {

    private @Autowired
    MockMvc mockMvc;

    private @MockBean
    OrderService orderService;

    private OrderRequestDTO validOrderRequestDTO;

    @BeforeEach
    public void setup() {

        CustomerDTO dummy_customer = CustomerDTO.builder()
                .customerId(1L)
                .name("Dummy Customer")
                .build();

        BookOrderLineDTO dummyBookOrderLineDTO = BookOrderLineDTO.builder()
                .bookId(1L)
                .orderQuantity(10)
                .build();

        validOrderRequestDTO = OrderRequestDTO.builder()
                .customer(dummy_customer)
                .books(Arrays.asList(dummyBookOrderLineDTO))
                .build();
    }

    @Test
    void testBuyBook() throws Exception {

         BookDTO validBookDTO = BookDTO.builder()
                .title("DummyBook")
                .price(BigDecimal.TEN)
                .isbn("1")
                .build();

        AddressDTO dummyAddressDTO = AddressDTO.builder()
                .addressLine1("Dummy Address")
                .city("DummyCity")
                .state("DummyState")
                .build();

        OrderResponseDTO responseDTO = OrderResponseDTO.builder()
                .customerName("Dummy Customer")
                .dispatchAddress(dummyAddressDTO)
                .bookDTOList(Arrays.asList(validBookDTO))
                .totalAmount(BigDecimal.TEN)
                .build();

        Mockito.when(orderService.createOrder(validOrderRequestDTO)).thenReturn(responseDTO);

        mockMvc.perform(
                post("/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJson(validOrderRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Purchase Details"))
                .andExpect(jsonPath("$.data.customerName").value("Dummy Customer"))
                .andExpect(jsonPath("$.data.dispatchAddress.addressLine1").value("Dummy Address"))
                .andExpect(jsonPath("$.data.dispatchAddress.state").value("DummyState"))
                .andExpect(jsonPath("$.data.dispatchAddress.city").value("DummyCity"))
                .andExpect(jsonPath("$.data.totalAmount").value("10"))
                .andExpect(jsonPath("$.data.bookDTOList[:1].title").value("DummyBook"));
    }

    @Test
    void testBuyBook_missingCustomerId() throws Exception {

        validOrderRequestDTO.getCustomer().setCustomerId(null);

        mockMvc.perform(
                post("/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJson(validOrderRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("customer id is mandatory"));
    }

    @Test
    void testBuyBook_InvalidCustomerId() throws Exception {

        validOrderRequestDTO.getCustomer().setCustomerId(-1L);

        mockMvc.perform(
                post("/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJson(validOrderRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("customer id is invalid"));
    }

    @Test
    void testBuyBook_MissingBookId() throws Exception {

        validOrderRequestDTO.getBooks().get(0).setBookId(null);

        mockMvc.perform(
                post("/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJson(validOrderRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("book id is missing"));
    }

    @Test
    void testBuyBook_InvalidBookId() throws Exception {

        validOrderRequestDTO.getBooks().get(0).setBookId(-1L);

        mockMvc.perform(
                post("/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJson(validOrderRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("invalid bookId"));
    }

    @Test
    void testBuyBook_InvalidOrderQuantity() throws Exception {

        validOrderRequestDTO.getBooks().get(0).setOrderQuantity(-12);

        mockMvc.perform(
                post("/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJson(validOrderRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("invalid quantity must be greater than zero"));
    }

    @Test
    void testBuyBook_MissingOrderQuantity() throws Exception {

        validOrderRequestDTO.getBooks().get(0).setOrderQuantity(null);

        mockMvc.perform(
                post("/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJson(validOrderRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("quantity is missing"));
    }

    @Test
    void testBuyBook_CustomerNotFoundInDB() throws Exception {

        Mockito.when(orderService.createOrder(validOrderRequestDTO)).thenThrow(new ValidationException("Customer Not Found"));

        mockMvc.perform(
                post("/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJson(validOrderRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("Customer Not Found"));
    }

    @Test
    void testBuyBook_BookNotFoundInDB() throws Exception {

        Mockito.when(orderService.createOrder(validOrderRequestDTO)).thenThrow(new ValidationException("Book Not Found"));

        mockMvc.perform(
                post("/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJson(validOrderRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("Book Not Found"));
    }

    @Test
    void testBuyBook_StockError() throws Exception {

        Mockito.when(orderService.createOrder(validOrderRequestDTO)).thenThrow(new ValidationException("some books in order have limited or out of stock"));

        mockMvc.perform(
                post("/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJson(validOrderRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("some books in order have limited or out of stock"));
    }
}
