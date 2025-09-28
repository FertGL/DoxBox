package ru.gl.docs.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "document_link", nullable = false)
    private String googleDriveLink;

    @Column(nullable = false,name = "date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate Date;

    @ManyToMany(mappedBy = "documents")
    private Set<Users> users = new HashSet<>();

    public Document(Long id, String documentName, String googleDriveLink, LocalDate date, Set<Users> users) {
        this.id = id;
        this.documentName = documentName;
        this.googleDriveLink = googleDriveLink;
        Date = date;
        this.users = users;
    }

    public Document() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getGoogleDriveLink() {
        return googleDriveLink;
    }

    public void setGoogleDriveLink(String googleDriveLink) {
        this.googleDriveLink = googleDriveLink;
    }

    public LocalDate getDate() {
        return Date;
    }

    public void setDate(LocalDate date) {
        Date = date;
    }

    public Set<Users> getUsers() {
        return users;
    }

    public void setUsers(Set<Users> users) {
        this.users = users;
    }


    public String getValidGoogleDriveLink() {
        if (googleDriveLink == null || googleDriveLink.trim().isEmpty()) {
            return null;
        }

        String link = googleDriveLink.trim();

        // Если ссылка уже абсолютная, возвращаем как есть
        if (link.startsWith("http://") || link.startsWith("https://")) {
            return link;
        }

        // Если ссылка относительная, добавляем базовый URL
        return "https://" + link;
    }

    // Метод для получения прямой ссылки на скачивание
    public String getDirectDownloadLink() {
        String link = getValidGoogleDriveLink();
        if (link == null) return null;

        // Преобразуем ссылку просмотра в ссылку скачивания
        if (link.contains("/file/d/")) {
            String fileId = extractFileId(link);
            if (fileId != null) {
                return "https://drive.google.com/uc?export=download&id=" + fileId;
            }
        }

        return link;
    }

    // Извлекаем ID файла из Google Drive ссылки
    private String extractFileId(String driveLink) {
        try {
            if (driveLink.contains("/file/d/")) {
                String[] parts = driveLink.split("/file/d/");
                if (parts.length > 1) {
                    String secondPart = parts[1];
                    String[] secondParts = secondPart.split("/");
                    return secondParts[0];
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
