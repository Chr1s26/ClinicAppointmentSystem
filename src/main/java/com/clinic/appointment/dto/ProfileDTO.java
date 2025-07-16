package com.clinic.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileDTO {
    private String username;
    private String profileUrl;
    private String email;
}
