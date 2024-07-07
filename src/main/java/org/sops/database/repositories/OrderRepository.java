package org.sops.database.repositories;

import org.sops.database.entities.OrderEntity;
import org.sops.types.OrderStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    List<OrderEntity> findAllByStatus(OrderStatusType status);

    int countAllByStatus(OrderStatusType status);

    Optional<OrderEntity> findByIdAndCreatedBy_Username(int id, String createdBy);

    List<OrderEntity> findAllByCreatedBy_Username(String createdBy);

}
