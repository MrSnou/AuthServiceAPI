package com.project.authapi.authserviceapi.repository;

import com.project.authapi.authserviceapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    // JpaRepository<User, Long>
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
