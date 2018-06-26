package com.softvision.model;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

}
