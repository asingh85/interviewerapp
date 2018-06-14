package com.softvision.entities;

import com.softvision.helper.LocalDateTimeAttributeConverter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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

    private InterviewStatus interviewStatus;

    @Min(0)
    @Max(2)
    private int acceptedCount;

    private String nextInterviewerId;

    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime creationTime;

    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime modifiedDate;

    private boolean isDeleted;
}