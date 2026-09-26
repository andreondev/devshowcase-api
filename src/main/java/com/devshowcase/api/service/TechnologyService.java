package com.devshowcase.api.service;

import com.devshowcase.api.dto.TechnologyRequestDTO;
import com.devshowcase.api.dto.TechnologyResponseDTO;
import com.devshowcase.api.model.Technology;
import com.devshowcase.api.repository.TechnologyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TechnologyService {

    private final TechnologyRepository technologyRepository;

    public TechnologyService(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    @Transactional
    public TechnologyResponseDTO create(TechnologyRequestDTO dto) {
        Technology tech = new Technology(dto.name());
        Technology saved = technologyRepository.save(tech);
        return TechnologyResponseDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List findAll() {
        return technologyRepository.findAll().stream()
                .map(TechnologyResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TechnologyResponseDTO findById(Long id) {
        Technology tech = technologyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tecnologia não encontrada com o ID: " + id));
        return TechnologyResponseDTO.fromEntity(tech);
    }

    @Transactional
    public void delete(Long id) {
        if (!technologyRepository.existsById(id)) {
            throw new RuntimeException("Tecnologia não encontrada com o ID: " + id);
        }
        technologyRepository.deleteById(id);
    }
}