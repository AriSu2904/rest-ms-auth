package com.service.authorization.repository;

import com.service.authorization.entity.EntityCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EntityCredentialRepository extends JpaRepository<EntityCredential, String> {
    Optional<EntityCredential> findByStudentId(String entityId);
}
