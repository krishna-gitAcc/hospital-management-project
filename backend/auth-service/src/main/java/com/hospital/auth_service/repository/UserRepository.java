package com.hospital.auth_service.repository;

import com.hospital.auth_service.entity.User;
import com.hospital.common.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail (String email);
    long countByRole(UserRole role);
}
