package org.sops.controllers;

import org.sops.BaseTest;
import org.sops.TestSopsApplication;
import org.sops.services.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = {TestSopsApplication.class})
@AutoConfigureMockMvc
public class BaseControllerTest extends BaseTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected JwtTokenService jwtTokenService;

}
