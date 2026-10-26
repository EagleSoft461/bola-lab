
package com.aliorhan.bola_lab.document;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DocumentRepository
        extends JpaRepository<DocumentRecord, UUID> {

    Optional<DocumentRecord> findByIdAndOwnerId(
        UUID id,
        UUID ownerId
    );
}
