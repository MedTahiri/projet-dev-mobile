# Django API Documentation

This API allows you to interact with the `Student` and `Note` models. Below are the available endpoints:

## 1. /api/profiles/

### **GET**: Retrieve all student profiles.

- **Description**: Get a list of all students in the database.
- **url**: `https://medtahiri.pythonanywhere.com/api/profiles/`
---

## 2. /api/profile/

### **GET**: Retrieve a specific student profile by ID.

- **Description**: Fetch a student profile by their `id`.
- **Parameters**: 
  - `id` (required): The `id` of the student to retrieve.
  - `id` from 32 to 60
- **url**: `https://medtahiri.pythonanywhere.com/api/profile/?id=45`

### **POST**: Create a new student.

- **Description**: Create a new student record by providing student details in the request body.

### **PUT**: Update an existing student profile.

- **Description**: Update a student profile using the `id` provided in the request body.

### **DELETE**: Delete a specific student.

- **Description**: Delete a student profile based on the `id`.

---

## 3. /api/notes/

### **GET**: Retrieve all notes for a specific student.

- **Description**: Fetch all notes for a student based on their `id`.
- **Parameters**:
  - `id` (required): The `id` of the student whose notes you want to retrieve.
  - `id` from 32 to 60
- **url**: `https://medtahiri.pythonanywhere.com/api/notes/?id=45`

---

## 4. /api/note/

### **POST**: Create a new note for a student.

- **Description**: Add a new note for a student by providing the note details in the request body.

### **PUT**: Update an existing note.

- **Description**: Update an existing note based on the `id` provided in the request body.

### **DELETE**: Delete a specific note.

- **Description**: Delete a note for a student by providing the `id` of the note.

---
