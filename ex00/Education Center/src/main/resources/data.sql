INSERT INTO ex00.users (id, first_name, last_name, role, login, password)
VALUES
       (1, 'Teacher', 'One', 'TEACHER', 'teacher1', 'qwe'),
       (2, 'Teacher', 'Two', 'TEACHER', 'teacher2', 'qwe'),
       (3, 'Teacher', 'Three', 'TEACHER', 'teacher3', 'qwe'),
       (4, 'Student', 'One', 'STUDENT', 'student1', 'qwe'),
       (5, 'Student', 'Two', 'STUDENT', 'student2', 'qwe'),
       (6, 'Student', 'Three', 'STUDENT', 'student3', 'qwe')
ON CONFLICT DO NOTHING;

INSERT INTO ex00.courses (id, start_date, end_date, name, description)
VALUES
       (1, '2022-10-01', '2022-10-10', 'First course', 'First test course'),
       (2, '2022-10-11', '2022-10-20', 'Second course', 'Second test course'),
       (3, '2022-10-21', '2022-10-30', 'Third course', 'Third test course')
ON CONFLICT DO NOTHING;

INSERT INTO ex00.lessons (id, teacher_id, course_id, start_time, end_time, day_of_week)
VALUES
       (1, 1, 1, '10:00:00', '11:00:00', 'MONDAY'),
       (2, 2, 2, '10:00:00', '11:00:00', 'TUESDAY'),
       (3, 3, 3, '10:00:00', '11:00:00', 'WEDNESDAY')
ON CONFLICT DO NOTHING;

INSERT INTO ex00.course_students (student_id, course_id)
VALUES
       (4, 1),
       (5, 2),
       (6, 3)
ON CONFLICT DO NOTHING;

INSERT INTO ex00.course_teachers (teacher_id, course_id)
VALUES
       (1, 1),
       (2, 2),
       (3, 3)
ON CONFLICT DO NOTHING;
