package com.devshowcase.api.repository;

import com.devshowcase.api.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p JOIN p.technologies t WHERE LOWER(t.name) = LOWER(:techName)")
    Page<Project> findByTechnologyName(@Param("techName") String techName, Pageable pageable);
}