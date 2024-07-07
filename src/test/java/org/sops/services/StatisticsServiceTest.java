package org.sops.services;

import org.junit.jupiter.api.Test;
import org.sops.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class StatisticsServiceTest extends BaseTest {
    @Autowired
    StatisticsService statisticsService;

    @Test
    void getStatistic() {
        var statistic = statisticsService.getStatistic();
        assertNotNull(statistic);
    }

}