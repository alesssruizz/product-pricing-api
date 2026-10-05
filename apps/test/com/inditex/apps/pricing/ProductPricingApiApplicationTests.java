package com.inditex.apps.pricing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:${random.uuid};DB_CLOSE_DELAY=-1")
@AutoConfigureMockMvc
public abstract class ProductPricingApiApplicationTests {

  @Autowired private MockMvc mockMvc;

  protected void assertResponse(
      String endpoint, Integer expectedStatusCode, String expectedResponse) throws Exception {
    ResultMatcher response =
        expectedResponse.isEmpty() ? content().string("") : content().json(expectedResponse);

    mockMvc.perform(get(endpoint)).andExpect(status().is(expectedStatusCode)).andExpect(response);
  }

  protected void assertStatusWithoutErrorCode(String endpoint, Integer expectedStatusCode)
      throws Exception {
    mockMvc
        .perform(get(endpoint))
        .andExpect(status().is(expectedStatusCode))
        .andExpect(jsonPath("$.errorCode").doesNotExist());
  }

  protected void assertJsonPathAbsent(String endpoint, String path) throws Exception {
    mockMvc
        .perform(get(endpoint))
        .andExpect(status().isOk())
        .andExpect(jsonPath(path).doesNotExist());
  }

  protected ResultActions perform(RequestBuilder request) throws Exception {
    return mockMvc.perform(request);
  }

  protected ResultActions postBody(String endpoint, String body) throws Exception {
    return perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(body));
  }
}
