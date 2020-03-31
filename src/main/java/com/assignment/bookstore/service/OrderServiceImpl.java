package com.assignment.bookstore.service;

import com.assignment.bookstore.beans.domain.BookOrderLine;
import com.assignment.bookstore.beans.domain.Customer;
import com.assignment.bookstore.beans.domain.Order;
import com.assignment.bookstore.beans.dto.mapper.AddressMapper;
import com.assignment.bookstore.beans.dto.mapper.BookMapper;
import com.assignment.bookstore.beans.dto.order.BookOrderLineDTO;
import com.assignment.bookstore.beans.dto.order.OrderRequestDTO;
import com.assignment.bookstore.beans.dto.order.OrderResponseDTO;
import com.assignment.bookstore.exception.NoDataFoundException;
import com.assignment.bookstore.repository.BookRepository;
import com.assignment.bookstore.repository.CustomerRepository;
import com.assignment.bookstore.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.assignment.bookstore.util.MessageConstants.ErrorMessage.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AddressMapper addressMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CustomerRepository customerRepository, BookRepository bookRepository, BookMapper bookMapper, AddressMapper addressMapper) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.addressMapper = addressMapper;
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO) {

        Customer customer = customerRepository
                .findById(requestDTO.getCustomer().getCustomerId())
                .orElseThrow(() -> new NoDataFoundException(CUSTOMER_NOT_FOUND));

        Order order = new Order();

        Set<BookOrderLine> bookOrderLines = requestDTO.getBook()
                .parallelStream()
                .map(bookOrderLineDTO -> createBookOrderLine(bookOrderLineDTO, order))
                .collect(Collectors.toCollection(HashSet::new));

        order.setCustomer(customer);
        order.setBookOrderLines(bookOrderLines);

        Order savedOrder = orderRepository.save(order);

       return mapToOrderResponseDTO(savedOrder);
    }

    private BookOrderLine createBookOrderLine(BookOrderLineDTO bookOrderLineDTO, Order order) {
        BookOrderLine orderLine = new BookOrderLine();
        orderLine.setBook(bookRepository
                .findById(bookOrderLineDTO.getBookId())
                .orElseThrow(() -> new NoDataFoundException(BOOK_NOT_FOUND)));
        orderLine.setOrderQuantity(bookOrderLineDTO.getOrderQuantity());
        orderLine.setOrder(order);
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

        responseDTO.setAddressDTO(addressMapper.addressToAddressDTO(savedOrder.getCustomer().getAddress()));

        responseDTO.setBookDTOList(savedOrder
                .getBookOrderLines()
                .stream()
                .map(bookOrderLine -> bookMapper.bookToBookDTO(bookOrderLine.getBook()))
                .collect(Collectors.toList()));

        return responseDTO;
    }
}
