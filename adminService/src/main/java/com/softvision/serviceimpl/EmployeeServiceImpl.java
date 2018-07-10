package com.softvision.serviceimpl;

import com.softvision.common.ServiceConstants;
import com.softvision.exception.EmployeeServiceException;
import com.softvision.model.Employee;
import com.softvision.model.EmployeeType;
import com.softvision.model.InterviewerType;
import com.softvision.model.TechnologyCommunity;
import com.softvision.repository.EmployeeRepository;
import com.softvision.service.EmployeeService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService<Employee> {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Inject
    EmployeeRepository employeeRepository;

    @Inject
    MongoTemplate mongoTemplate;


    @Override
    public Optional<Employee> register() {
        Employee employee = new Employee();
        LocalDateTime localDateTime = LocalDateTime.now();
        employee.setCreatedDate(localDateTime);
        employee.setModifiedDate(localDateTime);
        employee.setFirstName(ServiceConstants.ADMIN);
        employee.setLastName(ServiceConstants.ADMIN);
        employee.setIsDeleted(ServiceConstants.NO);
        employee.setEmailId(ServiceConstants.DEFAULT_ADMIN_EMAIL);
        employee.setPassword(ServiceConstants.ADMIN);
        employee.setEmployeeType(EmployeeType.A);
        employee.setBandExperience(0);
        employee.setInterviewerType(InterviewerType.I);
        employee.setTechnologyCommunity(null);
        LOGGER.info(" Exit  from addEmployee() ");
        return Optional.of(employeeRepository.insert(employee));
    }

    @Override
    public Optional<Employee> login(String email, String password) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria = criteria.andOperator(Criteria.where("emailId").is(email),
                Criteria.where("password").is(password));
        query = query.addCriteria(criteria);
        System.out.println(query.toString());
        return Optional.of(mongoTemplate.findOne(query, Employee.class));
    }

    @Override
    public Optional<List<Employee>> getAllRecruiters() {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("employeeType").regex(EmployeeType.R.toString(), "si"),
                Criteria.where("isDeleted").regex("N", "si").andOperator(criteria));
        Query query = new Query(criteria);
        List<Employee> employees = mongoTemplate.find(query, Employee.class);
        return Optional.of(employees);
    }

    @Override
    public Optional<List<Employee>> getAllInterviewers() {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("employeeType").regex(EmployeeType.I.toString(), "si"),
                Criteria.where("isDeleted").regex("N", "si"));
        Query query = new Query(criteria);
        List<Employee> employees = mongoTemplate.find(query, Employee.class);
        return Optional.of(employees);
    }


    @Override
    public Optional<Employee> getEmployeeById(String id) {
        LOGGER.info("EmployeeServiceImpl ID is : {} ", id);
        return Optional.of(employeeRepository.findById(id).get());
    }


    @Override
    public List<Employee> searchInterviewer(String str) {
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
        System.out.println(query.toString());
        return mongoTemplate.find(query, Employee.class);
    }


    @Override
    public List<Employee> searchRecruiter(String str) {
        LOGGER.info(" Search string is : {} ", str);

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("employeeType").is(EmployeeType.R), Criteria.where("isDeleted").is("N"))
                .orOperator(
                        Criteria.where("firstName").regex(str, "si")
                        , Criteria.where("lastName").regex(str, "si")
                        , Criteria.where("emailId").regex(str, "si")
                        , Criteria.where("employeeId").regex(str, "si"));

        Query query = new Query(criteria);
        System.out.println(query.toString());
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public Optional<Employee> addEmployee(Employee employee) {
        LOGGER.info(" Entered into addEmployee() ");
        LocalDateTime localDateTime = LocalDateTime.now();
        employee.setCreatedDate(localDateTime);
        employee.setModifiedDate(localDateTime);
        employee.setIsDeleted("N");
        employee.setPassword(employee.getFirstName());

        if (employee.getEmployeeType().equals(EmployeeType.R)) {
            employee.setBandExperience(0);
            employee.setInterviewerType(null);
            employee.setTechnologyCommunity(null);
        }
        LOGGER.info(" Exit  from addEmployee() ");
        return Optional.of(employeeRepository.insert(employee));
    }

    @Override
    public Optional<Employee> updateEmployee(Employee employee, String id) {
        LOGGER.info("EmployeeServiceImpl updateEmployee()  ID is :{}", id);

        Optional<Employee> interviewerDAO = employeeRepository.findById(id);
        if(interviewerDAO.isPresent()){
            Employee existingEmployee = interviewerDAO.get();

            if (employee.getEmployeeId() != null && !(employee.getEmployeeId().equals(existingEmployee.getEmployeeId())))
                existingEmployee.setEmployeeId(employee.getEmployeeId());
            if (employee.getFirstName() != null && !(employee.getFirstName().equals(existingEmployee.getFirstName())))
                existingEmployee.setFirstName(employee.getFirstName());
            if (employee.getLastName() != null && !(employee.getLastName().equals(existingEmployee.getLastName())))
                existingEmployee.setLastName(employee.getLastName());
            if (employee.getEmailId() != null && !(employee.getEmailId().equals(existingEmployee.getEmailId())))
                existingEmployee.setEmailId(employee.getEmailId());
            if (employee.getContactNumber() != null && !(employee.getContactNumber().equals(existingEmployee.getContactNumber())))
                existingEmployee.setContactNumber(employee.getContactNumber());

            if (employee.getIsDeleted() != null && !(employee.getIsDeleted().equals(existingEmployee.getIsDeleted()))) {
                existingEmployee.setIsDeleted(employee.getIsDeleted());
            }

            LocalDateTime localDateTime = LocalDateTime.now();
            employee.setModifiedDate(localDateTime);

            if (existingEmployee.getEmployeeType().equals(EmployeeType.I) || existingEmployee.getEmployeeType().equals(EmployeeType.A)) {

                if (employee.getTechnologyCommunity() != null && !(employee.getTechnologyCommunity().equals(existingEmployee.getTechnologyCommunity())))
                    existingEmployee.setTechnologyCommunity(employee.getTechnologyCommunity());
                if (employee.getBandExperience() != 0 && !(employee.getBandExperience() == (existingEmployee.getBandExperience())))
                    existingEmployee.setBandExperience(employee.getBandExperience());
                if (employee.getInterviewerType() != null && !(employee.getInterviewerType().equals(existingEmployee.getInterviewerType())))
                    existingEmployee.setInterviewerType(employee.getInterviewerType());
            }
            return Optional.of(employeeRepository.save(existingEmployee));
        }else {
            throw new EmployeeServiceException("Employee Not Found!");
        }
    }

    @Override
    public Optional<Employee> deleteEmployee(String id) {
        LOGGER.info("EmployeeServiceImpl deleteEmployee()  ID is :{}", id);
        Optional<Employee> employeeDAO = employeeRepository.findById(id);
        Optional<Employee> returnEmployee = Optional.empty();
        if (employeeDAO.isPresent()) {
            LOGGER.info("EmployeeServiceImpl deleteEmployee()  is not empty");
            Employee optEmployee = employeeDAO.get();
            optEmployee.setIsDeleted(ServiceConstants.YES);
            optEmployee.setModifiedDate(LocalDateTime.now());
            returnEmployee = Optional.of(employeeRepository.save(optEmployee));
        } else {
            throw new EmployeeServiceException("Employee Not Found!");
        }
        LOGGER.info("EmployeeServiceImpl exit from deleteEmployee()");
        return returnEmployee;
    }


    @Override
    public Optional<List<Employee>> deleteAllEmployees() {
        LOGGER.info("EmployeeServiceImpl entered into deleteAllEmployees()  ");
        List<Employee> employeeList = employeeRepository.findAll();
        employeeList.forEach(employee -> employee.setIsDeleted(ServiceConstants.YES));
        employeeRepository.saveAll(employeeList);
        LOGGER.info("EmployeeServiceImpl exit from deleteAllEmployees()  ");

        return Optional.empty();
    }


    @Override
    public Optional<List<Employee>> getAllEmployeesByBandExp(int expInmonths, String technicalCommunity) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("employeeType").regex(EmployeeType.I.toString(),"si"),
                Criteria.where("bandExperience").gte(expInmonths),
                Criteria.where("technologyCommunity").regex(TechnologyCommunity.valueOf(technicalCommunity).toString(),"si")
        );
        query.addCriteria(criteria);
        List<Employee> employees = mongoTemplate.find(query, Employee.class);
        if (employees == null || employees.isEmpty()) {
            employees = getByDefaultByBandExp(technicalCommunity);
        }
        LOGGER.info("Interviewers information {} :", employees);
        return Optional.of(employees);
    }


    private List<Employee> getByDefaultByBandExp(String technicalCommunity) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("employeeType").is(EmployeeType.I),
                Criteria.where("bandExperience").gte(7),
                Criteria.where("technologyCommunity").is(TechnologyCommunity.valueOf(technicalCommunity))
        );
        query.addCriteria(criteria);
        List<Employee> employees = mongoTemplate.find(query, Employee.class);
        LOGGER.info(" getByDefaultByBandExp | Interviewers information {} :", employees);
        return employees;
    }

    @Override
    public Optional<List<TechnologyCommunity>> getTechStack() {
        LOGGER.info("EmployeeServiceImpl entered into getTechStack()");
        List<TechnologyCommunity> list = Arrays.asList(TechnologyCommunity.values());
        LOGGER.info("EmployeeServiceImpl entered into getTechStack()is {} :", list);
        return Optional.of(list);
    }


    @Override
    public Optional<List<EmployeeType>> getEmployeeType() {
        LOGGER.info("EmployeeServiceImpl entered into getEmployeeType()");
        List<EmployeeType> list = Arrays.asList(EmployeeType.values());
        LOGGER.info("EmployeeServiceImpl entered into getEmployeeType() is {} :", list);
        return Optional.of(list);
    }

    @Override
    public Optional<List<Employee>> getInterviewerByType(String technicalCommunity, String interviewerType) {
        LOGGER.info("EmployeeServiceImpl entered into getInterviewerByType()");
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