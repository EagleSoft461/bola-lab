
package com.aliorhan.bola_lab.document;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DocumentRepositoryTest {

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    void ownerCanFindTheirDocument() {
        UUID ownerId = UUID.randomUUID();
        UUID documentId = UUID.randomUUID();

        documentRepository.save(
            new DocumentRecord(
                documentId,
                ownerId,
                "Private document"
            )
        );

        var result = documentRepository
            .findByIdAndOwnerId(documentId, ownerId);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle())
            .isEqualTo("Private document");
    }

    @Test
    void differentUserCannotFindOwnersDocument() {
        UUID ownerId = UUID.randomUUID();
        UUID anotherUserId = UUID.randomUUID();
        UUID documentId = UUID.randomUUID();

        documentRepository.save(
            new DocumentRecord(
                documentId,
                ownerId,
                "Private document"
            )
        );

        var result = documentRepository
            .findByIdAndOwnerId(documentId, anotherUserId);

        assertThat(result).isEmpty();
    }
}
