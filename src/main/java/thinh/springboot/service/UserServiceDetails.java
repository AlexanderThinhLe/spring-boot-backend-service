package thinh.springboot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import thinh.springboot.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceDetails {
    private final UserRepository userRepository;

    public UserDetailsService UserServiceDetails() {
        return userRepository::findByUsername;
    }
}
