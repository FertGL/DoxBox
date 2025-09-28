package ru.gl.docs.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gl.docs.Service.DocumentService;
import ru.gl.docs.Service.UserService;
import ru.gl.docs.dto.DocumentDto;
import ru.gl.docs.entity.Document;
import ru.gl.docs.entity.Users;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private DocumentService documentService;


    @GetMapping("/dashboard")
    public String adminDashboard(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstname") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        Page<Users> userPage = userService.searchUsers(search, page, size, sortBy, sortDir);

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("search", search);

        // Добавляем пустые списки для модального окна (чтобы не было ошибок Thymeleaf)
        model.addAttribute("allDocuments", Collections.emptyList());
        model.addAttribute("userDocuments", Collections.emptyList());

        return "admin/dashboard";
    }

    @GetMapping("/users/{userId}/documents")
    @ResponseBody
    public Map<String, Object> getUserDocuments(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();

        List<Long> userDocumentIds = documentService.getUserDocumentIds(userId);
        List<DocumentDto> allDocuments = documentService.getAllDocuments();

        response.put("userDocuments", userDocumentIds);
        response.put("allDocuments", allDocuments);

        return response;
    }

    @PostMapping("/users/{userId}/documents")
    public String updateUserDocuments(
            @PathVariable Long userId,
            @RequestParam(value = "documentIds", required = false) List<Long> documentIds) {

        documentService.updateUserDocuments(userId, documentIds);
        return "redirect:/admin/dashboard?success=true";
    }

    // Дополнительные методы для управления документами
    @GetMapping("/documents")
    public String manageDocuments(Model model) {
        List<DocumentDto> documents = documentService.getAllDocuments();
        model.addAttribute("documents", documents);
        return "admin-documents";
    }

    @PostMapping("/documents")
    public String createDocument(@ModelAttribute Document document) {
        documentService.createDocument(document);
        return "redirect:/admin/documents?success=true";
    }

    @GetMapping("/users/{userId}/documents/data")
    @ResponseBody
    public Map<String, Object> getUserDocumentsData(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("=== ЗАПРОС ДАННЫХ ДЛЯ ПОЛЬЗОВАТЕЛЯ " + userId + " ===");

            List<Long> userDocumentIds = documentService.getUserDocumentIds(userId);
            List<DocumentDto> allDocuments = documentService.getAllDocuments(); // Теперь возвращает DTO

            System.out.println("Всего документов в системе: " + allDocuments.size());
            System.out.println("Документов у пользователя: " + userDocumentIds.size());

            response.put("userDocuments", userDocumentIds);
            response.put("allDocuments", allDocuments);
            response.put("success", true);
            response.put("totalDocuments", allDocuments.size());
            response.put("userDocumentsCount", userDocumentIds.size());

        } catch (Exception e) {
            System.err.println("❌ Ошибка в getUserDocumentsData: " + e.getMessage());
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }


}