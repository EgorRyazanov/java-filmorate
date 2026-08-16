package ru.yandex.practicum.filmorate.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateValid, LocalDate> {

    private static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28); // 👈 вот здесь год

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value != null && value.isAfter(MIN_DATE);
    }
}