package com.cpp.lccalc.repo;

import com.cpp.lccalc.models.MaterialResourcesSubTask;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MaterialResourcesSubTaskRepository extends CrudRepository<MaterialResourcesSubTask, Long> {

    @Modifying
    @Query("delete from MaterialResourcesSubTask where materialResourceSubTaskId = :id")
    void deleteById(Long id);

}