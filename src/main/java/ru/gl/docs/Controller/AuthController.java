package ru.gl.docs.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.gl.docs.Service.UserService;
import ru.gl.docs.dto.UserLoginDto;
import ru.gl.docs.dto.UserRegistrationDto;
import ru.gl.docs.entity.Users;

@Controller
public class AuthController {


    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/hello")
    public String homePage(Model model) {
        model.addAttribute("userDto", new UserRegistrationDto());
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("userLoginDto", new UserLoginDto());
        return "login";
    }

    @GetMapping("/profile")
    public String showProfile(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String passportNumber = authentication.getName();
        Users user = userService.getByPassport(passportNumber);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("isAdmin", hasAdminRole(authentication));

        return "profile";
    }

    private boolean hasAdminRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userDto") UserRegistrationDto userDto,
                               Model model) {
        try {
            Users user = userService.registerUser(userDto);
            return "redirect:/profile";

        } catch (Exception e) {
            model.addAttribute("error", "Ошибка регистрации: " + e.getMessage());
            model.addAttribute("userDto", userDto);
            return "register";
        }
    }
}
