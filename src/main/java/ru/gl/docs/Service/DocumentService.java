package ru.gl.docs.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gl.docs.dto.DocumentDto;
import ru.gl.docs.entity.Document;
import ru.gl.docs.entity.Users;
import ru.gl.docs.repository.DocumentRepository;
import ru.gl.docs.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    // Получить все документы
    public List<Document> getUserDocuments(Long userId) {
        try {
            return documentRepository.findDocumentsByUserId(userId);
        } catch (Exception e) {
            System.err.println("Error getting user documents: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }


    public Document createDocument(Document document) {
        return documentRepository.save(document);
    }

    public Document updateDocument(Long id, Document documentDetails) {
        Optional<Document> documentOpt = documentRepository.findById(id);
        if (documentOpt.isPresent()) {
            Document document = documentOpt.get();
            document.setDocumentName(documentDetails.getDocumentName());
            document.setGoogleDriveLink(documentDetails.getGoogleDriveLink());
            document.setDate(documentDetails.getDate());
            return documentRepository.save(document);
        }
        return null;
    }

    public boolean deleteDocument(Long id) {
        if (documentRepository.existsById(id)) {
            documentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean documentExists(Long id) {
        return documentRepository.existsById(id);
    }

    public List<Users> getUsersWithDocumentAccess(Long documentId) {
        Optional<Document> documentOpt = documentRepository.findById(documentId);
        return documentOpt.map(document ->
                document.getUsers().stream().collect(Collectors.toList())
        ).orElse(Collections.emptyList());
    }

    public List<DocumentDto> getAllDocuments() {
        try {
            List<Document> documents = documentRepository.findAllByOrderByDocumentNameAsc();
            return documents.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("❌ Ошибка при получении документов: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Конвертер Entity -> DTO
    private DocumentDto convertToDto(Document document) {
        DocumentDto dto = new DocumentDto();
        dto.setId(document.getId());
        dto.setDocumentName(document.getDocumentName());
        dto.setGoogleDriveLink(document.getGoogleDriveLink());
        dto.setDate(document.getDate());
        return dto;
    }

    @Transactional
    public void updateUserDocuments(Long userId, List<Long> documentIds) {
        try {
            System.out.println("=== ОБНОВЛЕНИЕ ДОКУМЕНТОВ ДЛЯ ПОЛЬЗОВАТЕЛЯ " + userId + " ===");

            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + userId));

            System.out.println("Найден пользователь: " + user.getFirstname() + " " + user.getLastname());

            // Очищаем текущие документы
            user.getDocuments().clear();
            System.out.println("Текущие документы очищены");

            // Добавляем новые документы, если они переданы
            if (documentIds != null && !documentIds.isEmpty()) {
                System.out.println("Добавляем " + documentIds.size() + " документов");

                List<Document> documentsToAdd = documentRepository.findAllById(documentIds);
                System.out.println("Найдено документов в БД: " + documentsToAdd.size());

                user.getDocuments().addAll(documentsToAdd);
            }

            userRepository.save(user);
            System.out.println("✅ Изменения сохранены успешно");

        } catch (Exception e) {
            System.err.println("❌ Ошибка в updateUserDocuments: " + e.getMessage());
            throw e;
        }
    }

    public List<Long> getUserDocumentIds(Long userId) {
        try {
            return documentRepository.findDocumentIdsByUserId(userId);
        } catch (Exception e) {
            System.err.println("❌ Ошибка при получении документов пользователя: " + e.getMessage());
            return Collections.emptyList();
        }
    }


}