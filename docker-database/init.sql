-- Create the User table first
CREATE TABLE IF NOT EXISTS "User" (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create the Profile table with a foreign key reference to User
CREATE TABLE IF NOT EXISTS Profile (
    profile_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES "User"(user_id)
        ON DELETE CASCADE
);

-- Optional: Add some sample data to the User table
INSERT INTO "User" (username, email, password)
VALUES 
    ('User', 'user@phinmaed.com', 'password123'),

-- Optional: Add some sample data to the Profile table, linking to the User table
INSERT INTO Profile (user_id, first_name, last_name, middle_name)
VALUES 
    (1, 'User First Name', 'User Last Name', 'User Middle Name'),
    