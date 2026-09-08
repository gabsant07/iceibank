package aula.iceibank.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InternalTransferSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRejectInterAgencyCreditWithoutInternalKey() throws Exception {
        String body = "{\"transactionId\":\"" + UUID.randomUUID()
                + "\",\"sourceAccount\":4,\"destinationAccount\":3,\"amount\":10,"
                + "\"sourceAgency\":1,\"lamportTimestamp\":1}";

        mockMvc.perform(post("/api/transfers/internal/credit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());
    }
}
