package com.ucu.seguridad.dao;

import com.ucu.seguridad.models.MessagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by nachogarrone on 10/27/16.
 */
public interface MessagesRepository extends JpaRepository<MessagesEntity, Long> {
    List<MessagesEntity> findByAuthor(String author);
}
