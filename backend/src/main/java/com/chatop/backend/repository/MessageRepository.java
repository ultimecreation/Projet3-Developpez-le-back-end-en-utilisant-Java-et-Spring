package com.chatop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatop.backend.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

}
