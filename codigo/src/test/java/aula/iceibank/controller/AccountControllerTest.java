package aula.iceibank.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateDepositAndReadHistoryWithToken() throws Exception {
        String token = login();
        mockMvc.perform(post("/api/accounts")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountNumber\":30,\"holderName\":\"Test User\",\"initialBalance\":100}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.agencyId").value(0));

        mockMvc.perform(post("/api/accounts/30/deposits")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":50}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(150));

        mockMvc.perform(get("/api/accounts/30/history"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/accounts/30/history")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("DEPOSIT"));
    }

    @Test
    void shouldCompleteLocalTransfer() throws Exception {
        String token = login();
        createAccount(60, 200, token);
        createAccount(63, 20, token);

        mockMvc.perform(post("/api/transfers")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sourceAccount\":60,\"destinationAccount\":63,\"amount\":75}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("LOCAL_TRANSFER"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        mockMvc.perform(get("/api/accounts/60")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(125));

        mockMvc.perform(get("/api/accounts/63")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(95));
    }

    private void createAccount(long number, int balance, String token) throws Exception {
        mockMvc.perform(post("/api/accounts")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountNumber\":" + number
                                + ",\"holderName\":\"Test User\",\"initialBalance\":" + balance + "}"))
                .andExpect(status().isCreated());
    }

    private String login() throws Exception {
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"admin123\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JsonNode body = objectMapper.readTree(response);
        return body.get("token").asText();
    }
}
