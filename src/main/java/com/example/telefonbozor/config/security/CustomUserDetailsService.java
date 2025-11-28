package com.example.telefonbozor.config.security;

import com.example.telefonbozor.config.security.CustomUserDetails;
import com.example.telefonbozor.model.entity.AuthUser;
import com.example.telefonbozor.repository.AuthUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthUserRepository repository;

    public CustomUserDetailsService(AuthUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Long telegramId = Long.parseLong(username); // String → Long
            AuthUser user = repository.findByTelegramId(telegramId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + telegramId));
            return new CustomUserDetails(user);
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("Invalid Telegram ID format: " + username);
        }
    }
}
