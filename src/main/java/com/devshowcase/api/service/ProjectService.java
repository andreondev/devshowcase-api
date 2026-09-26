package com.devshowcase.api.service;

import com.devshowcase.api.dto.ProjectRequestDTO;
import com.devshowcase.api.dto.ProjectResponseDTO;
import com.devshowcase.api.dto.FeedbackRequestDTO;
import com.devshowcase.api.dto.FeedbackResponseDTO;
import com.devshowcase.api.exception.ResourceNotFoundException;
import com.devshowcase.api.model.Feedback;
import com.devshowcase.api.model.Profile;
import com.devshowcase.api.model.Project;
import com.devshowcase.api.model.Technology;
import com.devshowcase.api.repository.FeedbackRepository;
import com.devshowcase.api.repository.ProfileRepository;
import com.devshowcase.api.repository.ProjectRepository;
import com.devshowcase.api.repository.TechnologyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProfileRepository profileRepository;
    private final TechnologyRepository technologyRepository;
    private final FeedbackRepository feedbackRepository;

    public ProjectService(ProjectRepository projectRepository,
                          ProfileRepository profileRepository,
                          TechnologyRepository technologyRepository,
                          FeedbackRepository feedbackRepository) {
        this.projectRepository = projectRepository;
        this.profileRepository = profileRepository;
        this.technologyRepository = technologyRepository;
        this.feedbackRepository = feedbackRepository;
    }

    @Transactional
    public ProjectResponseDTO create(ProjectRequestDTO dto) {
        Profile profile = profileRepository.findById(dto.profileId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado com o ID: " + dto.profileId()));

        List<Technology> techList = technologyRepository.findAllById(dto.technologyIds());
        if (techList.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma tecnologia válida foi encontrada para os IDs fornecidos.");
        }

        Project project = new Project();
        project.setTitle(dto.title());      
        project.setDescription(dto.description());
        project.setRepositoryUrl(dto.repositoryUrl());
        project.setProfile(profile);
        project.setTechnologies(new HashSet<>(techList));

        Project saved = projectRepository.save(project);
        return ProjectResponseDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> findAll(String techName, Pageable pageable) {
        Page<Project> projects;
        if (techName != null && !techName.isEmpty()) {
            projects = projectRepository.findByTechnologyName(techName, pageable);
        } else {
            projects = projectRepository.findAll(pageable);
        }
        return projects.map(ProjectResponseDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public ProjectResponseDTO findById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado com o ID: " + id));
        return ProjectResponseDTO.fromEntity(project);
    }

    @Transactional
    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Projeto não encontrado com o ID: " + id);
        }
        projectRepository.deleteById(id);
    }

    @Transactional
    public ProjectResponseDTO upvote(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado com o ID: " + id));
        
        project.setUpvotes(project.getUpvotes() + 1);
        Project saved = projectRepository.save(project);
        return ProjectResponseDTO.fromEntity(saved);
    }

    @Transactional
    public FeedbackResponseDTO addFeedback(Long id, FeedbackRequestDTO dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado com o ID: " + id));
        
        Feedback feedback = new Feedback(dto.rating(), dto.comment(), project);
        Feedback saved = feedbackRepository.save(feedback);
        
        // Recalculate average rating
        List<Feedback> allFeedbacks = feedbackRepository.findByProjectId(id);
        OptionalDouble average = allFeedbacks.stream()
                .mapToInt(Feedback::getRating)
                .average();
                
        if (average.isPresent()) {
            project.setAverageRating(Math.round(average.getAsDouble() * 10.0) / 10.0); // round to 1 decimal place
            projectRepository.save(project);
        }
        
        return FeedbackResponseDTO.fromEntity(saved);
    }
}