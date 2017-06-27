package com.cherry.youliao.utils;

import com.cherry.youliao.service.exception.InvalidParamsException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Iterator;
import java.util.Set;

@Component
public class ObjectValidator implements InitializingBean {

    private Validator validator;

    @Override
    public void afterPropertiesSet() throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public <T> void check(T object) throws InvalidParamsException {

        Set<ConstraintViolation<T>> results =
                validator.validate(object);

        StringBuffer sb = new StringBuffer();
        Iterator<ConstraintViolation<T>> iterator = results.iterator();
        while (iterator.hasNext()) {
            ConstraintViolation<T> violation = iterator.next();
            sb.append(violation.getMessage());
            sb.append(" ");
        }

        if (!results.isEmpty()) {
            throw new InvalidParamsException(sb.toString());
        }
    }

}
