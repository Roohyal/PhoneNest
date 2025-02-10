package com.mathias.phonenest.infrastucture.controller;


import com.mathias.phonenest.domain.entities.Contact;
import com.mathias.phonenest.domain.enums.Group;
import com.mathias.phonenest.payload.request.ContactRequest;
import com.mathias.phonenest.payload.request.UpdateContactRequest;
import com.mathias.phonenest.payload.response.ContactReportDto;
import com.mathias.phonenest.payload.response.ContactResponse;
import com.mathias.phonenest.service.ContactService;
import com.mathias.phonenest.util.CsvHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;


    /**
     * Create a new contact.
     * URL: POST /api/contacts/add-contact
     * The method accepts a ContactRequest in the request body, which contains all the necessary
     * details for a new contact. It delegates the creation process to the contactService.
     * @param contact the contact details from the request body.
     * @return a ResponseEntity containing the ContactResponse and an HTTP CREATED (201) status.
     */
    @PostMapping("/add-contact")
    public ResponseEntity<ContactResponse> createContact(@RequestBody ContactRequest contact) {
        // Call the service to create a new contact.
        ContactResponse created = contactService.createContact(contact);
        // Return the created contact response with HTTP status 201 (Created).
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Update an existing contact.
     * URL: PUT /api/contacts/update-contact?id={id}
     * The method accepts the contact's ID as a request parameter and the updated contact details
     * in the request body as an UpdateContactRequest.
     * @param id the ID of the contact to update.
     * @param updateRequest the updated contact details.
     * @return a ResponseEntity containing the updated ContactResponse and an HTTP OK (200) status.
     */
    @PutMapping("/update-contact")
    public ResponseEntity<ContactResponse> updateContact(@RequestParam Long id,
                                                         @RequestBody UpdateContactRequest updateRequest) {
        // Update the contact using the service layer.
        ContactResponse response = contactService.updateContact(id, updateRequest);
        // Return the updated contact response with HTTP status 200 (OK).
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a single contact by its ID.
     * URL: DELETE /api/contacts/delete-contact?contactId={contactId}
     * The method accepts the contact ID as a request parameter and delegates the deletion process
     * to the service layer.
     *
     * @param contactId the ID of the contact to be deleted.
     * @return a ResponseEntity containing a deletion confirmation message and an HTTP OK (200) status.
     */
    @DeleteMapping("/delete-contact")
    public ResponseEntity<?> deleteContact(@RequestParam Long contactId) {
        // Delete the contact using the service layer.
        String result = contactService.deleteContact(contactId);
        // Return the result message with HTTP status 200 (OK).
        return ResponseEntity.ok(result);
    }

    /**
     * Search for a contact using a query string.
     * URL: GET /api/contacts/search?query={query}
     * The search will look into firstName, lastName, email, or phoneNumber fields and return the first match.
     *
     * @param query the search term to be used.
     * @return a ResponseEntity containing a ContactReportDto for the matched contact and an HTTP OK (200) status.
     */
    @GetMapping("/search")
    public ResponseEntity<ContactReportDto> searchContact(@RequestParam String query) {
        // Search for the contact using the provided query string.
        ContactReportDto contact = contactService.searchContacts(query);
        // Return the found contact as a DTO with HTTP status 200 (OK).
        return ResponseEntity.ok(contact);
    }


    /**
     * Retrieve all contacts.
     * URL: GET /api/contacts/get-all-contacts
     * This endpoint retrieves all contacts, typically sorted by a certain order (e.g., alphabetically).
     *
     * @return a ResponseEntity containing a list of ContactReportDto objects and an HTTP OK (200) status.
     */
    @GetMapping("/get-all-contacts")
    public ResponseEntity<List<ContactReportDto>> getAllContacts() {
        // Retrieve all contacts from the service layer.
        List<ContactReportDto> contact = contactService.getAllContacts();
        // Return the list of contacts with HTTP status 200 (OK).
        return ResponseEntity.ok(contact);
    }

    /**
     * Retrieve contacts filtered by group.
     * URL: GET /api/contacts/get-contact-by-group?groupName={groupName}
     * The endpoint accepts a group name as a request parameter and returns contacts belonging to that group.
     *
     * @param groupName the group to filter contacts by.
     * @return a ResponseEntity containing a list of ContactReportDto objects for the specified group and an HTTP OK (200) status.
     */
    @GetMapping("/get-contact-by-group")
    public ResponseEntity<?> getContactByGroup(@RequestParam Group groupName) {
        // Retrieve contacts that belong to the specified group.
        List<ContactReportDto> contact = contactService.getContactByGroup(groupName);
        // Return the list of filtered contacts with HTTP status 200 (OK).
        return ResponseEntity.ok(contact);
    }

    /**
     * Bulk delete contacts.
     * URL: DELETE /api/contacts/bulk-delete
     * The endpoint accepts a list of contact IDs in the request body and deletes all corresponding contacts.
     *
     * @param ids the list of contact IDs to be deleted.
     * @return a ResponseEntity containing a deletion confirmation message and an HTTP OK (200) status.
     */
    @DeleteMapping("/bulk-delete")
    public ResponseEntity<String> bulkDeleteContacts(@RequestBody List<Long> ids) {
        // Delete multiple contacts using the service layer.
        String result = contactService.bulkDeleteContacts(ids);
        // Return the result message with HTTP status 200 (OK).
        return ResponseEntity.ok(result);
    }

    /**
     * Import contacts via CSV file.
     *
     * Endpoint: POST /api/contacts/import
     *
     * Expects a multipart file with CSV data.
     * Sample CSV template header:
     * firstName,lastName,email,phoneNumber,contactImage,address,groupName
     *
     * @param file the uploaded CSV file.
     * @return a ResponseEntity with a success or error message.
     */
    @PostMapping("/import")
    public ResponseEntity<String> importContacts(@RequestParam("file") MultipartFile file) {
        // Validate that the uploaded file is a CSV.
        if (!CsvHelper.hasCSVFormat(file)) {
            return ResponseEntity.badRequest().body("Please upload a CSV file.");
        }
        try {
            // Parse the CSV file to a list of Contact objects.
            List<Contact> contacts = CsvHelper.csvToContacts(file.getInputStream());
            // Save all the contacts using the service layer.
            contactService.saveAllContacts(contacts);
            return ResponseEntity.ok("Uploaded and imported CSV file successfully: " + file.getOriginalFilename());
        } catch (IOException e) {
            return ResponseEntity.status(417).body("Failed to import CSV file: " + e.getMessage());
        }
    }

    /**
     * Export all contacts as a CSV file.
     *
     * Endpoint: GET /api/contacts/export
     *
     * This endpoint retrieves all contacts from the database and converts them to CSV format.
     *
     * @return a ResponseEntity containing the CSV file as an attachment.
     */
    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportContacts() {
        String filename = "contacts.csv";
        // Retrieve all contacts from the service layer.
        List<Contact> contacts = contactService.getAllContactsEntity();
        // Convert the contacts list to CSV format.
        ByteArrayInputStream in = CsvHelper.contactsToCSV(contacts);
        // Prepare the HTTP headers for file download.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + filename);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(new InputStreamResource(in));
    }

}
