package org.sops.services;

import lombok.RequiredArgsConstructor;
import org.sops.database.entities.UserEntity;
import org.sops.database.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserEntity loadUserByUsername(String userName) {
        return userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userName));
    }

    public UserEntity addUser(UserEntity user) {
        return userRepository.save(user);
    }

}
