-- ADMIN PASS 123QWEasd
INSERT INTO ex02.users (id, first_name, last_name, role, login, password)
VALUES
       (1, 'Teacher', 'One', 'TEACHER', 'teacher1', '$2a$12$sf6UB4Ia1WBmKQg9FYn7H.ZiJXMmgrvmXiD8Auxic.UMirWlzE5wi'),
       (2, 'Teacher', 'Two', 'TEACHER', 'teacher2', '$2a$12$sf6UB4Ia1WBmKQg9FYn7H.ZiJXMmgrvmXiD8Auxic.UMirWlzE5wi'),
       (3, 'Teacher', 'Three', 'TEACHER', 'teacher3', '$2a$12$sf6UB4Ia1WBmKQg9FYn7H.ZiJXMmgrvmXiD8Auxic.UMirWlzE5wi'),
       (4, 'Student', 'One', 'STUDENT', 'student1', '$2a$12$sf6UB4Ia1WBmKQg9FYn7H.ZiJXMmgrvmXiD8Auxic.UMirWlzE5wi'),
       (5, 'Student', 'Two', 'STUDENT', 'student2', '$2a$12$sf6UB4Ia1WBmKQg9FYn7H.ZiJXMmgrvmXiD8Auxic.UMirWlzE5wi'),
       (6, 'Student', 'Three', 'STUDENT', 'student3', '$2a$12$sf6UB4Ia1WBmKQg9FYn7H.ZiJXMmgrvmXiD8Auxic.UMirWlzE5wi'),
       (7, 'Admin', 'Adminskiy', 'ADMINISTRATOR', 'admin', '$2a$12$NZ5.MRdSch5OHENQwssQieieIjwI0ao7et0Ql0tVosLn26SedeBaW')
ON CONFLICT DO NOTHING;

INSERT INTO ex02.courses (id, start_date, end_date, name, description)
VALUES
       (1, '2022-10-01', '2022-10-10', 'First course', 'First test course'),
       (2, '2022-10-11', '2022-10-20', 'Second course', 'Second test course'),
       (3, '2022-10-21', '2022-10-30', 'Third course', 'Third test course')
ON CONFLICT DO NOTHING;

INSERT INTO ex02.lessons (id, teacher_id, course_id, start_time, end_time, day_of_week)
VALUES
       (1, 1, 1, '10:00:00', '11:00:00', 'MONDAY'),
       (2, 2, 2, '10:00:00', '11:00:00', 'TUESDAY'),
       (3, 3, 3, '10:00:00', '11:00:00', 'WEDNESDAY')
ON CONFLICT DO NOTHING;

INSERT INTO ex02.course_students (student_id, course_id)
VALUES
       (4, 1),
       (5, 2),
       (6, 3)
ON CONFLICT DO NOTHING;

INSERT INTO ex02.course_teachers (teacher_id, course_id)
VALUES
       (1, 1),
       (2, 2),
       (3, 3)
ON CONFLICT DO NOTHING;
