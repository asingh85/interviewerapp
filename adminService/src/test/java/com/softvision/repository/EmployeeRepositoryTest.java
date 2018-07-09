package com.softvision.repository;

import com.softvision.common.ServiceConstants;
import com.softvision.model.Employee;
import com.softvision.model.EmployeeType;
import com.softvision.model.InterviewerType;
import com.softvision.model.TechnologyCommunity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@EnableMongoRepositories
@TestPropertySource(locations = "classpath:application-prod.properties")
@SpringBootTest
public class EmployeeRepositoryTest {

    @Inject
    EmployeeRepository empRepository;

    Employee employee;
    Employee insertedEmployee;

    @Before
    public void setUp() {
        employee = new Employee();
        employee.setEmployeeId("10");
        employee.setFirstName("employee1");
        employee.setLastName("employee1");
        employee.setEmailId("employee1@softvision.com");
        employee.setIsDeleted(ServiceConstants.NO);
        employee.setContactNumber("325698741");
        employee.setCreatedDate(LocalDateTime.now());
        employee.setModifiedDate(LocalDateTime.now());
        employee.setEmployeeType(EmployeeType.R);
        employee.setTechnologyCommunity(TechnologyCommunity.JAVA);
        employee.setBandExperience(0);
        employee.setInterviewerType(InterviewerType.I);
        employee.setPassword("Softvision#317");
        insertedEmployee = empRepository.save(employee);
    }

    @Test
    public void testRepositoryInsert() {
        assertNotNull(insertedEmployee);
        assertThat(insertedEmployee.getFirstName(), is(equalTo(employee.getFirstName())));
        assertThat(insertedEmployee.getLastName(), is(equalTo(employee.getLastName())));
        assertThat(insertedEmployee.getContactNumber(), is(equalTo(employee.getContactNumber())));
        assertEquals(insertedEmployee.getTechnologyCommunity(), employee.getTechnologyCommunity());
    }

    @Test
    public void testRepositoryFindAll() {
        List<Employee> employees = empRepository.findAll();
        assertTrue(employees.size() > 2);
    }

    @Test
    public void testRepositoryFindById() {
        Optional<Employee> findEmployee = empRepository.findById("10");
        assertFalse(findEmployee.isPresent());
    }

    @Test
    public void testRepositoryExists() {
        Boolean value = empRepository.existsById("100");
        assertNotNull(value);
        assertFalse(value);
    }


    @Test
    public void testRepositoryDelete() {
        empRepository.deleteById("10");
        Optional<Employee> deletedEmployee = empRepository.findById("10");
        assertFalse(deletedEmployee.isPresent());
    }
}
