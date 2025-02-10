package com.mathias.phonenest.service;

import com.mathias.phonenest.domain.entities.Contact;
import com.mathias.phonenest.domain.enums.Group;
import com.mathias.phonenest.payload.request.ContactRequest;
import com.mathias.phonenest.payload.request.UpdateContactRequest;
import com.mathias.phonenest.payload.response.ContactReportDto;
import com.mathias.phonenest.payload.response.ContactResponse;

import java.util.List;

public interface ContactService {

 ContactResponse createContact(ContactRequest contactRequest);

 ContactResponse updateContact(Long id, UpdateContactRequest updateRequest);

 String deleteContact(Long contactId);

 String bulkDeleteContacts(List<Long> ids);

 List<ContactReportDto> getAllContacts();

 ContactReportDto searchContacts(String query);

 List<ContactReportDto> getContactByGroup(Group groupName);

 void saveAllContacts(List<Contact> contacts);

 List<Contact> getAllContactsEntity();

}
