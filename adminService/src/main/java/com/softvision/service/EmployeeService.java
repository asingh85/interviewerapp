package com.softvision.service;

import com.softvision.model.Employee;
import com.softvision.model.EmployeeType;
import com.softvision.model.TechnologyCommunity;

import java.util.List;
import java.util.Optional;

public interface EmployeeService<T extends Employee> {


    /**
     * Find All Employees
     *
     * @return Employee
     */
    Optional<List<Employee>> getAllEmployees();

    Optional<List<Employee>> getAllRecruiters();

    Optional<List<Employee>> getAllInterviewers();

    /**
     * Find Employee By ID
     *
     * @param id The Id
     * @return Employee
     */
    Optional<T> getEmployeeById(String id);

    /**
     * Searching operation
     *
     * @param str the str
     * @return search result
     */
    List<T> search(String str);

    /**
     * Add an Employee
     *
     * @param employee The Employee
     * @return Employee
     */
    Optional<T> addEmployee(T employee);

    /**
     * Update Employee by ID
     *
     * @param employee The Employee
     * @param id       The ID
     * @return Employee
     */
    Optional<T> updateEmployee(T employee, String id);

    /**
     * Delete Employee by ID (softDelete)
     *
     * @param id The ID
     */

    void deleteEmployee(String id);

    /**
     * Delete All Employeessoftdelete()
     */
    void deleteAllEmployees();

    /**
     * Find Employee by EexpInmonths and technicalCommunity
     *
     * @param expInmonths        The expInmonths
     * @param technicalCommunity The technicalCommunity
     * @return
     */
    Optional<List<Employee>> getAllEmployeesByBandExp(int expInmonths, String technicalCommunity);

    /**
     * @return TechnologyCommunity
     */
    Optional<List<TechnologyCommunity>> getTechStack();

    /**
     * @return EmployeeType
     */
    Optional<List<EmployeeType>> getEmployeeType();
}
