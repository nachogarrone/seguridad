package com.ucu.seguridad.services.impl;

import com.ucu.seguridad.dao.MessagesRepository;
import com.ucu.seguridad.models.MessagesEntity;
import com.ucu.seguridad.services.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by nachogarrone on 10/27/16.
 */
@Service
public class MessagesServiceImpl implements MessagesService {

    @Autowired
    private MessagesRepository messagesRepository;

    @Override
    public List<MessagesEntity> findAllMessages() {
        return messagesRepository.findAll();
    }

    @Override
    public MessagesEntity save(MessagesEntity message) {
        return messagesRepository.save(message);
    }

    @Override
    public MessagesEntity findById(Long id) {
        return messagesRepository.findOne(id);
    }

    @Override
    public List<MessagesEntity> findByAuthor(String author) {
        return messagesRepository.findByAuthor(author);
    }
}
