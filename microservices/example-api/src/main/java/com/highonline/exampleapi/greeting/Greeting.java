package com.highonline.exampleapi.greeting;

import com.fasterxml.uuid.Generators;
import com.highonline.common.DomainException;
import io.hypersistence.tsid.TSID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Greeting {

    @Id
    private Long id; // TSID: ordenável por tempo, exposto como string na API
    private UUID traceId; // UUIDv7
    private String message;
    private String recipientEmail;
    private OffsetDateTime createdAt;

    public Greeting(String message, String recipientEmail) {
        if (!EmailValidator.getInstance().isValid(recipientEmail)) {
            throw new DomainException("Invalid recipient e-mail: " + recipientEmail);
        }
        this.id = TSID.fast().toLong();
        this.traceId = Generators.timeBasedEpochGenerator().generate();
        this.message = message;
        this.recipientEmail = recipientEmail;
        this.createdAt = OffsetDateTime.now();
    }
}
