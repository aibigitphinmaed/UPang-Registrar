-- Create the User table first
CREATE TABLE IF NOT EXISTS "User" (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create the PersonalInformation table
CREATE TABLE IF NOT EXISTS "PersonalInformation" (
    profile_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    extension_name VARCHAR(100),
    gender VARCHAR(50) NOT NULL,
    citizenship VARCHAR(100) NOT NULL,
    religion VARCHAR(100) NOT NULL,
    civil_status VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    contact_number VARCHAR(15),
    birth_date DATE NOT NULL,
    father_name VARCHAR(255) NOT NULL,
    mother_name VARCHAR(255) NOT NULL,
    spouse_name VARCHAR(255),
    contact_person_number VARCHAR(15) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES "User"(user_id) ON DELETE CASCADE
);

-- Create the LocationInformation table
CREATE TABLE IF NOT EXISTS "LocationInformation" (
    location_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    house_number VARCHAR(10),
    street VARCHAR(255) NOT NULL,
    zone VARCHAR(50),
    barangay VARCHAR(100) NOT NULL,
    city_municipality VARCHAR(100) NOT NULL,
    province VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    postal_code VARCHAR(10) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES "User"(user_id) ON DELETE CASCADE
);

-- Insert default user
INSERT INTO "User" (username, password, role)
VALUES ('user', 'user', 'student');

-- Insert into PersonalInformation with required fields
INSERT INTO "PersonalInformation" (
    user_id, first_name, last_name, gender, citizenship, religion, civil_status, email, birth_date, father_name, mother_name, contact_person_number
) VALUES (
    1, 'User First Name', 'User Last Name', 'Male', 'Filipino', 'Catholic', 'Single', 'user@phinmaed.com', '2000-01-01', 'Father Name', 'Mother Name', '09123456789'
);

-- Insert into LocationInformation with required fields
INSERT INTO "LocationInformation" (
    user_id, street, barangay, city_municipality, province, country, postal_code
) VALUES (
    1, 'Main Street', 'Barangay 1', 'City XYZ', 'Province ABC', 'Philippines', '1000'
);

