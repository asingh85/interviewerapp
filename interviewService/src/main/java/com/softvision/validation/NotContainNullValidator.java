package com.softvision.validation;

import java.util.Collection;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.stream.Collectors.toList;


public class NotContainNullValidator implements ConstraintValidator<NotContainNull, Collection<Object>> {

    @Override
    public void initialize(NotContainNull notEmptyFields) {
    }

    @Override
    public boolean isValid(Collection<Object> objects, ConstraintValidatorContext constraintValidatorContext) {
        return (objects != null) ? objects.stream().filter(Objects::isNull).collect(toList()).size()== 0 : true;
    }

}