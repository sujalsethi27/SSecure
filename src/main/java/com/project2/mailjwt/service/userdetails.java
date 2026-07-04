package com.project2.mailjwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project2.mailjwt.entities.userEntity;
import com.project2.mailjwt.repo.userrepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class userdetails implements UserDetailsService {
// this user details loads the user from the database using the email as the username and returns a UserDetails object that Spring Security can use for authentication and authorization. It implements the loadUserByUsername method, which is called by Spring Security during the authentication process.
    private final userrepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      userEntity user = userRepository.findByEmail(email)
    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
      return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new java.util.ArrayList<>());
    }

}
