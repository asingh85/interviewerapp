package com.softvision.validation;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;

public class ValidationUtil {

	private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

	public static <T> void validate(T object) throws ValidationException {
		Set<ConstraintViolation<T>> violations = VALIDATOR.validate(object);
		if (!violations.isEmpty()) {
			throw new ValidationException(violations.iterator().next().getMessage());
		}
	}

}
