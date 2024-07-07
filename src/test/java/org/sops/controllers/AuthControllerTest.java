package org.sops.controllers;

import org.junit.jupiter.api.Test;
import org.sops.services.UserService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AuthControllerTest extends BaseControllerTest {
    @MockBean
    UserService userService;

    @Test
    void register() throws Exception {
        when(userService.addUser(any())).thenReturn(posterEntity);
        mockMvc.perform(post("/auth/register/POSTER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"User8\", \"password\": \"password8\", \"email\": \"user8@mail.org\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void login() throws Exception {
        when(userService.loadUserByUsername(any())).thenReturn(posterEntity);
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"User1\", \"password\": \"password1\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void login_wrongPassword() throws Exception {
        when(userService.loadUserByUsername(any())).thenReturn(posterEntity);
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"User1\", \"password\": \"password\"}"))
                .andExpect(status().isForbidden());
    }

}