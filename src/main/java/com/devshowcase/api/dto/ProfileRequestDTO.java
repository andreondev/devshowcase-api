package com.devshowcase.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record ProfileRequestDTO(
    @NotBlank(message = "O nome é obrigatório") 
    String name,
    
    @NotBlank(message = "O e-mail é obrigatório") 
    @Email(message = "Forneça um endereço de e-mail válido") 
    String email,
    
    
    @URL(message = "Forneça um URL válido para o GitHub") 
    String githubUrl
) {}