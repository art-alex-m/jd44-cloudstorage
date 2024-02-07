package ru.netology.cloudstorage.webapp.service;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.netology.cloudstorage.contracts.auth.service.AuthTokenManager;
import ru.netology.cloudstorage.contracts.core.model.TraceId;
import ru.netology.cloudstorage.contracts.trace.factory.TraceIdFactory;
import ru.netology.cloudstorage.contracts.trace.model.TraceIdHeader;
import ru.netology.cloudstorage.core.model.CoreTraceId;
import ru.netology.cloudstorage.webapp.config.SecurityDisabledConfiguration;
import ru.netology.cloudstorage.webapp.controller.AppAuthController;
import ru.netology.cloudstorage.webapp.factory.AuthenticationTestFactory;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("security-disable")
@WebMvcTest(AppAuthController.class)
@Import({AuthenticationTestFactory.class, SecurityDisabledConfiguration.class})
class AppTraceIdInterceptorMvcTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthTokenManager authTokenManager;

    @MockBean
    private TraceIdFactory traceIdFactory;

    @Test
    void whenAny_whileInteracting_thenResponseTraceIdHeaders() throws Exception {
        TraceId expectedTraceId = new CoreTraceId(1759972524539924596L,
                UUID.fromString("3ebd8de5-23ec-4fb6-b25d-bf6adf6bc074"));
        BDDMockito.given(traceIdFactory.create()).willReturn(expectedTraceId);

        mvc.perform(post("/logout"))
                .andExpect(status().isOk())
                .andExpect(header().longValue(TraceIdHeader.ID, expectedTraceId.getId()))
                .andExpect(header().string(TraceIdHeader.UUID, expectedTraceId.getUuid().toString()));
        BDDMockito.verify(traceIdFactory, BDDMockito.times(1)).create();
        BDDMockito.verifyNoMoreInteractions(traceIdFactory);
    }

    @Test
    void whenSendHeader_whileInteracting_thenResponseSameTraceIdHeaders() throws Exception {
        String uuid = "3ebd8de5-23ec-4fb6-b25d-bf6adf6bc074";
        TraceId expectedTraceId = new CoreTraceId(1759972524539924596L, UUID.fromString(uuid));
        BDDMockito.given(traceIdFactory.create(uuid)).willReturn(expectedTraceId);

        mvc.perform(post("/logout").header(TraceIdHeader.UUID, uuid))
                .andExpect(status().isOk())
                .andExpect(header().longValue(TraceIdHeader.ID, expectedTraceId.getId()))
                .andExpect(header().string(TraceIdHeader.UUID, expectedTraceId.getUuid().toString()));
        BDDMockito.verify(traceIdFactory, BDDMockito.times(1)).create();
        BDDMockito.verify(traceIdFactory, BDDMockito.times(1)).create(uuid);
        BDDMockito.verifyNoMoreInteractions(traceIdFactory);
    }

    @Test
    void whenException_whileInteracting_thenResponseTraceIdHeaders() throws Exception {
        TraceId expectedTraceId = new CoreTraceId(1759972524539924596L,
                UUID.fromString("3ebd8de5-23ec-4fb6-b25d-bf6adf6bc074"));
        BDDMockito.given(traceIdFactory.create()).willReturn(expectedTraceId);
        BDDMockito.given(authTokenManager.revokeToken(BDDMockito.any())).willThrow(new UsernameNotFoundException(""));

        mvc.perform(post("/logout"))
                .andExpect(status().is4xxClientError())
                .andExpect(header().longValue(TraceIdHeader.ID, expectedTraceId.getId()))
                .andExpect(header().string(TraceIdHeader.UUID, expectedTraceId.getUuid().toString()));
        BDDMockito.verify(traceIdFactory, BDDMockito.times(1)).create();
        BDDMockito.verifyNoMoreInteractions(traceIdFactory);
    }
}
