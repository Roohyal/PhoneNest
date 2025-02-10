package com.mathias.phonenest.service.Impl;

import com.mathias.phonenest.domain.entities.Contact;
import com.mathias.phonenest.domain.enums.Group;
import com.mathias.phonenest.exceptions.AlreadyExistException;
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
    public ContactReportDto searchContacts(String query) {
        // Query the repository for the first contact that contains the query (ignoring case)
        // in any of the following fields: firstName, lastName, email, or phoneNumber.
        Optional<Contact> optionalContact = contactRepository
                .findFirstByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(
                        query, query, query, query
                );

        // If a matching contact is found, map the Contact entity to a ContactReportDto and return it.
        if (optionalContact.isPresent()) {
            Contact contact = optionalContact.get();
            return ContactReportDto.builder()
                    .firstName(contact.getFirstName())
                    .lastName(contact.getLastName())
                    .phoneNumber(contact.getPhoneNumber())
                    .email(contact.getEmail())
                    .contactImage(contact.getContactImage())
                    .address(contact.getAddress())
                    .groupName(contact.getGroupName())
                    .build();
        }

        // If no matching contact is found, throw a custom exception indicating that.
        throw new NotFoundException("No contact found matching query: " + query);
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
}
