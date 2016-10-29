package com.ucu.seguridad.dao;

import com.ucu.seguridad.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by nachogarrone on 10/27/16.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);

    UserEntity findByEmail(String email);
}
