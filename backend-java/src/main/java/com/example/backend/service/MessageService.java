package com.example.backend.service;

import com.example.backend.model.Message;
import com.example.backend.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final LoggingService loggingService;

    @Autowired
    public MessageService(MessageRepository messageRepository, LoggingService loggingService) {
        this.messageRepository = messageRepository;
        this.loggingService = loggingService;
    }

    public Message saveMessage(String messageText) {
        Message message = new Message(messageText, LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);
        loggingService.logToRedis("db_insert", "Message saved: " + 
                (messageText.length() > 30 ? messageText.substring(0, 30) + "..." : messageText));
        return savedMessage;
    }

    public List<Message> getAllMessages() {
        List<Message> messages = messageRepository.findAll(
                Sort.by(Sort.Direction.DESC, "createdAt"));
        loggingService.logToRedis("db_select", "Retrieved " + messages.size() + " messages");
        return messages;
    }
} 