package com.cpp.lccalc.repo;

import com.cpp.lccalc.models.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer,Long> {

}
