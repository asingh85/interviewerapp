package com.softvision.serviceimpl;

import com.softvision.model.Employee;
import com.softvision.model.EmployeeType;
import com.softvision.model.InterviewerType;
import com.softvision.model.TechnologyCommunity;
import com.softvision.repository.EmployeeRepository;
import com.softvision.service.EmployeeService;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService<Employee> {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Inject
    EmployeeRepository employeeRepository;

    @Inject
    MongoTemplate mongoTemplate;


    /*
     * (non-Javadoc)
     *
     * @see
     * com.softvision.service.EmployeeService #getAllEmployees()
     */
    @Override
    public Optional<List<Employee>> getAllEmployees() {
        return Optional.of(employeeRepository.findAll());
    }

    @Override
    public Optional<List<Employee>> getAllRecruiters() {
        return Optional.of(employeeRepository.findAll());
    }

    @Override
    public Optional<List<Employee>> getAllInterviewers() {
        return Optional.of(employeeRepository.findAll());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.softvision.service.EmployeeService #getEmployeeById()
     */
    @Override
    public Optional<Employee> getEmployeeById(String id) {
        LOGGER.info("InterviewerServiceImpl ID is : {} ", id);
        return Optional.of(employeeRepository.findById(id).get());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.softvision.service.EmployeeService #search()
     */
    @Override
    public List<Employee> search(String str) {
        LOGGER.info(" Search string is : {} ", str);
        StringBuilder covertStr = new StringBuilder();
        covertStr.append("/").append(str).append("/");
        Criteria criteria = new Criteria();
        criteria = criteria.orOperator(
                Criteria.where("firstName").regex(str, "si")
                , Criteria.where("lastName").regex(str, "si")
                , Criteria.where("technologyCommunity").regex(str, "si")
                , Criteria.where("interviewerID").regex(str, "si"));


        Query query = new Query(criteria);

        System.out.println(query.toString());
        return mongoTemplate.find(query, Employee.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.softvision.service.EmployeeService #addEmployee()
     */
    @Override
    public Optional<Employee> addEmployee(Employee employee) {
        LOGGER.info(" Entered into addEmployee() ");
        LocalDateTime localDateTime = LocalDateTime.now();
        employee.setCreatedDate(localDateTime);
        employee.setModifiedDate(localDateTime);
        LOGGER.info(" Exit  from addEmployee() ");
        return Optional.of(employeeRepository.insert(employee));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.softvision.service.EmployeeService #updateEmployee()
     */
    @Override
    public Optional<Employee> updateEmployee(Employee employee, String id) {
        LOGGER.info("EmployeeServiceImpl updateEmployee()  ID is :{}", id);

        Optional<Employee> interviewerDAO = employeeRepository.findById(id);
        Employee existingEmployee =interviewerDAO.get();

        employee.setId(id);
        LocalDateTime localDateTime = LocalDateTime.now();
        employee.setCreatedDate(existingEmployee.getCreatedDate());
        employee.setModifiedDate(localDateTime);
        return Optional.of(employeeRepository.save(employee));
     }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.softvision.service.EmployeeService #deleteEmployee(java.lang.String)
     */
    @Override
    public void deleteEmployee(String id) {
        LOGGER.info("EmployeeServiceImpl deleteInterviewer()  ID is :{}", id);
        Optional<Employee> employeeDAO = employeeRepository.findById(id);
        if (employeeDAO.isPresent()) {
            LOGGER.info("EmployeeServiceImpl deleteRecruiter()  is not empty");
            Employee optEmployee = employeeDAO.get();
            optEmployee.setDeleted(true);
            optEmployee.setModifiedDate(LocalDateTime.now());
            employeeRepository.save(optEmployee);
        }
        LOGGER.info("EmployeeServiceImpl exit from deleteRecruiter()");

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.softvision.service.EmployeeService #deleteAllEmployees()
     */
    @Override
    public void deleteAllEmployees() {
        LOGGER.info("EmployeeServiceImpl entered into deleteAllRecruiter()  ");
        List<Employee> employeeList = employeeRepository.findAll();
        employeeList.forEach(employee -> employee.setDeleted(true));
        employeeRepository.saveAll(employeeList);
        LOGGER.info("EmployeeServiceImpl exit from deleteAllRecruiter()  ");

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.softvision.service.EmployeeService #getAllEmployeesByBandExp()
     */
    @Override
    public Optional<List<Employee>> getAllEmployeesByBandExp(int expInmonths, String technicalCommunity) {
        Query query = new Query();

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("employeeType").is(EmployeeType.INTERVIEWER),
                Criteria.where("technologyCommunity").is(technicalCommunity)
                , Criteria.where("bandExperience").gte(expInmonths));
        List<Employee> employees = mongoTemplate.find(query, Employee.class);
        LOGGER.info("Interviewers information {} :", employees);
        return Optional.of(employees);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.softvision.service.EmployeeService #getTechStack()
     */
    @Override
    public Optional<List<TechnologyCommunity>> getTechStack() {
        List<TechnologyCommunity> list = Arrays.asList(TechnologyCommunity.values());
        return Optional.of(list);
    }


    /*
     * (non-Javadoc)
     *
     * @see
     * com.softvision.service.EmployeeService #getEmployeeType()
     */
    @Override
    public Optional<List<EmployeeType>> getEmployeeType() {
        List<EmployeeType> list = Arrays.asList(EmployeeType.values());
        return Optional.of(list);
    }

    @Override
    public Optional<List<Employee>> getInterviewerByType(String technicalCommunity, String interviewerType) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.andOperator(
                Criteria.where("technologyCommunity").is(TechnologyCommunity.valueOf(technicalCommunity)),
                Criteria.where("interviewerType").is(InterviewerType.valueOf(interviewerType)));
        query.addCriteria(criteria);
        List<Employee> employees = mongoTemplate.find(query, Employee.class);
        LOGGER.info("Interviewers information {} :", employees);
        return Optional.of(employees);
    }
}
