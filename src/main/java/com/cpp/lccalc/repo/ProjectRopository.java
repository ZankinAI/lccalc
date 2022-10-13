package com.cpp.lccalc.repo;

import com.cpp.lccalc.models.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRopository extends CrudRepository<Project, Long> {

   List<Project> findByStatus(int status);
}
