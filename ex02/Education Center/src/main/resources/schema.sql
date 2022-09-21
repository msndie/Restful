CREATE SCHEMA IF NOT EXISTS ex02_testing;

CREATE SEQUENCE IF NOT EXISTS ex02_testing.users_id_seq AS BIGINT START WITH 8;
CREATE SEQUENCE IF NOT EXISTS ex02_testing.courses_id_seq AS BIGINT START WITH 4;
CREATE SEQUENCE IF NOT EXISTS ex02_testing.lessons_id_seq AS BIGINT START WITH 4;

CREATE TABLE IF NOT EXISTS ex02_testing.users (
    id         BIGINT DEFAULT nextval('ex02_testing.users_id_seq') PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name  TEXT NOT NULL,
    role       TEXT NOT NULL,
    login      TEXT NOT NULL UNIQUE,
    password   TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS ex02_testing.courses (
    id          BIGINT DEFAULT nextval('ex02_testing.courses_id_seq') PRIMARY KEY,
    start_date  DATE NOT NULL,
    end_date    DATE NOT NULL,
    name        TEXT NOT NULL,
    description TEXT NOT NULL,
    state       TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS ex02_testing.lessons (
    id          BIGINT DEFAULT nextval('ex02_testing.lessons_id_seq') PRIMARY KEY,
    teacher_id  BIGINT NOT NULL,
    course_id   BIGINT NOT NULL,
    start_time  TIME NOT NULL,
    end_time    TIME NOT NULL,
    day_of_week TEXT NOT NULL,
    CONSTRAINT users FOREIGN KEY ( teacher_id ) REFERENCES ex02_testing.users ( id ) ON DELETE CASCADE,
    CONSTRAINT courses FOREIGN KEY ( course_id ) REFERENCES ex02_testing.courses ( id )
);

CREATE TABLE IF NOT EXISTS ex02_testing.course_teachers (
    teacher_id BIGINT NOT NULL,
    course_id  BIGINT NOT NULL,
    CONSTRAINT PK_1 PRIMARY KEY (teacher_id, course_id),
    CONSTRAINT users FOREIGN KEY ( teacher_id ) REFERENCES ex02_testing.users ( id ) ON DELETE CASCADE,
    CONSTRAINT courses FOREIGN KEY ( course_id ) REFERENCES ex02_testing.courses ( id ) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ex02_testing.course_students (
    student_id BIGINT NOT NULL,
    course_id  BIGINT NOT NULL,
    CONSTRAINT PK_2 PRIMARY KEY (student_id, course_id),
    CONSTRAINT users FOREIGN KEY ( student_id ) REFERENCES ex02_testing.users ( id ) ON DELETE CASCADE,
    CONSTRAINT courses FOREIGN KEY ( course_id ) REFERENCES ex02_testing.courses ( id ) ON DELETE CASCADE
);

ALTER SEQUENCE ex02_testing.users_id_seq OWNED BY ex02_testing.users.id;
ALTER SEQUENCE ex02_testing.courses_id_seq OWNED BY ex02_testing.courses.id;
ALTER SEQUENCE ex02_testing.lessons_id_seq OWNED BY ex02_testing.lessons.id;