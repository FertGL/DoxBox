package ru.gl.docs.dto;

import ru.gl.docs.entity.Document;
import ru.gl.docs.entity.Users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserProfileDto {
    private Users user;
    private List<Document> documents = new ArrayList<>(); // Инициализация по умолчанию

    public UserProfileDto() {
        this.documents = new ArrayList<>();
    }

    public UserProfileDto(Users user, List<Document> documents) {
        this.user = user;
        this.documents = documents != null ? documents : new ArrayList<>();
    }


    public Users getUser() {
        return user;
    }
    public void setUser(Users user) {
        this.user = user;
    }

    public List<Document> getDocuments() {
        return documents != null ? documents : Collections.emptyList();
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents != null ? documents : new ArrayList<>();
    }

    // Вспомогательный метод для проверки наличия документов
    public boolean hasDocuments() {
        return documents != null && !documents.isEmpty();
    }
}