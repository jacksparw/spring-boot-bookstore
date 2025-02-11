package com.assignment.bookstore.service;

import com.assignment.bookstore.beans.domain.*;
import com.assignment.bookstore.beans.dto.mapper.BookMapper;
import com.assignment.bookstore.beans.dto.order.BookOrderLineDTO;
import com.assignment.bookstore.beans.dto.order.OrderRequestDTO;
import com.assignment.bookstore.beans.dto.order.OrderResponseDTO;
import com.assignment.bookstore.exception.ValidationException;
import com.assignment.bookstore.repository.BookRepository;
import com.assignment.bookstore.repository.CustomerRepository;
import com.assignment.bookstore.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.assignment.bookstore.util.MessageConstants.ErrorMessage.BOOK_NOT_FOUND;
import static com.assignment.bookstore.util.MessageConstants.ErrorMessage.LIMITED_OR_OUT_OF_STOCK;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CustomerRepository customerRepository,
                            BookRepository bookRepository,
                            BookMapper bookMapper) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO) {



        Optional<Customer> optionalCustomer = customerRepository
                .findByEmail(requestDTO.getEmail());

        Customer customer;

        if(optionalCustomer.isPresent()){
            customer = optionalCustomer.get();
        }else{
            Customer saveCustomer = new Customer();
            saveCustomer.setCustomerName(requestDTO.getCustomerName());
            saveCustomer.setEmail(requestDTO.getEmail());

            customer = customerRepository.save(saveCustomer);
        }

        Order order = new Order();

        Set<BookOrderLine> bookOrderLines = requestDTO.getBooks()
                .stream()
                .map(this::createBookOrderLine)
                .collect(Collectors.toCollection(HashSet::new));

        order.setCustomer(customer);
        order.setBookOrderLines(bookOrderLines);

        bookOrderLines.forEach(orderLine -> orderLine.setOrder(order));

        Order savedOrder = orderRepository.saveAndFlush(order);

       return mapToOrderResponseDTO(savedOrder);
    }

    private BookOrderLine createBookOrderLine(BookOrderLineDTO bookOrderLineDTO) {
        BookOrderLine orderLine = new BookOrderLine();

        Book book = bookRepository
                .findBookByIsbn(bookOrderLineDTO.getIsbn())
                .orElseThrow(() -> new ValidationException(BOOK_NOT_FOUND));

        if (book.getStock().getBookCount() < bookOrderLineDTO.getOrderQuantity()) {
            throw new ValidationException(LIMITED_OR_OUT_OF_STOCK);
        }

        Stock stock = book.getStock();
        stock.setBookCount(stock.getBookCount() - bookOrderLineDTO.getOrderQuantity());
        orderLine.setBook(book);
        orderLine.setOrderQuantity(bookOrderLineDTO.getOrderQuantity());
        return orderLine;
    }

    private OrderResponseDTO mapToOrderResponseDTO(Order savedOrder) {

        OrderResponseDTO responseDTO = new OrderResponseDTO();

        responseDTO.setCustomerName(savedOrder.getCustomer().getCustomerName());

        responseDTO.setTotalAmount(savedOrder
                .getBookOrderLines()
                .stream()
                .map(bookOrderLine -> new BigDecimal(bookOrderLine.getOrderQuantity()).multiply(bookOrderLine.getBook().getPrice()))
                .reduce(BigDecimal::add).get());

        responseDTO.setBookDTOList(savedOrder
                .getBookOrderLines()
                .stream()
                .map(bookOrderLine -> bookMapper.bookToBookDTO(bookOrderLine.getBook()))
                .collect(Collectors.toList()));

        return responseDTO;
    }
}
