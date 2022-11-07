package com.cpp.lccalc.repo;

import com.cpp.lccalc.models.Project;
import com.cpp.lccalc.models.Risk;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RiskRepository extends CrudRepository<Risk,Long> {
}
