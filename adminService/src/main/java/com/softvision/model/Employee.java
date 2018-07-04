package com.softvision.model;

import com.softvision.helper.LocalDateTimeAttributeConverter;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Comparator;

@Data
@Document(collection = "employee")
@NotNull
public class Employee implements Comparable<Employee> {

    @Id
    @GeneratedValue
    private String id;

    @NotNull
    @Min(value = 0, message = "Employee Id cannot be null or empty")
    private String employeeId;

    @NotNull(message = "Interviewer First Name cannot be null")
    @Size(min = 2, max = 100, message = "Interviewer First Name must be atleast 2 and 100 characters")
    @TextIndexed(weight = 2)
    private String firstName;

    @NotNull(message = "Interviewer Last Name cannot be null")
    @Size(min = 0, max = 100, message = "Interviewer Last Name must be atleast 2 and 100 characters")
    @TextIndexed(weight = 2)
    private String lastName;

    private String emailId;

    @Pattern(regexp = "(^$|[0-9]{10})", message = "Invalid Phone number")
    private String contactNumber;

    private boolean isDeleted;

    // Interview / Recruiter
    private EmployeeType employeeType;

    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime createdDate;

    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime modifiedDate;

    @TextIndexed(weight = 2)
    private TechnologyCommunity technologyCommunity;

    private int bandExperience;
    // M-Manager , I-interviewer(default)
    private InterviewerType interviewerType;

    @Override
    public int compareTo(Employee o) {
        return Comparator.comparing(Employee::getFirstName)
                .thenComparing(Employee::getLastName)
                .compare(this, o);
    }
}
