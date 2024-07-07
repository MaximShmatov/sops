package org.sops.controllers;

import org.junit.jupiter.api.Test;
import org.sops.database.entities.OrderEntity;
import org.sops.database.repositories.UserRepository;
import org.sops.dto.PlaceOrderRequest;
import org.sops.services.OrderService;
import org.sops.types.OrderStatusType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest extends BaseControllerTest {
    final OrderEntity orderEntity = new OrderEntity(1, "title1", "dec1", OrderStatusType.READY, posterEntity, null, OffsetDateTime.now(), null);
    @MockBean
    OrderService orderService;
    @MockBean
    UserRepository userRepository;

    @Test
    void placeAndOrder() throws Exception {
        when(orderService.placeAndOrder(any(PlaceOrderRequest.class))).thenReturn(orderEntity);
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(posterEntity));
        mockMvc.perform(post("/orders/place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtTokenService.generateToken(posterEntity))
                        .content("{\"title\": \"title1\", \"description\": \"desc1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("READY"))
                .andExpect(jsonPath("$.title").value("title1"));
    }

    @Test
    void getOrderById() throws Exception {
        when(orderService.getOrderById(any())).thenReturn(Optional.of(orderEntity));
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(posterEntity));
        mockMvc.perform(get("/orders/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtTokenService.generateToken(posterEntity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title1"));
    }

    @Test
    void getAllOrders() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of(orderEntity));
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(posterEntity));
        mockMvc.perform(get("/orders/get-all")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtTokenService.generateToken(posterEntity)))
                .andExpect(status().isOk());
    }

    @Test
    void getReadyToProcess() throws Exception {
        when(orderService.getReadyToProcess()).thenReturn(List.of(orderEntity));
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(processorEntity));
        mockMvc.perform(get("/orders/ready")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtTokenService.generateToken(processorEntity)))
                .andExpect(status().isOk());
    }

    @Test
    void startOrderProcessing() throws Exception {
        orderEntity.setStatus(OrderStatusType.IN_PROCESS);
        when(orderService.startOrderProcessing(any())).thenReturn(Optional.of(orderEntity));
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(processorEntity));
        mockMvc.perform(post("/orders/process/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtTokenService.generateToken(processorEntity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROCESS"));
    }

    @Test
    void completeOrderProcessing() throws Exception {
        orderEntity.setStatus(OrderStatusType.PROCESSED);
        when(orderService.completeOrderProcessing(any())).thenReturn(Optional.of(orderEntity));
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(processorEntity));
        mockMvc.perform(post("/orders/complete/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtTokenService.generateToken(processorEntity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PROCESSED"));
    }

}