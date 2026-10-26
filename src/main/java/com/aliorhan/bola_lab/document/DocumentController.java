
package com.aliorhan.bola_lab.document;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/api/documents/{documentId}")
    public DocumentRecord getDocument(
            @PathVariable UUID documentId,
            Authentication authentication
    ) {
        UUID authenticatedUserId;

        try {
            authenticatedUserId =
                    UUID.fromString(authentication.getName());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Authenticated identity is not a valid user ID"
            );
        }

        return documentService
                .findDocumentForOwner(documentId, authenticatedUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Document not found"
                ));
    }


    
    // INSECURE: Demonstrates a BOLA vulnerability.
    @GetMapping("/api/lab/insecure/documents/{documentId}")
    public DocumentRecord getDocumentInsecurely(
            @PathVariable UUID documentId
    ) {
        return documentService
                .findDocumentByIdInsecurely(documentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Document not found"
                ));
    }

}
