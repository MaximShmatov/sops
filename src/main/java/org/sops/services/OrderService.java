package org.sops.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.sops.database.entities.OrderEntity;
import org.sops.database.repositories.OrderRepository;
import org.sops.database.repositories.UserRepository;
import org.sops.dto.PlaceOrderRequest;
import org.sops.types.OrderStatusType;
import org.sops.types.RoleType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private static final ScheduledExecutorService taskScheduler = Executors.newSingleThreadScheduledExecutor();
    @Value("${security.signing-key}")
    private String signingKey;
    @Value("${server.lock-timeout}")
    private int lockTimeout;
    private final RedissonClient redissonClient;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderEntity placeAndOrder(PlaceOrderRequest request) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var username = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        var order = new OrderEntity();
        order.setTitle(request.title());
        order.setDescription(request.description());
        order.setCreatedBy(username);
        order.setStatus(OrderStatusType.READY);
        order.setCreatedDate(OffsetDateTime.now());
        return orderRepository.save(order);
    }

    public Optional<OrderEntity> getOrderById(Integer id) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (hasPosterRole(auth)) {
            return orderRepository.findByIdAndCreatedBy_Username(id, auth.getName());
        }
        return orderRepository.findById(id);
    }

    public List<OrderEntity> getAllOrders() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (hasPosterRole(auth)) {
            return orderRepository.findAllByCreatedBy_Username(auth.getName());
        }
        return orderRepository.findAll();
    }

    public List<OrderEntity> getReadyToProcess() {
        return orderRepository.findAllByStatus(OrderStatusType.READY);
    }

    public Optional<OrderEntity> startOrderProcessing(Integer id) {
        return orderRepository.findById(id)
                .filter(order -> !isLockedOrder(id))
                .map(order -> {
                    order.setStatus(OrderStatusType.IN_PROCESS);
                    order.setUpdatedDate(OffsetDateTime.now());
                    return orderRepository.save(order);
                });
    }

    public Optional<OrderEntity> completeOrderProcessing(Integer id) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(OrderStatusType.PROCESSED);
                    order.setUpdatedDate(OffsetDateTime.now());
                    return orderRepository.save(order);
                });
    }

    private boolean hasPosterRole(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(grantedAuth -> grantedAuth.getAuthority().equals(RoleType.POSTER.name()));
    }

    private boolean isLockedOrder(int orderId) {
        var lockKey = "%s_orderId_%s".formatted(signingKey, orderId);
        RLock lock = redissonClient.getLock(lockKey);
        boolean isLockedOrder = lock.isLocked();
        if (!isLockedOrder) {
            lockOrder(orderId, lock);
        }
        return isLockedOrder;
    }

    private void lockOrder(int orderId, RLock lock) {
        lock.lock(lockTimeout, TimeUnit.SECONDS);
        taskScheduler.schedule(() -> {
            orderRepository.findById(orderId)
                    .filter(order -> !order.getStatus().equals(OrderStatusType.PROCESSED))
                    .ifPresent(order -> {
                        order.setStatus(OrderStatusType.READY);
                        orderRepository.save(order);
                    });
        }, lockTimeout, TimeUnit.SECONDS);
    }

}
