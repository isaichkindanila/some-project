package ru.itis.some.project.services.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itis.some.project.models.User;
import ru.itis.some.project.repositories.UserRepository;
import ru.itis.some.project.services.AuthService;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthAndUserDetailsServiceImpl implements AuthService, UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );

        return new UserDetailsImpl(user);
    }

    @Override
    public Optional<User> getCurrentUserOptional() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return Optional.empty();
        }

        var details = (UserDetailsImpl) authentication.getPrincipal();

        return details == null ? Optional.empty() : Optional.of(details.getUser());
    }

    @Override
    public User getCurrentUser() {
        return getCurrentUserOptional().orElseThrow(
                () -> new IllegalStateException("user is not authenticated")
        );
    }

    @Getter
    @AllArgsConstructor
    private static class UserDetailsImpl implements UserDetails {
        private final User user;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.emptyList();
        }

        @Override
        public String getPassword() {
            return user.getPassHash();
        }

        @Override
        public String getUsername() {
            return user.getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return user.isActivated();
        }
    }
}
