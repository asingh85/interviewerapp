package com.softvision.model;

import com.softvision.helper.LocalDateTimeAttributeConverter;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "interview")
@Data
public class Interview  {

    @Id
    @GeneratedValue
    private String id;

    @NotNull
    private String candidateId;

    private List<String> interviewerId;

    @NotNull
    private InterviewStatus interviewStatus;

    @NotNull
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime creationTime;

    @NotNull
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime modifiedDate;

    @NotNull
    private boolean isDeleted;
}