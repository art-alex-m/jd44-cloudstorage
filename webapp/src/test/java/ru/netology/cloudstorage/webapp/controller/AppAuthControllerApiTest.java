package ru.netology.cloudstorage.webapp.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.netology.cloudstorage.contracts.auth.service.AuthTokenManager;
import ru.netology.cloudstorage.webapp.config.SecurityDisabledConfiguration;
import ru.netology.cloudstorage.webapp.factory.AuthenticationTestFactory;
import ru.netology.cloudstorage.webapp.model.AppAuthTokenProperties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("security-disable")
@WebMvcTest(AppAuthController.class)
@Import({AuthenticationTestFactory.class, SecurityDisabledConfiguration.class})
class AppAuthControllerApiTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AppAuthTokenProperties appAuthTokenProperties;

    @Autowired
    private AuthenticationTestFactory authenticationTestFactory;

    @MockBean
    private AuthTokenManager appAuthTokenManager;


    @Test
    void givenSuccessCredentials_whenLogin_thenSuccessToken() throws Exception {
        String headerNameExpression = "$." + appAuthTokenProperties.getHeaderName();
        BDDMockito.given(appAuthTokenManager.createToken(BDDMockito.any(Authentication.class)))
                .willReturn(authenticationTestFactory.getTestTokenValue());

        mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(String.format(
                                "{\"login\":\"%s\",\"password\":\"%s\"}",
                                authenticationTestFactory.getTestUsername(),
                                authenticationTestFactory.getTestUserPassword()
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath(headerNameExpression, is(authenticationTestFactory.getTestTokenValue())));
    }

    @Test
    void givenTokenHeader_whenLogout_thenSuccess() throws Exception {
        ArgumentCaptor<String> tokenValueCaptor = ArgumentCaptor.forClass(String.class);

        mvc.perform(post("/logout")
                        .header(appAuthTokenProperties.getHeaderName(), authenticationTestFactory.getTestTokenValue()
                        ))
                .andExpect(status().isOk());
        BDDMockito.verify(appAuthTokenManager, BDDMockito.times(1)).revokeToken(tokenValueCaptor.capture());
        assertThat(tokenValueCaptor.getValue(), is(authenticationTestFactory.getTestTokenValue()));
    }
}