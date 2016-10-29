package com.ucu.seguridad.services.impl;

import com.ucu.seguridad.dao.UserRepository;
import com.ucu.seguridad.models.UserEntity;
import com.ucu.seguridad.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by nachogarrone on 10/27/16.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public UserEntity findById(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public UserEntity findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
