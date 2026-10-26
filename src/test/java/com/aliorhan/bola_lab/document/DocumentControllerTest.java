
package com.aliorhan.bola_lab.document;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DocumentRepository documentRepository;

    private static final UUID OWNER_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    private static final UUID OTHER_USER_ID =
            UUID.fromString("22222222-2222-2222-2222-222222222222");

    private String basicAuth(UUID userId) {
        String credentials = userId + ":lab-password";
        String encoded = Base64.getEncoder().encodeToString(
                credentials.getBytes(StandardCharsets.UTF_8)
        );
        return "Basic " + encoded;
    }

    @Test
    void unauthenticatedUserGets401() throws Exception {
        mockMvc.perform(get("/api/documents/{id}", UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void ownerCanReadTheirDocument() throws Exception {
        UUID documentId = UUID.randomUUID();

        documentRepository.save(
                new DocumentRecord(documentId, OWNER_ID, "Private document")
        );

        mockMvc.perform(
                get("/api/documents/{id}", documentId)
                        .header("Authorization", basicAuth(OWNER_ID))
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Private document")));
    }

    @Test
    void anotherUserCannotReadTheDocument() throws Exception {
        UUID documentId = UUID.randomUUID();

        documentRepository.save(
                new DocumentRecord(documentId, OWNER_ID, "Private document")
        );

        mockMvc.perform(
                get("/api/documents/{id}", documentId)
                        .header("Authorization", basicAuth(OTHER_USER_ID))
        )
                .andExpect(status().isNotFound());
    }

    @Test
    void insecureEndpointAllowsAnotherUserToReadDocument() throws Exception {
        UUID documentId = UUID.randomUUID();

        documentRepository.save(
                new DocumentRecord(documentId, OWNER_ID, "Private document")
        );

        mockMvc.perform(
                get("/api/lab/insecure/documents/{id}", documentId)
                        .header("Authorization", basicAuth(OTHER_USER_ID))
        )
		.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Private document")));
    }

    @Test
    void secureEndpointBlocksAnotherUserFromReadingDocument() throws Exception {
        UUID documentId = UUID.randomUUID();

        documentRepository.save(
                new DocumentRecord(documentId, OWNER_ID, "Private document")
        );

        mockMvc.perform(
                get("/api/documents/{id}", documentId)
                        .header("Authorization", basicAuth(OTHER_USER_ID))
        )
		.andDo(print())
                .andExpect(status().isNotFound());
    }

}
