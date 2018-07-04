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
public class Employee{

    private String id;

    private String employeeId;

    private String firstName;

     private String lastName;

    private String emailId;

    private String contactNumber;

    private boolean isDeleted;
}
