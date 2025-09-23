package ru.gl.docs.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,name = "date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate Date;

    @Column(name = "document_link", nullable = false)
    private String documentLink;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users user;
}
