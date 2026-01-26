package io.github.enikolas.taskmanagement.infrastructure.persistence.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditableFields {
    @CreatedDate
    @Column(nullable = false)
    protected Instant createdAt;

    @CreatedBy
    protected String createdBy;

    @LastModifiedDate
    @Column(nullable = false)
    protected Instant updatedAt;

    @LastModifiedBy
    @Column
    protected String updatedBy;
}
