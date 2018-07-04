package com.softvision.model;


import com.softvision.helper.LocalDateTimeAttributeConverter;
import lombok.Data;

import javax.persistence.Convert;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CommonEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The created by.
     */
    protected String createdBy;

    /**
     * The created date.
     */
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    protected LocalDateTime createdDate;

    /**
     * The modified by.
     */
    protected String modifiedBy;

    /**
     * The modified date.
     */
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    protected LocalDateTime modifiedDate;

}
