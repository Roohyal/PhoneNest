package com.mathias.phonenest.payload.request;

import com.mathias.phonenest.domain.enums.Group;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UpdateContactRequest {

    @NotBlank(message = "FirstName is required")
    private String firstName;

    @NotBlank(message = "LastName is required")
    private String lastName;

    @Column(nullable = false)
    private String phoneNumber;

    private String contactImage;

    private String address;

    @Enumerated(EnumType.STRING)
    private Group groupName;
}
