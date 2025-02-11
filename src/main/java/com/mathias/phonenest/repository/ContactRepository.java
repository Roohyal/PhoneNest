package com.mathias.phonenest.repository;

import com.mathias.phonenest.domain.entities.Contact;
import com.mathias.phonenest.domain.enums.Group;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    /**
     * Retrieves a Contact entity by its email address.
     *
     * @param email the email address to search for
     * @return an Optional containing the Contact if a match is found; otherwise, an empty Optional
     */
    Optional<Contact> findByEmail(String email);

    /**
     * Retrieves a Contact entity by its phone number.
     *
     * @param phoneNumber the phone number to search for
     * @return an Optional containing the Contact if a match is found; otherwise, an empty Optional
     */
    Optional<Contact> findByPhoneNumber(String phoneNumber);

    /**
     * Retrieves a list of Contact entities that belong to the specified group.
     * The returned list is sorted according to the given Sort parameter.
     *
     * @param groupName the group (e.g., FAMILY, FRIENDS, WORK) to filter contacts by
     * @param sort the sorting criteria to apply to the result list
     * @return a List of Contact entities that belong to the specified group, sorted as per the provided criteria
     */
    List<Contact> findByGroupName(Group groupName, Sort sort);


    /**
     * Finds all Contact entities where any of the following fields contain the specified search term,
     * ignoring case differences:
     * - firstName
     * - lastName
     * - email
     * - phoneNumber
     * This method leverages Spring Data JPA's query derivation mechanism to generate a query based
     * on the method name. It returns a list of contacts that match the search criteria in any of the
     * specified fields.
     *
     * @param firstName   the search term to match against the contact's first name (case-insensitive)
     * @param lastName    the search term to match against the contact's last name (case-insensitive)
     * @param email       the search term to match against the contact's email address (case-insensitive)
     * @param phoneNumber the search term to match against the contact's phone number (case-insensitive)
     * @return a List of Contact objects that match any of the given search criteria
     */
    List<Contact> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(
            String firstName, String lastName, String email, String phoneNumber);

}
