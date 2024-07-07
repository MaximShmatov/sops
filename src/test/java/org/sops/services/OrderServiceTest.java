package org.sops.services;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.sops.BaseTest;
import org.sops.database.entities.OrderEntity;
import org.sops.database.repositories.OrderRepository;
import org.sops.database.repositories.UserRepository;
import org.sops.dto.PlaceOrderRequest;
import org.sops.types.OrderStatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest extends BaseTest {
    final PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest("Title1", "Desk1");
    final Authentication posterAuthToken = new UsernamePasswordAuthenticationToken(posterEntity.getUsername(),posterEntity.getPassword(), posterEntity.getAuthorities());
    final Authentication processorAuthToken = new UsernamePasswordAuthenticationToken(processorEntity.getUsername(),processorEntity.getPassword(), processorEntity.getAuthorities());
    @Autowired
    private OrderService orderService;
    @MockBean
    OrderRepository orderRepository;
    @MockBean
    UserRepository userRepository;
    @Captor
    ArgumentCaptor<OrderEntity> orderEntityArgumentCaptor;

    @Test
    void placeAndOrder() {
        SecurityContextHolder.getContext().setAuthentication(posterAuthToken);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(posterEntity));
        orderService.placeAndOrder(placeOrderRequest);
        verify(orderRepository, times(1)).save(orderEntityArgumentCaptor.capture());
        assertEquals(orderEntityArgumentCaptor.getValue().getStatus(), OrderStatusType.READY);
        assertEquals(orderEntityArgumentCaptor.getValue().getCreatedBy().getUsername(), "User1");
    }

    @Test
    void getOrderById() {
        SecurityContextHolder.getContext().setAuthentication(posterAuthToken);
        orderService.getOrderById(1);
        verify(orderRepository, times(1)).findByIdAndCreatedBy_Username(any(Integer.class), any());
        SecurityContextHolder.getContext().setAuthentication(processorAuthToken);
        orderService.getOrderById(1);
        verify(orderRepository, times(1)).findById(any(Integer.class));
    }

    @Test
    void getAllOrders() {
        SecurityContextHolder.getContext().setAuthentication(posterAuthToken);
        orderService.getAllOrders();
        verify(orderRepository, times(1)).findAllByCreatedBy_Username(any(String.class));
        SecurityContextHolder.getContext().setAuthentication(processorAuthToken);
        orderService.getAllOrders();
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getReadyToProcess() {
        orderService.getReadyToProcess();
        verify(orderRepository, times(1)).findAllByStatus(OrderStatusType.READY);
    }

    @Test
    void startOrderProcessing() {
        when(orderRepository.findById(any())).thenReturn(Optional.of(new OrderEntity()));
        orderService.startOrderProcessing(1);
        verify(orderRepository, times(1)).save(orderEntityArgumentCaptor.capture());
        assertEquals(orderEntityArgumentCaptor.getValue().getStatus(), OrderStatusType.IN_PROCESS);
    }

    @Test
    void completeOrderProcessing() {
        when(orderRepository.findById(any())).thenReturn(Optional.of(new OrderEntity()));
        orderService.completeOrderProcessing(1);
        verify(orderRepository, times(1)).save(orderEntityArgumentCaptor.capture());
        assertEquals(orderEntityArgumentCaptor.getValue().getStatus(), OrderStatusType.PROCESSED);
    }

}