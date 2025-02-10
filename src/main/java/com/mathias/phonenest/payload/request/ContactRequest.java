package com.mathias.phonenest.payload.request;

import com.mathias.phonenest.domain.enums.Group;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactRequest {

    @NotBlank(message = "FirstName is required")
    private String firstName;

    @NotBlank(message = "LastName is required")
    private String lastName;

    @Column(nullable = false)
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Email Format is Wrong ")
    private String email;

    private String contactImage;

    private String address;

    @Enumerated(EnumType.STRING)
    private Group groupName;
}
