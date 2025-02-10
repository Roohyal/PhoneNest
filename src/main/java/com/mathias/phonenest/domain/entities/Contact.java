package com.mathias.phonenest.domain.entities;

import com.mathias.phonenest.domain.enums.Group;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "contact_tbl")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contact extends BaseClass {

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
