package com.mathias.phonenest.payload.response;

import com.mathias.phonenest.domain.enums.Group;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactReportDto {

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String email;

    private String contactImage;

    private String address;

    @Enumerated(EnumType.STRING)
    private Group groupName;
}
