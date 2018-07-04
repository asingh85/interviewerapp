package com.softvision.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Document(collection = "login")
@NotNull
public class Login {

    @Id
    @GeneratedValue
    private String id;

    private String userName;
    private String password;
    private String emailid;
    private String employeeID;
    private List<String> role;

}
