package com.mathias.phonenest.util;

import com.mathias.phonenest.domain.entities.Contact;
import com.mathias.phonenest.domain.enums.Group;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvHelper {
    // Define the CSV content type and header names.
    public static final String TYPE = "text/csv";
    static String[] HEADERs = {"firstName", "lastName", "email", "phoneNumber", "contactImage", "address", "groupName"};

    /**
     * Check if the uploaded file is in CSV format.
     *
     * @param file the uploaded file.
     * @return true if the file is CSV, false otherwise.
     */
    public static boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }
    /**
     * Parse CSV data from an InputStream and convert it into a list of Contact objects.
     *
     * Expected CSV header (sample template):
     * firstName,lastName,email,phoneNumber,contactImage,address,groupName
     *
     * @param is the InputStream of the CSV file.
     * @return a List of Contact objects.
     */
    public static List<Contact> csvToContacts(InputStream is) {
        List<Contact> contacts = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim())) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                // Build a Contact using the values from the CSV record.
                Contact contact = Contact.builder()
                        .firstName(csvRecord.get("firstName"))
                        .lastName(csvRecord.get("lastName"))
                        .email(csvRecord.get("email"))
                        .phoneNumber(csvRecord.get("phoneNumber"))
                        .contactImage(csvRecord.get("contactImage"))
                        .address(csvRecord.get("address"))
                        .groupName(Group.valueOf(csvRecord.get("groupName").toUpperCase()))
                        .build();
                contacts.add(contact);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
        return contacts;
    }

    /**
     * Convert a list of Contact objects into a CSV format.
     *
     * @param contacts the list of contacts.
     * @return a ByteArrayInputStream containing CSV data.
     */
    public static ByteArrayInputStream contactsToCSV(List<Contact> contacts) {
        final CSVFormat format = CSVFormat.DEFAULT.withHeader(HEADERs);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            for (Contact contact : contacts) {
                List<String> data = List.of(
                        contact.getFirstName(),
                        contact.getLastName(),
                        contact.getEmail(),
                        contact.getPhoneNumber(),
                        contact.getContactImage(),
                        contact.getAddress(),
                        contact.getGroupName().toString()
                );
                csvPrinter.printRecord(data);
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data to CSV file: " + e.getMessage());
        }
    }
}
