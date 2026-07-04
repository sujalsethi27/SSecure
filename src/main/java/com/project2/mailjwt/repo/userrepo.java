package com.project2.mailjwt.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.project2.mailjwt.entities.userEntity;


public interface userrepo extends JpaRepository<userEntity, Long> {
    java.util.Optional<userEntity> findByEmail(String email);
    Boolean existsByEmail(String email); // this method checks if a user with the given email already exists in the database. It returns true if a user with the email exists, and false otherwise.


}
