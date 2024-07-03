package com.example.cupteaaccount.domain.login.service;

import com.example.cupteaaccount.config.security.user.CustomUserDetails;
import com.example.db.domain.model.entity.user.UserEntity;
import com.example.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserEntity user = userRepository.findByLoginId(username);

        if (user != null) {
            return new CustomUserDetails(user);
        }
        return null;
    }
}
