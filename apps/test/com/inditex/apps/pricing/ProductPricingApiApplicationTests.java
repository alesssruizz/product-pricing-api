package com.inditex.apps.pricing;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class ProductPricingApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    protected void assertResponse(
        String endpoint,
        Integer expectedStatusCode,
        String expectedResponse
    ) throws Exception {
        ResultMatcher response = expectedResponse.isEmpty()
            ? content().string("")
            : content().json(expectedResponse);

        mockMvc
            .perform(get(endpoint))
            .andExpect(status().is(expectedStatusCode))
            .andExpect(response);
    }

    protected void assertResponse(
        String endpoint,
        Integer expectedStatusCode,
        String expectedResponse,
        HttpHeaders headers
    ) throws Exception {
        ResultMatcher response = expectedResponse.isEmpty()
            ? content().string("")
            : content().json(expectedResponse);

        mockMvc
            .perform(get(endpoint).headers(headers))
            .andExpect(status().is(expectedStatusCode))
            .andExpect(response);
    }
}
