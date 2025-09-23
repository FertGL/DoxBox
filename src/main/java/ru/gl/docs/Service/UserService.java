package ru.gl.docs.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gl.docs.dto.UserRegistrationDto;
import ru.gl.docs.entity.Role;
import ru.gl.docs.entity.Users;
import ru.gl.docs.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AutoLoginService autoLoginService;

    @Autowired
    private UserDetailsService userDetailsService;

    public Users registerUser(UserRegistrationDto userDto) {
        if (userRepository.existsByPassport(userDto.getPassportNumber())) {
            throw new RuntimeException("User with this passport number already exists");
        }
        Users user = new Users();
        user.setFirstname(userDto.getFirstName());
        user.setLastname(userDto.getLastName());
        user.setPassport(userDto.getPassportNumber());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.USER);

        Users savedUser = userRepository.save(user);

        autoLoginService.authenticateUser(userDto.getPassportNumber(), userDto.getPassword());

        return savedUser;
    }

    public Users findUserByPassport(String passportNumber) {
        return userRepository.findByPassport(passportNumber);
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Page<Users> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<Users> searchUsersByPassport(String passportNumber, Pageable pageable) {
        return userRepository.findByPassportContaining(passportNumber, pageable);
    }
}
