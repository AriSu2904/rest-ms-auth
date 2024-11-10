package com.service.authorization.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
@NoArgsConstructor
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<U> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String Id;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    protected LocalDateTime creationDate;

    @LastModifiedDate
    @Column(name = "update_at")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    protected  LocalDateTime updateDate;
}