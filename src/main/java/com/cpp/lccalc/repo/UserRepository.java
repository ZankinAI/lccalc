package com.cpp.lccalc.repo;

import com.cpp.lccalc.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByUsername(String username);
}
