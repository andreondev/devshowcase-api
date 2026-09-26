package com.devshowcase.api.controller;

import com.devshowcase.api.dto.ProfileRequestDTO;
import com.devshowcase.api.dto.ProfileResponseDTO;
import com.devshowcase.api.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@Tag(name = "Perfis", description = "Endpoints para gerenciamento de perfis de desenvolvedores. Um perfil é a entidade principal que agrupa os projetos de um desenvolvedor.")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Operation(
        summary = "Cadastrar um novo perfil",
        description = "Cria um novo perfil de desenvolvedor. O e-mail e o nome devem ser únicos na plataforma."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Perfil criado com sucesso",
            content = @Content(schema = @Schema(implementation = ProfileResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou e-mail/nome já cadastrado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ProfileResponseDTO> create(@Valid @RequestBody ProfileRequestDTO dto) {
        ProfileResponseDTO created = profileService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
        summary = "Listar todos os perfis",
        description = "Retorna a lista completa de todos os perfis de desenvolvedores cadastrados na plataforma."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de perfis retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<ProfileResponseDTO>> findAll() {
        return ResponseEntity.ok(profileService.findAll());
    }

    @Operation(
        summary = "Buscar perfil por ID",
        description = "Retorna os dados de um perfil de desenvolvedor específico com base no seu identificador único."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Perfil encontrado",
            content = @Content(schema = @Schema(implementation = ProfileResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Perfil não encontrado com o ID informado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponseDTO> findById(
            @Parameter(description = "ID único do perfil", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(profileService.findById(id));
    }

    @Operation(
        summary = "Atualizar dados de um perfil",
        description = "Atualiza as informações de um perfil de desenvolvedor existente. Todos os campos enviados sobrescrevem os anteriores."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = ProfileResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Perfil não encontrado com o ID informado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponseDTO> update(
            @Parameter(description = "ID único do perfil a ser atualizado", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ProfileRequestDTO dto) {
        return ResponseEntity.ok(profileService.update(id, dto));
    }

    @Operation(
        summary = "Deletar perfil",
        description = "Remove permanentemente um perfil de desenvolvedor da base de dados. Atenção: a remoção pode afetar os projetos vinculados ao perfil."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Perfil deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Perfil não encontrado com o ID informado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID único do perfil a ser removido", required = true, example = "1")
            @PathVariable Long id) {
        profileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}