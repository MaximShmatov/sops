package org.sops.controllers;

import lombok.RequiredArgsConstructor;
import org.sops.dto.StatisticDto;
import org.sops.services.StatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/statistics")
public class StatisticsController extends BaseController {
    private final StatisticsService statisticsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public StatisticDto getStatistic() {
        return statisticsService.getStatistic();
    }

}
