package com.softvision.model;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Convert;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.softvision.helper.LocalDateTimeAttributeConverter;


/**
 * The Class Candidate.
 *
 * @author arun.p
 */

@Document(collection = "candidate")
@Data
public class Candidate extends CommonEntity implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    @Id
    private String candidateId;

    /** The first name. */
    @NotNull
    private String firstName;

    /** The last name. */
    private String lastName;

    /** The gender. */
    private String gender;

    /** The technology stack. */
    private String technologyStack;

    /** The is active. */
    private Boolean isActive;

    /** The experiance. */
    private String experiance;

    /** The phone number. */
    @NotNull
    private String phoneNumber;

    /** The email. */
    @NotNull
    private String email;

    /** The unique identity number. */
    private String uniqueIdentityNumber;

    /** The resume */
    private File resume;
    
    
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime interviewDate;
    
    
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime interviewTime;

}
