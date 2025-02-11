package com.mathias.phonenest.service.Impl;

import com.mathias.phonenest.domain.entities.Contact;
import com.mathias.phonenest.domain.enums.Group;
import com.mathias.phonenest.exceptions.AlreadyExistException;
import com.mathias.phonenest.exceptions.InvalidFormatException;
import com.mathias.phonenest.exceptions.NotFoundException;
import com.mathias.phonenest.payload.request.ContactRequest;
import com.mathias.phonenest.payload.request.UpdateContactRequest;
import com.mathias.phonenest.payload.response.ContactReportDto;
import com.mathias.phonenest.payload.response.ContactResponse;
import com.mathias.phonenest.repository.ContactRepository;
import com.mathias.phonenest.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    @Override
    public ContactResponse createContact(ContactRequest contactRequest) {

        // Check if a contact with the provided email already exists.
        // If yes, throw an AlreadyExistException.
        if (contactRepository.findByEmail(contactRequest.getEmail()).isPresent()) {
            throw new AlreadyExistException("A contact with the email " + contactRequest.getEmail() + " already exists.");
        }

        // Check if a contact with the provided phone number already exists.
        // If yes, throw an AlreadyExistException.
        if (contactRepository.findByPhoneNumber(contactRequest.getPhoneNumber()).isPresent()) {
            throw new AlreadyExistException("A contact with the phone number " + contactRequest.getPhoneNumber() + " already exists.");
        }
        // Define a regex pattern for the phone number:
        // ^        -> start of the string
        // \\d{11}  -> exactly 11 digits (\\d represents a digit)
        // $        -> end of the string
        String phoneRegex = "^\\d{11}$";

        // Validate that the phone number matches the regex pattern.
        if (!contactRequest.getPhoneNumber().matches(phoneRegex)) {
            throw new InvalidFormatException("Phone number must be exactly 11 digits long.");
        }

        // Build a new Contact entity from the incoming request
        Contact contact = Contact.builder()
                .firstName(contactRequest.getFirstName())
                .lastName(contactRequest.getLastName())
                .phoneNumber(contactRequest.getPhoneNumber())
                .email(contactRequest.getEmail())
                .contactImage(contactRequest.getContactImage())
                .groupName(contactRequest.getGroupName())
                .address(contactRequest.getAddress())
                .build();

        // Save the newly created Contact entity to the database.
        Contact savedContact = contactRepository.save(contact);

        // Build and return a response indicating successful creation of the contact.
        return ContactResponse.builder()
                .responseCode("001")
                .responseMessage(savedContact.getFirstName() + " has been Saved")
                .build();
    }

    @Override
    public ContactResponse updateContact(Long id, UpdateContactRequest updateRequest) {
        // Retrieve the contact from the repository by its ID.
        Optional<Contact> existingContact = contactRepository.findById(id);

        // If the contact exists, update its fields.
        if (existingContact.isPresent()) {
            Contact existingContactUpdate = existingContact.get();

            // Update the contact's fields using the provided update request.
            existingContactUpdate.setFirstName(updateRequest.getFirstName());
            existingContactUpdate.setLastName(updateRequest.getLastName());
            existingContactUpdate.setPhoneNumber(updateRequest.getPhoneNumber());
            existingContactUpdate.setAddress(updateRequest.getAddress());
            existingContactUpdate.setContactImage(updateRequest.getContactImage());
            existingContactUpdate.setGroupName(updateRequest.getGroupName());

            // Save the updated contact to the database.
            contactRepository.save(existingContactUpdate);

            // Build and return a success response.
            return ContactResponse.builder()
                    .responseCode("002")
                    .responseMessage(existingContactUpdate.getFirstName() + " has been Updated")
                    .build();
        }

        // If the contact is not found, throw a custom NotFoundException.
        throw new NotFoundException("Contact with id " + id + " not found");
    }


    @Override
    public String deleteContact(Long contactId) {

        contactRepository.deleteById(contactId);

        return "Contact has been deleted";
    }

    @Override
    public String bulkDeleteContacts(List<Long> ids) {
        List<Contact> contactList= contactRepository.findAllById(ids);
        contactRepository.deleteAll(contactList);
        return "The Contacts have been deleted";
    }

    @Override
    public List<ContactReportDto> getAllContacts() {
        // Retrieve contacts sorted by first name and last name (alphabetical order)
        List<Contact> contacts = contactRepository.findAll(Sort.by(Sort.Direction.ASC, "firstName", "lastName"));

        // Map the contacts to DTOs
        return contacts.stream()
                .map(contact -> ContactReportDto.builder()
                        .firstName(contact.getFirstName())
                        .lastName(contact.getLastName())
                        .phoneNumber(contact.getPhoneNumber())
                        .email(contact.getEmail())
                        .contactImage(contact.getContactImage())
                        .address(contact.getAddress())
                        .groupName(contact.getGroupName())
                        .build())
                .collect(Collectors.toList());

    }

    @Override
    public List<ContactReportDto> searchContacts(String query) {
        // Query the repository for all contacts where any of the following fields
        // contains the query string (ignoring case): firstName, lastName, email, or phoneNumber.
        List<Contact> contacts = contactRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(
                        query, query, query, query
                );

        // If no matching contacts are found, throw a NotFoundException.
        if (contacts.isEmpty()) {
            throw new NotFoundException("No contact found matching query: " + query);
        }

        // Map each Contact entity to a ContactReportDto using the builder pattern
        // and collect the results into a list.
        return contacts.stream()
                .map(contact -> ContactReportDto.builder()
                        .firstName(contact.getFirstName())
                        .lastName(contact.getLastName())
                        .phoneNumber(contact.getPhoneNumber())
                        .email(contact.getEmail())
                        .contactImage(contact.getContactImage())
                        .address(contact.getAddress())
                        .groupName(contact.getGroupName())
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    public List<ContactReportDto> getContactByGroup(Group group) {
        // Define sorting: first by firstName then by lastName (alphabetical order)
        Sort sort = Sort.by(Sort.Direction.ASC, "firstName", "lastName");

        // Retrieve contacts filtered by group using the repository method
        List<Contact> contacts = contactRepository.findByGroupName(group, sort);

        // Map each Contact to a ContactReportDto using the builder pattern
        return contacts.stream()
                .map(contact -> ContactReportDto.builder()
                        .firstName(contact.getFirstName())
                        .lastName(contact.getLastName())
                        .phoneNumber(contact.getPhoneNumber())
                        .email(contact.getEmail())
                        .contactImage(contact.getContactImage())
                        .address(contact.getAddress())
                        .groupName(contact.getGroupName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void saveAllContacts(List<Contact> contacts) {
        // Save all contacts using Spring Data JPA's saveAll method.
        contactRepository.saveAll(contacts);
    }

    @Override
    public List<Contact> getAllContactsEntity() {
        // Retrieve all contacts from the repository.
        return contactRepository.findAll();
    }
}
