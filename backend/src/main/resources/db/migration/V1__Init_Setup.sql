CREATE TABLE public.person(
    id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(25) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    birth_date DATE NOT NULL,
    gender VARCHAR(25) NOT NULL CHECK (gender IN ('MALE', 'FEMALE')),
    role VARCHAR(25) NOT NULL CHECK (role IN ('ADMINISTRATOR', 'PERSON', 'TEACHER', 'STUDENT', 'PARENT')),
    avatar VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE public.teacher (
     id BIGSERIAL PRIMARY KEY,
     person_id BIGINT,
     first_name VARCHAR(50) NOT NULL,
     last_name VARCHAR(50) NOT NULL,
     FOREIGN KEY (person_id) REFERENCES person(id) ON DELETE CASCADE
);

WITH inserted_person AS (
    INSERT INTO public.person (birth_date, created_at, email, gender, password, role, user_name)
        VALUES ('1985-05-15', NOW(), 'teacher@example.com', 'MALE', 'password', 'ADMINISTRATOR', 'admin_username')
        RETURNING id
)
INSERT INTO public.teacher (person_id, first_name, last_name)
SELECT id, 'John', 'Smith' FROM inserted_person;