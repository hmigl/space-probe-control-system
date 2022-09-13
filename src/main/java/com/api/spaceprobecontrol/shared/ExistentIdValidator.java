package com.api.spaceprobecontrol.shared;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ExistentIdValidator implements ConstraintValidator<ExistentId, Object> {
    private String domainAttribute;
    private Class<?> clazz;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(ExistentId constraintAnnotation) {
        domainAttribute = constraintAnnotation.fieldName();
        clazz = constraintAnnotation.domainClass();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        Query query = entityManager.createQuery("select 1 from "+ clazz.getName() + " where " + domainAttribute + "=:o");
        query.setParameter("o", o);
        List<?> list = query.getResultList();
        return !list.isEmpty();
    }
}
