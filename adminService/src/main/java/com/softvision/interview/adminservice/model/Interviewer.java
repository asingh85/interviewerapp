package com.softvision.interview.adminservice.model;

import java.util.List;
import java.util.Objects;
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
public class Interviewer implements Comparable<Interviewer> {

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

    private List<String> listTechStack;

    public void setId(String id) {
        this.id = id;
    }

    public String getInterviewerID() {
        return interviewerID;
    }

    public void setInterviewerID(String interviewerID) {
        this.interviewerID = interviewerID;
    }

    private boolean isDeleted;

    @Override
    public int compareTo(Interviewer o) {
        int val = 0 ;
        if(this.getFirstName().compareTo(o.getFirstName()) == -1 ) {
            val = -1;
        } else if( this.getFirstName().compareTo(o.getFirstName()) == 1) {
            val = 1;
        }
        return val;
    }
}
