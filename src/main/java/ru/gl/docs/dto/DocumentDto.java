package ru.gl.docs.dto;

import java.time.LocalDate;

public class DocumentDto {
    private Long id;
    private String documentName;
    private String googleDriveLink;
    private String description;
    private LocalDate date;

    // Конструкторы
    public DocumentDto() {}

    public DocumentDto(Long id, String documentName, String googleDriveLink, LocalDate date) {
        this.id = id;
        this.documentName = documentName;
        this.googleDriveLink = googleDriveLink;
        this.date = date;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDocumentName() { return documentName; }
    public void setDocumentName(String documentName) { this.documentName = documentName; }

    public String getGoogleDriveLink() { return googleDriveLink; }
    public void setGoogleDriveLink(String googleDriveLink) { this.googleDriveLink = googleDriveLink; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
