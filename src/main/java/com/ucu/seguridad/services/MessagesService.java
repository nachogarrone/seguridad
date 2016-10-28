package com.ucu.seguridad.services;

import com.ucu.seguridad.models.MessagesEntity;

import java.util.List;

/**
 * Created by nachogarrone on 10/27/16.
 */
public interface MessagesService {
    List<MessagesEntity> findAllMessages();

    MessagesEntity save(MessagesEntity user);

    MessagesEntity findById(Long id);

    List<MessagesEntity> findByAuthor(String username);
}
