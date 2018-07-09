package com.softvision.model;

import com.softvision.helper.LocalDateTimeAttributeConverter;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Convert;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * The Class Candidate.
 *
 * @author arun.p
 */

@Document(collection = "candidate")
@Data
public class Candidate implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private String candidateId;

    private String firstName;

    private String lastName;

    private String gender;

    private String technologyStack;

    private Boolean isActive;

    private String experience;

    private String phoneNumber;

    private String email;

    private String uniqueIdentityNumber;

    private String interviewObjectID;


  //  private File resume;


    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime interviewDate;


    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime interviewTime;

}
