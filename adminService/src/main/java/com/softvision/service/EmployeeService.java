package com.softvision.service;

import com.softvision.model.Employee;
import com.softvision.model.EmployeeType;
import com.softvision.model.TechnologyCommunity;

import java.util.List;
import java.util.Optional;

public interface EmployeeService<T extends Employee> {

    Optional<List<Employee>> getAllRecruiters();

    Optional<List<Employee>> getAllInterviewers();

    Optional<T> getEmployeeById(String id);

    List<T> searchInterviewer(String str);

    List<T> searchRecruiter(String str);

    Optional<T> addEmployee(T employee);

    Optional<T> updateEmployee(T employee, String id);

    Optional<List<Employee>> getAllEmployeesByBandExp(int expInmonths, String technicalCommunity);

    // user in UI
    Optional<List<TechnologyCommunity>> getTechStack();

    Optional<List<EmployeeType>> getEmployeeType();

    Optional<List<Employee>> getInterviewerByType(String technicalCommunity, String interviewerType);

    Optional<T> deleteEmployee(String id);

    void deleteAllEmployees();
}
