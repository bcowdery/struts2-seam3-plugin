/*
 * Doula Company Client Manager
 * Copyright (c) 2012 Brian Cowdery
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
 */

package com.pointyspoon.struts2;

import com.pointyspoon.struts2.annotations.DefaultValidator;
import com.opensymphony.xwork2.Validateable;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.Serializable;
import java.util.Set;

/**
 * Provides support for JSR-303 (Bean Validation) annotations on a Struts 2 action.
 *
 * @author Brian Cowdery
 * @since  28-Sept-2012
 */
public class SeamValidatorActionSupport extends SeamActionSupport implements Validateable, Serializable {

    @Inject @DefaultValidator Validator validator;

    public void validate() {
        Set<? extends ConstraintViolation<?>> constraintViolations = validator.validate(this);

        for (ConstraintViolation violation : constraintViolations) {
            addFieldError(violation.getPropertyPath().toString(), violation.getMessage());
        }
    }
}
