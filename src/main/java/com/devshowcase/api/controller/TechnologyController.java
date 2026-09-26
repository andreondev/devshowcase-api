package com.devshowcase.api.controller;

import com.devshowcase.api.dto.TechnologyRequestDTO;
import com.devshowcase.api.dto.TechnologyResponseDTO;
import com.devshowcase.api.service.TechnologyService;
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
@RequestMapping("/api/technologies")
@Tag(name = "Tecnologias", description = "Endpoints para gerenciamento do catálogo de tecnologias disponíveis na plataforma (ex: Java, React, Docker). As tecnologias são vinculadas aos projetos.")
public class TechnologyController {

    private final TechnologyService technologyService;

    public TechnologyController(TechnologyService technologyService) {
        this.technologyService = technologyService;
    }

    @Operation(
        summary = "Cadastrar uma nova tecnologia",
        description = "Adiciona uma nova tecnologia ao catálogo da plataforma. O nome da tecnologia deve ser único. "
                    + "Após criada, o ID gerado pode ser usado para associar a tecnologia a um projeto."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Tecnologia criada com sucesso",
            content = @Content(schema = @Schema(implementation = TechnologyResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou tecnologia com esse nome já existe", content = @Content)
    })
    @PostMapping
    public ResponseEntity<TechnologyResponseDTO> create(@Valid @RequestBody TechnologyRequestDTO dto) {
        TechnologyResponseDTO created = technologyService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
        summary = "Listar todas as tecnologias",
        description = "Retorna a lista completa de todas as tecnologias cadastradas no catálogo da plataforma."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de tecnologias retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<TechnologyResponseDTO>> findAll() {
        return ResponseEntity.ok(technologyService.findAll());
    }

    @Operation(
        summary = "Buscar tecnologia por ID",
        description = "Retorna os dados de uma tecnologia específica do catálogo com base no seu identificador único."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tecnologia encontrada",
            content = @Content(schema = @Schema(implementation = TechnologyResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Tecnologia não encontrada com o ID informado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TechnologyResponseDTO> findById(
            @Parameter(description = "ID único da tecnologia", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(technologyService.findById(id));
    }

    @Operation(
        summary = "Deletar tecnologia",
        description = "Remove uma tecnologia do catálogo da plataforma. Atenção: tecnologias vinculadas a projetos não devem ser removidas diretamente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Tecnologia deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tecnologia não encontrada com o ID informado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID único da tecnologia a ser removida", required = true, example = "1")
            @PathVariable Long id) {
        technologyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}