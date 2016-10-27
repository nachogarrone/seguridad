package com.ucu.seguridad.services;

import com.ucu.seguridad.models.UserEntity;

import java.util.List;

/**
 * Created by nachogarrone on 10/27/16.
 */
public interface UserService {
    List<UserEntity> findAllUsers();

    UserEntity save(UserEntity user);

    UserEntity findById(Long id);
}
