package com.cognizant.hams.service;

import com.cognizant.hams.entity.Role;
import com.cognizant.hams.entity.User;
import com.cognizant.hams.exception.InvalidCredentialsException;
import com.cognizant.hams.exception.UserAlreadyExistsException;
import com.cognizant.hams.repository.RoleRepository;
import com.cognizant.hams.repository.UserRepository;
import com.cognizant.hams.security.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String createAuthenticationToken(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new InvalidCredentialsException("USER_DISABLED");
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("INVALID_CREDENTIALS");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtTokenUtil.generateToken(userDetails);
    }

    // This method is for self-registration of a new user with a default role.
    public User registerNewUser(User newUser) {
        Optional<User> existingUser = userRepository.findByUsername(newUser.getUsername());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with this username already exists.");
        }

        // Find the default "PATIENT" role. This role MUST exist in your database.
        Role role = roleRepository.findByName("PATIENT")
                .orElseThrow(() -> new RuntimeException("Default role 'PATIENT' not found. Please seed the database."));

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRole(role);

        return userRepository.save(newUser);
    }

    // This method is for creating privileged users (e.g., Doctors, Admins) by an Admin.
    public User createPrivilegedUser(User newUser, String roleName) {
        Optional<User> existingUser = userRepository.findByUsername(newUser.getUsername());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with this username already exists.");
        }

        Role role = roleRepository.findByName(roleName.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Role '" + roleName + "' not found."));

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRole(role);

        return userRepository.save(newUser);
    }
}