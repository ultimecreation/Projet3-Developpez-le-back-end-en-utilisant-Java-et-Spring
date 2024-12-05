package com.chatop.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatop.backend.entity.Message;
import com.chatop.backend.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    /**
     * @param message takes a Message object
     * @return the repository response
     */
    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }
}
