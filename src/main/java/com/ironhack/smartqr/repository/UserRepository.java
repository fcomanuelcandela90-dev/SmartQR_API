package com.ironhack.smartqr.repository;

import com.ironhack.smartqr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
