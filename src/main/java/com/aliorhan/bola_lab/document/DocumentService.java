
package com.aliorhan.bola_lab.document;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Optional<DocumentRecord> findDocumentForOwner(
            UUID documentId,
            UUID authenticatedUserId
    ) {
        return documentRepository.findByIdAndOwnerId(
                documentId,
                authenticatedUserId
        );
    }

    // INSECURE: Does not check document ownership.
    public Optional<DocumentRecord> findDocumentByIdInsecurely(
            UUID documentId
    ) {
        return documentRepository.findById(documentId);
    }

}
