package cz.ff.jsframework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JsFrameworkController.class)
@MockBeans({@MockBean(JsFrameworkRepository.class), @MockBean(JsFrameworkMapper.class)})
public class JsFrameworkMockMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JsFrameworkService jsFrameworkService;

    private static final String NOT_ALLOWED_CHARACTER = "%";

    private static final String JSON_JS_FRAMEWORK = """
              {
                "name": "%s",
                "version": "%s",
                "deprecationDate": "2024-04-26",
                "hypeLevel": 1
              }
            """;

    @BeforeEach
    public void setUp() {
        doReturn(JsFrameworkService.UpsertResult.CREATED)
                .when(jsFrameworkService).createOrUpdateJsFramework(anyString(), anyString(), any());
    }

    @Test
    public void testWrongJsFrameworkNameFormatReturns4xx() throws Exception {
        String jsFrameworkName = ":foo";
        String version = "1.0.0";

        MockHttpServletRequestBuilder createPerson = put("/js-frameworks/%s/%s".formatted(jsFrameworkName, version))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_JS_FRAMEWORK.formatted(jsFrameworkName, version))
                ;

        mockMvc.perform(createPerson)
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testWrongJsFrameworkVersionFormatReturns4xx() throws Exception {
        String jsFrameworkName = "foo";
        String version = "1:0.0";

        MockHttpServletRequestBuilder createPerson = put("/js-frameworks/%s/%s".formatted(jsFrameworkName, version))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_JS_FRAMEWORK.formatted(jsFrameworkName, version))
                ;

        mockMvc.perform(createPerson)
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testValidJsFrameworkFormatReturns2xx() throws Exception {
        String jsFrameworkName = "*-+.foo*-+.";
        String version = "1.0.0";

        MockHttpServletRequestBuilder createPerson = put("/js-frameworks/%s/%s".formatted(jsFrameworkName, version))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_JS_FRAMEWORK.formatted(jsFrameworkName, version))
                ;

        mockMvc.perform(createPerson)
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testWrongJsFrameworkNameInUrlQueryReturns4xx() throws Exception {
        String jsFrameworkName = "*-+.foo*-+." + NOT_ALLOWED_CHARACTER;

        MockHttpServletRequestBuilder createPerson = get("/js-frameworks")
                .queryParam("nameContains", jsFrameworkName)
                ;

        mockMvc.perform(createPerson)
                .andExpect(status().isBadRequest());
    }
}
