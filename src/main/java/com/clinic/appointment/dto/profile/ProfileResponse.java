package com.clinic.appointment.dto.profile;

import com.clinic.appointment.dto.appUser.AppUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse<T> {
    private T object;
    private AppUserDTO user;
}
