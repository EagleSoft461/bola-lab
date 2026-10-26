
package com.aliorhan.bola_lab.document;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "documents")
public class DocumentRecord {

    @Id
    private UUID id;

    private UUID ownerId;

    private String title;

    protected DocumentRecord() {
    }

    public DocumentRecord(UUID id, UUID ownerId, String title) {
        this.id = id;
        this.ownerId = ownerId;
        this.title = title;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public String getTitle() {
        return title;
    }
}
