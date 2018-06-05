package com.softvision.interview.interviewapp.model;

import java.util.List;
import java.util.Objects;
import javax.persistence.GeneratedValue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Interviewer)) return false;
        Interviewer that = (Interviewer) o;
        return id == that.id &&
                isDeleted == that.isDeleted &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(emailId, that.emailId) &&
                Objects.equals(contactNumber, that.contactNumber) &&
                Objects.equals(listTechStack, that.listTechStack);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, firstName, lastName, emailId, contactNumber, listTechStack, isDeleted);
    }

    @Override
    public String toString() {
        return "Interviewer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailId='" + emailId + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", listTechStack=" + listTechStack +
                ", isDeleted=" + isDeleted +
                '}';
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public List<String> getListTechStack() {
        return listTechStack;
    }

    public void setListTechStack(List<String> listTechStack) {
        this.listTechStack = listTechStack;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

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
