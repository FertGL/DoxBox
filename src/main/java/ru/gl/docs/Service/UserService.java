package ru.gl.docs.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gl.docs.dto.UserProfileDto;
import ru.gl.docs.dto.UserRegistrationDto;
import ru.gl.docs.entity.Document;
import ru.gl.docs.entity.Role;
import ru.gl.docs.entity.Users;
import ru.gl.docs.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AutoLoginService autoLoginService;

    public UserProfileDto getUserProfile(Long userId) {
        try {
            Optional<Users> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                Users user = userOpt.get();
                List<Document> documents = documentService.getUserDocuments(userId);
                return new UserProfileDto(user, documents);
            }
        } catch (Exception e) {
            System.err.println("Error getting user profile: " + e.getMessage());
        }

        // Возвращаем пустой DTO в случае ошибки
        return new UserProfileDto();
    }

    // Остальные методы остаются без изменений...
    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<Users> getUserByPassport(String passport) {
        return userRepository.findByPassport(passport);
    }

    public Users getByPassport(String passport) {
        return userRepository.getByPassport(passport);
    }

    public Page<Users> searchUsers(String search, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));

        if (search != null && !search.trim().isEmpty()) {
            return userRepository.findByPassportContaining(search.trim(), pageable);
        } else {
            return userRepository.findAll(pageable);
        }
    }

    public Users saveUser(Users user) {
        return userRepository.save(user);
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean userExistsByPassport(String passport) {
        return userRepository.existsByPassport(passport);
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

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
}
