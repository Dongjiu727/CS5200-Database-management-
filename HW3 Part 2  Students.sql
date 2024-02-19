-- HW3 Part2 Students
-- Create Schema for HW_03 Part2
DROP DATABASE IF EXISTS HW3_Students;
CREATE DATABASE IF NOT EXISTS HW3_Students;
USE HW3_Students;

-- Create tables in the HW3_Student database
CREATE TABLE HW3_Students.Person
    (
    idNumber INT AUTO_INCREMENT NOT NULL,
    firstName VARCHAR(255),
    lastName VARCHAR(255),
    CONSTRAINT pk_Person_idNumber PRIMARY KEY (idNumber)
    );

CREATE TABLE HW3_Students.Department
    (
    `name` VARCHAR(45) NOT NULL,
    CONSTRAINT pk_Department_name PRIMARY KEY (`name`)
    );

CREATE TABLE HW3_Students.Message
    (
    messageID INT AUTO_INCREMENT NOT NULL,
    sender INT NOT NULL,
    recipient INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    body VARCHAR(255) NOT NULL,
    CONSTRAINT pf_Message_messageID PRIMARY KEY (messageID),
    CONSTRAINT fk_Message_sender FOREIGN KEY (sender) REFERENCES Person(idNumber)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_Message_recipient FOREIGN KEY (recipient) REFERENCES Person(idNumber)
        ON UPDATE CASCADE ON DELETE CASCADE
	);

CREATE TABLE HW3_Students.Student
    (
    idNumber INT NOT NULL,
    enrollmentDate Date NOT NULL,
    CONSTRAINT pk_Student_idNumber PRIMARY KEY (idNumber),
    CONSTRAINT fk_Student_idNumber FOREIGN KEY (idNumber) REFERENCES Person(idNumber)
        ON UPDATE CASCADE ON DELETE CASCADE
    );

CREATE TABLE HW3_Students.Faculty
    (
    idNumber INT NOT NULL,
    department VARCHAR(45) NOT NULL,
    CONSTRAINT pk_Faculty_idNumber PRIMARY KEY (idNumber),
    CONSTRAINT fk_Faculty_idNumber FOREIGN KEY (idNumber) REFERENCES Person(idNumber)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_Faculty_department FOREIGN KEY (department) REFERENCES Department(name)
        ON UPDATE CASCADE ON DELETE CASCADE
    );

CREATE TABLE HW3_Students.Course
    (
    catalogNumber VARCHAR(255) NOT NULL,
    department VARCHAR(45) NOT NULL,
    description VARCHAR(255) NOT NULL,
    lecturer INT,
    
    CONSTRAINT pk_Course_catalogNumber PRIMARY KEY (catalogNumber),
    CONSTRAINT fk_Course_department FOREIGN KEY (department) REFERENCES Department(name)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_Faculty_lecturer FOREIGN KEY (lecturer) REFERENCES Faculty(idNumber),
    CONSTRAINT uq_Course_catalogNumber_department UNIQUE (catalogNumber,department)
    );

CREATE TABLE HW3_Students.Registration
    (
    studentID INT NOT NULL,
    course VARCHAR(255) NOT NULL,
    grade INT,
    CONSTRAINT pk_Registration_studentID_course PRIMARY KEY(studentID, course),
    CONSTRAINT fk_Registration_studentID FOREIGN KEY (studentID) REFERENCES Student(idNumber)
        ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT fk_Registration_course FOREIGN KEY (course) REFERENCES Course(catalogNumber)
        ON UPDATE CASCADE ON DELETE CASCADE
	);

USE HW3_Students;
-- Q1 
-- Insert student information into Person and Student tables
-- Insert the stuent information into Person table
INSERT INTO Person (idNumber, firstName, lastName) VALUES (2475343,'Abc','Chen');
-- Insert the stuent information into Student table
INSERT INTO Student (idNumber,enrollmentDate) VALUES (2475343,'2023-10-18');
-- Insert the department information into the Department table
INSERT IGNORE INTO Department (`name`) VALUES ('CS');
-- Insert the course information into Course table
INSERT IGNORE INTO Course (catalogNumber, department, description) VALUES ('CS5004','CS','Good');
-- Register this student for course CS5004
INSERT INTO Registration (studentID, course) VALUES (2475343,'CS5004');


-- Inset some data into the database for testing
-- Insert data into Person table
INSERT INTO HW3_Students.Person (idNumber, firstName, lastName) VALUES
(1, 'John', 'Doe'),
(2, 'Jane', 'Smith'),
(3, 'Alice', 'Johnson'),
(4, 'Bob', 'Wilson'),
(5, 'Joh', 'De'),
(8, 'Ja', 'Smi');

-- Insert data into Department table
INSERT INTO HW3_Students.Department (`name`) VALUES
('Mathematics'),
('English'),
('Biology');

-- Insert data into Message table
INSERT INTO HW3_Students.Message (sender, recipient, title, body)
VALUES
    (4, 2, 'Hello', 'Hi, how are you?'),
    (2, 4, 'Re: Hello', 'Hi , I am doing well.'),
    (3, 1, 'Important Notice', 'please remember to submit your assignment.'),
    (4, 3, 'Reminder', 'We have a meeting tomorrow.');
    
-- Insert data into Student table
INSERT INTO HW3_Students.Student (idNumber, enrollmentDate) VALUES
(1, '2022-01-15'),
(2, '2021-09-10');

-- Insert data into Faculty table
INSERT INTO HW3_Students.Faculty (idNumber, department) VALUES
(3, 'Mathematics'),
(4, 'English'),
(5, 'Biology'),
(8, 'CS');

-- Insert data into Course table
INSERT INTO HW3_Students.Course (catalogNumber, department, description, lecturer) VALUES
('CS5010', 'Mathematics', 'Introduction to Calculus', 3),
('CS101', 'CS', 'Programming Fundamentals', 4),
('CS501', 'Mathematics', 'Introduction to Biology', NULL),
('CS5011', 'Biology', 'Introduction to Biology', 4);

-- Insert data into Registration table
INSERT INTO HW3_Students.Registration (studentID, course,grade) VALUES
(1, 'CS101',59),
(1, 'CS5010',90),
(1, 'CS5011',80),
(2, 'CS101',NULL),
(2, 'CS5010',100);

-- Q2
SELECT p.idNumber, p.firstName AS 'First Name', p.lastName AS 'Last Name'
FROM Person AS p
JOIN Student AS s ON p.idNumber = s.idNumber
JOIN Registration AS r ON s.idNumber = r.studentID
JOIN Course AS c ON r.course = c.catalogNumber 
WHERE c.catalogNumber = 'CS5010';

-- Q3
SELECT DISTINCT studentID, AVG(grade) AS 'Average'
FROM Registration 
GROUP BY studentID;
-- Here, I select all students in the Registration table, including those with null grades.

-- Q4
SELECT p.idNumber, p.firstName, p.lastName, COUNT(m.messageID) AS 'numberOfMessages'
FROM Person AS p
JOIN Faculty AS f ON p.idNumber = f.idNumber
LEFT JOIN Message AS m ON p.idNumber = m.sender 
WHERE f.department = 'English'
GROUP BY p.idNumber,p.firstName, p.lastName;

-- Q5
SELECT c.catalogNumber, COUNT(r.studentID) AS 'numberOfStudents'
FROM Course AS c
LEFT JOIN Registration AS r ON c.catalogNumber = r.course
GROUP BY c.catalogNumber 
ORDER BY COUNT(r.studentID) DESC
LIMIT 5;

-- Q6
SELECT r.studentID, p.firstName, p.lastName, c.catalogNumber
FROM Person AS p
JOIN Student AS s ON p.idNumber = s.idNumber
JOIN Registration AS r ON s.idNumber = r.studentID
LEFT JOIN Course AS c ON c.catalogNumber = r.course
WHERE r.grade IS NULL;

-- Q7
SELECT f.idNumber, p.firstName, p.lastName, 
       IFNULL(COUNT(DISTINCT c.catalogNumber), 0) AS 'numberOfCourse'
FROM Person AS p
JOIN Faculty AS f ON p.idNumber = f.idNumber
LEFT JOIN Course AS c ON f.idNumber = c.lecturer
GROUP BY f.idNumber, p.firstName, p.lastName;