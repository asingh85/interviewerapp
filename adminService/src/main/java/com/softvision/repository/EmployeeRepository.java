package com.softvision.repository;

import com.softvision.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * The interface  EmployeeRepository
 */
public interface EmployeeRepository extends MongoRepository<Employee, String> {


}
