package com.chatop.backend.validation;

import org.springframework.beans.factory.annotation.Autowired;

import com.chatop.backend.entity.User;
import com.chatop.backend.repository.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmailConstraint, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        User emailExists = userRepository.findByEmail(value);
        return emailExists == null ? true : false;
    }

}
