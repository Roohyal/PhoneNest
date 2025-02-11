# PhoneNest 
## PhoneBook Management Application

## Overview

PhoneNest is a robust contact management system built using Spring Boot on the backend. This application allows users to perform essential CRUD operations on contacts, including creating, updating, deleting, searching, and bulk deletion. In addition, the project supports CSV file import/export functionality so that users can easily manage large contact lists. The backend is designed to work seamlessly with an Angular frontend (if used), and it leverages modern frameworks and tools to provide a clean, responsive, and scalable solution.

## Features

- **Capture a Contact:** Create new contacts with details such as first name, last name, email, phone number, contact image, physical address, and contact group (e.g., Family, Friends, Work).
- **List All Contacts:** Retrieve and display all contacts sorted alphabetically.
- **View a Single Contact:** Retrieve individual contact details using a unique identifier.
- **Edit and Update a Contact:** Update existing contacts with inline validation.
- **Delete a Contact:** Permanently remove a contact by its ID.
- **Bulk Deletion:** Delete multiple contacts at once using their IDs.
- **Search Contacts:** Search contacts dynamically by first name, last name, email, or phone number.
- **Import/Export Contacts:**
    - **Import:** Upload a CSV file containing contact details. The CSV file must adhere to a specific format.
    - **Export:** Download the current contact list as a CSV file.
- **Download CSV Template (Optional):** Users can download a CSV template to guide them in preparing their import files.

## Tech Stack

- **Backend:**
    - Java 17
    - Spring Boot 3.4.2
    - Spring Data JPA
    - Spring Boot Starter Web
    - Spring Boot Starter Validation
    - MySQL  for database persistence
    - Lombok for reducing boilerplate code
    - Apache Commons CSV for CSV parsing and generation
    - springdoc-openapi & Swagger UI for API documentation and testing


## Getting Started

### Prerequisites

- **Java 17** or later
- **Maven** for dependency management and building the project
- **MySQL Database**  configured with proper credentials


### Setup Instructions

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/yourusername/phonenest.git
   cd phonenest
2. **Configure the Database:**

   Update the `application.properties` (or `application.yml`) file located in `src/main/resources` with your database configuration:

`spring.datasource.url=jdbc:mysql://localhost:3306/phonenest_db
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update`



3. **Build the Project:**
   Use Maven to clean and build the project:
   `mvn clean install`


4. **Run the Application:** Use Maven to clean and build the project:
   `mvn spring-boot:run`


5.**Access API Documentation:** Open your browser and navigate to http://localhost:8080/swagger-ui/index.html to view and test the API endpoints interactively.

# API Endpoints
 ## PhoneBook Management Endpoints

### Create Contact

* **Endpoint:** `POST /api/contacts/add-contact`
* **Description:** Creates a new contact.
* **Request Body:** JSON payload containing:
* firstName, lastName, email, phoneNumber, contactImage, address, groupName
* **Response:** ContactResponse with confirmation message and response code.


 ### Update Contact

* **Endpoint:** `PUT /api/contacts/update-contact`
* **Description:** Updates an existing contact.
* **Request Parameters:** id (contact ID)
* **Request Body:** JSON payload with updated contact details.
* **Response:** ContactResponse with updated contact information.


### Delete Contact

* **Endpoint:** `DELETE /api/contacts/delete-contact`
* **Description:** Deletes a contact by its ID.
* **Request Parameter:** contactId
* **Response:** Confirmation message indicating deletion success.

 ### Bulk Delete Contacts

* **Endpoint:** `DELETE /api/contacts/bulk-delete`
* **Description:** Deletes multiple contacts at once.
* **Request Body:** JSON array of contact IDs, seperated using a comma.
* **Response:** Confirmation message.

 ### Search Contacts

* **Endpoint:** `GET /api/contacts/search`
* **Description:** Searches contacts by first name, last name, email, or phone number.
* **Query Parameter:** query, could be the firstname , lastname or email
* **Response:** List of ContactReportDto objects matching the query.

 ### Get All Contacts

* **Endpoint:** `GET /api/contacts/get-all-contacts`
* **Description:** Retrieves all contacts sorted alphabetically.
* **Response:** List of ContactReportDto objects.

###  Get Contacts By Group

* **Endpoint:** `GET /api/contacts/get-contact-by-group`
* **Description:** Retrieves contacts filtered by group.
* **Query Parameter:** groupName (e.g., FRIENDS, FAMILY, WORK)
* **Response:** List of ContactReportDto objects for the specified group.

## CSV Import/Export Endpoints

### Import Contacts

* **Endpoint:** `POST /api/contacts/import`
* **Consumes:** multipart/form-data
* **Description:** Imports contacts from a CSV file.
* The CSV file must have the following header:
* `firstName,lastName,email,phoneNumber,contactImage,address,groupName`
* **Request Parameter:** file (CSV file upload)
* **Response:** Confirmation message indicating successful import.

### Export Contacts

* **Endpoint:** `GET /api/contacts/export`
* **Description:** Exports all contacts to a CSV file.
* **Response:** A CSV file as an attachment.

## Testing the Application
### Using Swagger UI
1. **Run the Application:**
Start the application and navigate to http://localhost:8080/swagger-ui/index.html.

2. **Test Endpoints:**
Use the interactive Swagger UI to test all endpoints. For example, upload a CSV file through the `/api/contacts/import` endpoint by selecting a file using the file input field provided by Swagger.

### Using Postman
1. **Create a New Request:**
For file uploads, select POST and set the URL to http://localhost:8080/api/contacts/import.

2. **Set Up the Request:**
* Choose the Body tab and select form-data.
* Add a key named file, change its type to File, and attach your CSV file.


3. **Send the Request:**
Verify the response for successful import.

## Tools and Libraries

* **SpringBoot:** Provides the framework for building RESTful APIs.
* **Spring Data JPA:** Simplifies data access and persistence.
* **MySQL:** Relational database for contact storage.
* **Lombok:** Reduces boilerplate code through annotations.
* **Apache Commons CSV:** Facilitates CSV file parsing and generation.
* **springdoc-openapi & Swagger UI:** Generates interactive API documentation and testing interfaces.

Contact
For inquiries or support, please contact `royalihunma@gmail.com`.
