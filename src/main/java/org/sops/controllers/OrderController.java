package org.sops.controllers;

import lombok.RequiredArgsConstructor;
import org.sops.database.entities.OrderEntity;
import org.sops.dto.PlaceOrderRequest;
import org.sops.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/orders")
public class OrderController extends BaseController {
    private final OrderService orderService;

    @PostMapping("/place")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('POSTER')")
    public OrderEntity placeAndOrder(@RequestBody @Validated PlaceOrderRequest request) {
        return orderService.placeAndOrder(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderEntity getOrderById(@PathVariable Integer id) {
        return orderService.getOrderById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderEntity> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/ready")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PROCESSOR')")
    public List<OrderEntity> getReadyToProcess() {
        return orderService.getReadyToProcess();
    }

    @PostMapping("/process/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PROCESSOR')")
    public OrderEntity startOrderProcessing(@PathVariable Integer id) {
        return orderService.startOrderProcessing(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/complete/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PROCESSOR')")
    public OrderEntity completeOrderProcessing(@PathVariable Integer id) {
        return orderService.completeOrderProcessing(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
