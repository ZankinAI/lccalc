package com.cpp.lccalc.repo;

import com.cpp.lccalc.models.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task,Long> {
}
