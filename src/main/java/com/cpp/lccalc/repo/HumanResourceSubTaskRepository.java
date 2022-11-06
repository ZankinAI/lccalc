package com.cpp.lccalc.repo;

import com.cpp.lccalc.models.HumanResourcesSubTask;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface HumanResourceSubTaskRepository extends CrudRepository<HumanResourcesSubTask,Long> {

    @Modifying
    @Query("delete from HumanResourcesSubTask where humanResourceSubTaskId = :id")
    void deleteById(Long id);
}
