package org.sops.controllers;

import org.junit.jupiter.api.Test;
import org.sops.database.entities.UserEntity;
import org.sops.dto.StatisticDto;
import org.sops.services.StatisticsService;
import org.sops.services.UserService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StatisticsControllerTest extends BaseControllerTest {
    final StatisticDto statisticDto = new StatisticDto(10,3,3);
    @MockBean
    StatisticsService statisticsService;
    @MockBean
    UserService userService;

    @Test
    void getStatistics() throws Exception {
        when(statisticsService.getStatistic()).thenReturn(statisticDto);
                when(userService.loadUserByUsername(any(String.class))).thenReturn(posterEntity);
        mockMvc.perform(get("/statistics")
                        .header("Authorization", "Bearer " + jwtTokenService.generateToken(posterEntity))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getStatistic_no_auth() throws Exception {
        when(statisticsService.getStatistic()).thenReturn(statisticDto);
        when(userService.loadUserByUsername(any(String.class))).thenReturn(new UserEntity());
        mockMvc.perform(get("/statistics")
                        .header("Authorization", "Bearer " + jwtTokenService.generateToken(posterEntity))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}