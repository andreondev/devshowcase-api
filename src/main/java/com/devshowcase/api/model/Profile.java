package com.devshowcase.api.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    private String githubUrl;

    // Relacionamento 1 : N (Um Perfil para Vários Projetos)
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects = new ArrayList<>();

    // 1. Construtor padrão (obrigatório pelo JPA)
    public Profile() {
    }

    // 2. Construtor personalizado (facilita criar novos perfis no código)
    public Profile(String name, String email, String githubUrl) {
        this.name = name;
        this.email = email;
        this.githubUrl = githubUrl;
    }

    // 3. Getters e Setters (Encapsulamento do Java)
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public List<Project> getProjects() {
        return projects;
    }
}