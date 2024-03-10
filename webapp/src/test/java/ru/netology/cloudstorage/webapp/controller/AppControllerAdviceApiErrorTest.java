package ru.netology.cloudstorage.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.netology.cloudstorage.contracts.auth.service.AuthTokenManager;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileException;
import ru.netology.cloudstorage.contracts.core.exception.CloudFileExceptionCode;
import ru.netology.cloudstorage.contracts.core.model.TraceId;
import ru.netology.cloudstorage.webapp.config.SecurityDisabledConfiguration;
import ru.netology.cloudstorage.webapp.config.TestLoginRequest;
import ru.netology.cloudstorage.webapp.factory.AuthenticationTestFactory;
import ru.netology.cloudstorage.webapp.service.AppTraceIdMDCService;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("security-disable")
@WebMvcTest(AppAuthController.class)
@Import({AuthenticationTestFactory.class, SecurityDisabledConfiguration.class, AppTraceIdMDCService.class})
class AppControllerAdviceApiErrorTest {
    @Autowired
    ObjectMapper jsonMapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AuthenticationTestFactory authenticationTestFactory;

    @MockBean
    private AuthTokenManager appAuthTokenManager;

    @Test
    void givenWrongCredentials_whenLogin_thenDabRequest() throws Exception {
        TestLoginRequest request = new TestLoginRequest(authenticationTestFactory.getTestUsername(), "wrong-password");

        mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(greaterThan(0L))))
                .andExpect(jsonPath("$.message", containsString("Invalid username or password")));
    }

    @Test
    void givenMalformedRequest_whenLogin_thenDabRequest() throws Exception {
        mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"login\":\"some-user@login\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", containsString("Field 'password': ")))
                .andExpect(jsonPath("$.id", is(greaterThan(0L))));
    }

    @Test
    void givenSomeException_whenLogin_thenServerException() throws Exception {
        RuntimeException runtimeException = new RuntimeException("Runtime exception");
        given(appAuthTokenManager.createToken(any(Authentication.class))).willThrow(runtimeException);
        TestLoginRequest request = new TestLoginRequest(authenticationTestFactory.getTestUsername(),
                authenticationTestFactory.getTestUserPassword());

        mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(greaterThan(0L))))
                .andExpect(jsonPath("$.message", containsString(runtimeException.getMessage())));
    }

    @Test
    void givenCloudFieException_whenLogin_thenBadRequest() throws Exception {
        TraceId traceId = mock(TraceId.class);
        CloudFileException cloudFileException = new CloudFileException(CloudFileExceptionCode.FILE_NOT_FOUND_ERROR,
                traceId);
        given(appAuthTokenManager.createToken(any(Authentication.class))).willThrow(cloudFileException);
        TestLoginRequest request = new TestLoginRequest(authenticationTestFactory.getTestUsername(),
                authenticationTestFactory.getTestUserPassword());

        mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(greaterThan(0L))))
                .andExpect(jsonPath("$.message", containsString(cloudFileException.getMessage())));
    }
}
