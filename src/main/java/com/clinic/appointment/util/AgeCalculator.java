package com.clinic.appointment.util;

import java.time.LocalDate;
import java.time.Period;

public class AgeCalculator {

    public static int calculateAge(LocalDate dob) {
        if(dob == null){
            throw new IllegalArgumentException("Birth date cannot be null");
        }

        return Period.between(dob, LocalDate.now()).getYears();
    }

}
