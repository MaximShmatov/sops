package org.sops.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sops.database.repositories.OrderRepository;
import org.sops.dto.StatisticDto;
import org.sops.types.OrderStatusType;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final OrderRepository orderRepository;

    public StatisticDto getStatistic() {
        int ready = orderRepository.countAllByStatus(OrderStatusType.READY);
        int inProgress = orderRepository.countAllByStatus(OrderStatusType.IN_PROCESS);
        int processed = orderRepository.countAllByStatus(OrderStatusType.PROCESSED);
        return new StatisticDto(ready, inProgress, processed);
    }

}
