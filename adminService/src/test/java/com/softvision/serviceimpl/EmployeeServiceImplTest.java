package com.softvision.serviceimpl;


import com.softvision.common.ServiceConstants;
import com.softvision.model.Employee;
import com.softvision.model.EmployeeType;
import com.softvision.model.InterviewerType;
import com.softvision.model.TechnologyCommunity;
import com.softvision.repository.EmployeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class EmployeeServiceImplTest {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImplTest.class);

    @InjectMocks
    private EmployeeServiceImpl employeeServiceImpl;

    @Mock
    EmployeeRepository empRepository;

    @Mock
    MongoTemplate mongoTemplate;


    private Employee employee;

    private List<Employee> employees;

    private Optional<Employee> optionalEmployees;

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

        Employee employee2 = new Employee();
        employee2.setEmployeeId("20");
        employee2.setFirstName("employee2");
        employee2.setLastName("employee2");
        employee2.setEmailId("employee2@softvision.com");
        employee2.setIsDeleted(ServiceConstants.NO);
        employee2.setContactNumber("325698742");
        employee2.setCreatedDate(LocalDateTime.now());
        employee2.setModifiedDate(LocalDateTime.now());
        employee2.setEmployeeType(EmployeeType.R);
        employee2.setTechnologyCommunity(TechnologyCommunity.DOTNET);
        employee2.setBandExperience(0);
        employee2.setInterviewerType(InterviewerType.I);
        employee2.setPassword("Softvision#318");

        employees = new ArrayList<>();
        employees.add(employee);
        employees.add(employee2);
        optionalEmployees = Optional.of(employee);


    }

    @Test
    public void testLogin() {
        String email = "employee@softvision.com";
        String password = null;

        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria = criteria.andOperator(Criteria.where("emailId").is(email),
                Criteria.where("password").is(password));
        query = query.addCriteria(criteria);
        Mockito.when(mongoTemplate.findOne(query, Employee.class)).thenReturn(optionalEmployees.get());
        Optional<Employee> interviewerList = employeeServiceImpl.login(email, password);
        assertTrue(interviewerList.isPresent());
    }

    @Test
    public void testAddEmployee() {
        Mockito.when(empRepository.insert(employee)).thenReturn(employee);
        Optional<Employee> employee1 = employeeServiceImpl.addEmployee(employee);
        assertNotNull(employee1);
        assertTrue(employee1.get().getEmailId().equals(employee.getEmailId()));
        assertNotNull(employee1.get().getIsDeleted());
    }

    @Test
    public void testFindEmployeeByID() {

        Mockito.when(empRepository.findById("10")).thenReturn(optionalEmployees);
        Optional<Employee> employee1 = employeeServiceImpl.getEmployeeById("10");
        Employee employee2 = optionalEmployees.get();
        assertNotNull(employee1);
        assertTrue(employee1.get().getFirstName().equals(employee2.getFirstName()));
    }

    @Test
    public void testDeleteEmployeeByID() {
        Mockito.when(empRepository.findById("10")).thenReturn(optionalEmployees);
        Employee employee1 = optionalEmployees.get();
        Mockito.when(empRepository.save(employee1)).thenReturn(employee1);
        assertEquals(employee1.getIsDeleted(), "N");
    }

    @Test
    public void testDeleteAllEmployee() {
        Mockito.when(empRepository.findAll()).thenReturn(employees);
        Optional<List<Employee>> employeeList = employeeServiceImpl.deleteAllEmployees();
        assertFalse(employeeList.isPresent());
    }


    @Test
    public void testFindAllInterviewers() {
        Mockito.when(empRepository.findAll()).thenReturn(employees);
        Optional<List<Employee>> interviewerList = employeeServiceImpl.getAllInterviewers();
        assertTrue(interviewerList.isPresent());
    }

    @Test
    public void testFindAllRecruiters() {
        Mockito.when(empRepository.findAll()).thenReturn(employees);
        Optional<List<Employee>> recruiterList = employeeServiceImpl.getAllRecruiters();
        assertTrue(recruiterList.isPresent());
    }

    @Test
    public void testUpdateEmployee() {

        Mockito.when(empRepository.findById("10")).thenReturn(optionalEmployees);
        Employee employee1 = optionalEmployees.get();
        employee1.setFirstName("User1");
        employee1.setLastName("User1");
        employee1.setIsDeleted("N");
        employee1.setModifiedDate(LocalDateTime.now());
        Mockito.when(empRepository.save(employee1)).thenReturn(employee1);
        Optional<Employee> updatedEmployee = employeeServiceImpl.updateEmployee(employee, "10");
        assertEquals(updatedEmployee.get().getFirstName(), employee1.getFirstName());
        assertEquals(updatedEmployee.get().getLastName(), employee1.getLastName());
    }

    @Test
    public void testSearchInterviewer() {
        String str = "employee";
        LOGGER.info(" Search string is : {} ", str);
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("employeeType").is(EmployeeType.I), Criteria.where("isDeleted").is("N"))
                .orOperator(
                        Criteria.where("firstName").regex(str, "si")
                        , Criteria.where("lastName").regex(str, "si")
                        , Criteria.where("emailId").regex(str, "si")
                        , Criteria.where("employeeId").regex(str, "si")
                        , Criteria.where("technologyCommunity").regex(str, "si"));

        Query query = new Query(criteria);
        Mockito.when(mongoTemplate.find(query, Employee.class)).thenReturn(employees);
        List<Employee> interviewerList = employeeServiceImpl.searchInterviewer("employee");
        assertEquals(interviewerList.size(), 0);
    }

    @Test
    public void testSearchRecruiter() {
        String str = "employee";
        LOGGER.info(" Search string is : {} ", str);
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("employeeType").is(EmployeeType.R), Criteria.where("isDeleted").is("N"))
                .orOperator(
                        Criteria.where("firstName").regex(str, "si")
                        , Criteria.where("lastName").regex(str, "si")
                        , Criteria.where("emailId").regex(str, "si")
                        , Criteria.where("employeeId").regex(str, "si"));

        Query query = new Query(criteria);
        Mockito.when(mongoTemplate.find(query, Employee.class)).thenReturn(employees);
        List<Employee> recruiterList = employeeServiceImpl.searchRecruiter("employee");
        assertEquals(recruiterList.size(), 0);
    }

    @Test
    public void testGetAllDefaultByBandExp() {
        String technicalCommunity = "JAVA";
        int bandExperience = 7;

        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("employeeType").is(EmployeeType.I),
                Criteria.where("bandExperience").gte(7),
                Criteria.where("technologyCommunity").is(TechnologyCommunity.valueOf(technicalCommunity))
        );
        query.addCriteria(criteria);
        Mockito.when(mongoTemplate.find(query, Employee.class)).thenReturn(employees);
        Optional<List<Employee>> employeeList = employeeServiceImpl.getAllEmployeesByBandExp(7, "DOTNET");
        assertTrue(employeeList.isPresent());
    }

    @Test
    public void testGetTechStack() {
        Optional<List<TechnologyCommunity>> list = employeeServiceImpl.getTechStack();
        assertTrue(list.isPresent());
    }

    @Test
    public void testGetEmployeeType() {
        Optional<List<EmployeeType>> list = employeeServiceImpl.getEmployeeType();
        assertTrue(list.isPresent());

    }

    @Test
    public void testGetInterviewerByType() {
        String technicalCommunity = "DOTNET";
        String interviewerType = "I";
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.andOperator(
                Criteria.where("technologyCommunity").is(TechnologyCommunity.valueOf(technicalCommunity)),
                Criteria.where("interviewerType").is(InterviewerType.valueOf(interviewerType)));
        query.addCriteria(criteria);
        Mockito.when(mongoTemplate.find(query, Employee.class)).thenReturn(employees);
        Optional<List<Employee>> employeeList = employeeServiceImpl.getInterviewerByType("DOTNET", "I");
        assertTrue(employeeList.isPresent());

    }


}



