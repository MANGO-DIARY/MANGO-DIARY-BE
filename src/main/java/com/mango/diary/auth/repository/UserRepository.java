package com.mango.diary.auth.repository;


import com.mango.diary.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUserEmail(String userEmail);
    Optional<User> findByUserEmail(String userEmail);
    Optional<User> findById(Long id);
}
