package com.project.authapi.authserviceapi.mapper;


import com.project.authapi.authserviceapi.dto.RegisterRequest;
import com.project.authapi.authserviceapi.entity.Role;
import com.project.authapi.authserviceapi.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toEntity(RegisterRequest dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setRole(Role.USER);
        user.setEnabled(true);
        return user;
    }
}
