package ru.gl.docs.dto;


import java.util.List;

public class UserDocumentUpdateDto {
    private Long userId;
    private List<Long> documentIds;

    public UserDocumentUpdateDto() {}

    public UserDocumentUpdateDto(Long userId, List<Long> documentIds) {
        this.userId = userId;
        this.documentIds = documentIds;
    }


    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<Long> getDocumentIds() { return documentIds; }
    public void setDocumentIds(List<Long> documentIds) { this.documentIds = documentIds; }
}
