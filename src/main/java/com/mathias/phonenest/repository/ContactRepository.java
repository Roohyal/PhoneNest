package com.mathias.phonenest.repository;

import com.mathias.phonenest.domain.entities.Contact;
import com.mathias.phonenest.domain.enums.Group;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    Optional<Contact> findByEmail(String email);

    Optional<Contact> findByPhoneNumber(String phoneNumber);

    List<Contact> findByGroupName(Group groupName, Sort sort);

    List<Contact> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(
            String firstName, String lastName, String email, String phoneNumber);


}
