package com.chatop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatop.backend.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByEmail(String email);
}
