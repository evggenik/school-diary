INSERT INTO public.person (birth_date, created_at, email, gender, password, role, username)
VALUES ('1985-05-15', NOW(), 'teacher@example.com', 'male', 'securepassword', 'teacher', 'teacher_username');

INSERT INTO public.teacher (person_id, first_name, last_name)
VALUES (1, 'John', 'Doe');