package ru.gl.docs.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.gl.docs.Service.UserService;
import ru.gl.docs.entity.Users;
//
//@Controller
//@RequestMapping("/admin")
//public class AdminController {
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("/dashboard")
//    public String adminDashboard(Model model, Authentication authentication) {
//        if (!hasAdminRole(authentication)) {
//            return "redirect:/profile";
//        }
//
//        model.addAttribute("users", userService.getAllUsers());
//        return "admin/dashboard";
//    }
//
//    @PostMapping("/search")
//    public String searchUser(@RequestParam String passportNumber, Model model, Authentication authentication) {
//        if (!hasAdminRole(authentication)) {
//            return "redirect:/profile";
//        }
//
//        Users user = userService.findUserByPassport(passportNumber);
//        model.addAttribute("searchResult", user);
//        model.addAttribute("searchPassport", passportNumber);
//        model.addAttribute("users", userService.getAllUsers());
//
//        return "admin/dashboard";
//    }
//
//    private boolean hasAdminRole(Authentication authentication) {
//        return authentication.getAuthorities().stream()
//                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
//    }
//}

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String adminDashboard(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastname") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model,
            Authentication authentication) {

        if (!hasAdminRole(authentication)) {
            return "redirect:/profile";
        }

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));

        Page<Users> userPage;
        if (search.isEmpty()) {
            userPage = userService.getAllUsers(pageable);
        } else {
            userPage = userService.searchUsersByPassport(search, pageable);
        }

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("size", size);

        return "admin/dashboard";
    }

    @PostMapping("/search")
    public String searchUser(
            @RequestParam String passportNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model,
            Authentication authentication) {

        if (!hasAdminRole(authentication)) {
            return "redirect:/profile";
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Users> userPage = userService.searchUsersByPassport(passportNumber, pageable);

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("search", passportNumber);
        model.addAttribute("size", size);

        return "admin/dashboard";
    }

    private boolean hasAdminRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }
}