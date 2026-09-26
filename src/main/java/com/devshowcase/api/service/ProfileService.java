package com.devshowcase.api.service;

import com.devshowcase.api.dto.ProfileRequestDTO;
import com.devshowcase.api.dto.ProfileResponseDTO;
import com.devshowcase.api.model.Profile;
import com.devshowcase.api.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Transactional
    public ProfileResponseDTO create(ProfileRequestDTO dto) {
        Profile profile = new Profile();
        profile.setName(dto.name());
        profile.setEmail(dto.email());
        profile.setGithubUrl(dto.githubUrl());

        Profile saved = profileRepository.save(profile);
        return ProfileResponseDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List findAll() {
        return profileRepository.findAll().stream()
                .map(ProfileResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProfileResponseDTO findById(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado com o ID: " + id));
        return ProfileResponseDTO.fromEntity(profile);
    }

    @Transactional
    public ProfileResponseDTO update(Long id, ProfileRequestDTO dto) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado com o ID: " + id));

        profile.setName(dto.name());
        profile.setEmail(dto.email());
        profile.setGithubUrl(dto.githubUrl());

        Profile updated = profileRepository.save(profile);
        return ProfileResponseDTO.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new RuntimeException("Perfil não encontrado com o ID: " + id);
        }
        profileRepository.deleteById(id);
    }
}