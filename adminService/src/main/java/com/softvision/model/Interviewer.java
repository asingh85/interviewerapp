package com.softvision.model;

import com.softvision.helper.LocalDateTimeAttributeConverter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.GeneratedValue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "interviewer")
@NotNull
public class Interviewer implements Comparable<Interviewer>{

    @Id
    @GeneratedValue
    private String id;

    @NotNull
    @Min(value=0,message="Interviewer Id cannot be null or empty")
    private String interviewerID;

    @NotNull(message = "Interviewer First Name cannot be null")
    @Size(min = 2, max = 100, message = "Interviewer First Name must be atleast 2 and 100 characters")
    private String firstName;

    @NotNull(message = "Interviewer Last Name cannot be null")
    @Size(min = 0, max = 100, message = "Interviewer Last Name must be atleast 2 and 100 characters")
    private String lastName;

    private String emailId;

    @Pattern(regexp="(^$|[0-9]{10})",message="Invalid Phone number")
    private String contactNumber;

    private boolean isDeleted;

    @Convert(converter = LocalDateTimeAttributeConverter.class)
    //@NotNull(message = "Creation date cannot be null")
    private LocalDateTime createdDate;

    @Convert(converter = LocalDateTimeAttributeConverter.class)
    //@NotNull(message = "Modified date cannot be null")
    private LocalDateTime modifiedDate;

    private TechnologyCommunity technologyCommunity;

    private int bandExperience;

    @Override
    public int compareTo(Interviewer o) {
        return Comparator.comparing(Interviewer::getFirstName)
                .thenComparing(Interviewer::getLastName)
                .compare(this,o);
    }
}
