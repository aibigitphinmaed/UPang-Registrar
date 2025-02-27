-- Create the User table first
CREATE TABLE IF NOT EXISTS "user" (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    salt VARCHAR(64) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create the PersonalInformation table
CREATE TABLE IF NOT EXISTS "personal_information" (
    id SERIAL PRIMARY KEY,
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
    FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE
);

-- Create the LocationInformation table
CREATE TABLE IF NOT EXISTS "location_information" (
    id SERIAL PRIMARY KEY,
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
    FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "notification" (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "image_records" (
	id SERIAL PRIMARY KEY,
	file_name VARCHAR(255) NOT NULL,
	file_type VARCHAR(20) NOT NULL,
	user_id INT NOT NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE
);

ALTER TABLE "user"
ADD COLUMN image_id INT NULL;

CREATE TABLE IF NOT EXISTS "appointment" (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL,
    staff_id INT,
    appointment_type VARCHAR(255) NOT NULL,
    document_type VARCHAR(255),
    reason TEXT,
    requested_date TIMESTAMP NOT NULL,
    scheduled_date TIMESTAMP,
    status VARCHAR(50) NOT NULL DEFAULT 'pending',
    notified_at TIMESTAMP,
    is_urgent BOOLEAN DEFAULT FALSE,
    remarks TEXT,
    cancellation_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES "user"(id) ON DELETE CASCADE,
    FOREIGN KEY (staff_id) REFERENCES "user"(id) ON DELETE SET NULL
);

-- Create the Function for Automatically Updating `updated_at`
CREATE OR REPLACE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create the Trigger to Call the Function Before Update
CREATE TRIGGER set_timestamp
BEFORE UPDATE ON "appointment"
FOR EACH ROW
EXECUTE FUNCTION update_modified_column();
