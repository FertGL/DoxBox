package ru.gl.docs.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "Users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname", nullable = false)
    private String firstname;
    @Column(name = "lastname", nullable = false)
    private String lastname;
    @Column(name = "passportNumber", nullable = false, unique = true, length = 10)
    private String passport;
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_documents",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    private Set<Document> documents = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled = true;


    public Users() {
    }

    public Users(Long id, String firstname, String lastname, String passport, String password, Set<Document> documents, Role role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.passport = passport;
        this.password = password;
        this.documents = documents;
        this.role = role;

    }

    public Users(String firstname, String lastname, String passport, String password, Set<Document> documents, Role role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.passport = passport;
        this.password = password;
        this.documents = documents;
        this.role = role;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


}
