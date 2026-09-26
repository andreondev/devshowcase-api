package com.devshowcase.api.controller;

import com.devshowcase.api.dto.FeedbackRequestDTO;
import com.devshowcase.api.dto.FeedbackResponseDTO;
import com.devshowcase.api.dto.ProjectRequestDTO;
import com.devshowcase.api.dto.ProjectResponseDTO;
import com.devshowcase.api.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projetos", description = "Endpoints para gerenciamento de projetos dos desenvolvedores. Permite criar, listar, buscar, deletar, dar upvote e avaliar projetos com feedbacks.")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(
        summary = "Cadastrar um novo projeto",
        description = "Cria um novo projeto associado a um perfil de desenvolvedor existente. É necessário informar ao menos uma tecnologia válida pelo seu ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Projeto criado com sucesso",
            content = @Content(schema = @Schema(implementation = ProjectResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou tecnologias não encontradas", content = @Content),
        @ApiResponse(responseCode = "404", description = "Perfil não encontrado com o ID informado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> create(@Valid @RequestBody ProjectRequestDTO dto) {
        ProjectResponseDTO created = projectService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
        summary = "Listar todos os projetos",
        description = "Retorna uma lista paginada de todos os projetos cadastrados. Aceita filtro opcional por nome de tecnologia (ex: `?technology=Java`). "
                    + "Parâmetros de paginação: `page` (número da página, padrão 0), `size` (itens por página, padrão 10), `sort` (campo,asc|desc)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de projetos retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<Page<ProjectResponseDTO>> findAll(
            @Parameter(description = "Filtra projetos que utilizam a tecnologia informada (busca insensível a maiúsculas/minúsculas).", example = "Java")
            @RequestParam(required = false) String technology,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(projectService.findAll(technology, pageable));
    }

    @Operation(
        summary = "Buscar projeto por ID",
        description = "Retorna os detalhes completos de um projeto específico com base no seu identificador único."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Projeto encontrado",
            content = @Content(schema = @Schema(implementation = ProjectResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado com o ID informado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> findById(
            @Parameter(description = "ID único do projeto", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(projectService.findById(id));
    }

    @Operation(
        summary = "Deletar projeto",
        description = "Remove permanentemente um projeto da base de dados pelo seu ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Projeto deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado com o ID informado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID único do projeto a ser removido", required = true, example = "1")
            @PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Dar upvote em um projeto",
        description = "Incrementa em 1 o contador de upvotes (curtidas/estrelas) de um projeto. Cada chamada representa um novo voto positivo."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Upvote registrado. Retorna o projeto com o contador atualizado.",
            content = @Content(schema = @Schema(implementation = ProjectResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado com o ID informado", content = @Content)
    })
    @PutMapping("/{id}/upvote")
    public ResponseEntity<ProjectResponseDTO> upvote(
            @Parameter(description = "ID único do projeto que receberá o upvote", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(projectService.upvote(id));
    }

    @Operation(
        summary = "Cadastrar feedback para um projeto",
        description = "Adiciona uma avaliação (nota de 1 a 5) e um comentário opcional a um projeto. "
                    + "Após cada novo feedback, a nota média (`averageRating`) do projeto é recalculada automaticamente e atualizada."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Feedback cadastrado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos — a nota deve ser um número inteiro entre 1 e 5", content = @Content),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado com o ID informado", content = @Content)
    })
    @PostMapping("/{id}/feedbacks")
    public ResponseEntity<FeedbackResponseDTO> addFeedback(
            @Parameter(description = "ID único do projeto que receberá o feedback", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody FeedbackRequestDTO dto) {
        FeedbackResponseDTO created = projectService.addFeedback(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}