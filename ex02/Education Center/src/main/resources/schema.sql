CREATE SCHEMA IF NOT EXISTS ex02;

CREATE SEQUENCE IF NOT EXISTS ex02.users_id_seq AS BIGINT START WITH 8;
CREATE SEQUENCE IF NOT EXISTS ex02.courses_id_seq AS BIGINT START WITH 4;
CREATE SEQUENCE IF NOT EXISTS ex02.lessons_id_seq AS BIGINT START WITH 4;

CREATE TABLE IF NOT EXISTS ex02.users (
    id         BIGINT DEFAULT nextval('ex02.users_id_seq') PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name  TEXT NOT NULL,
    role       TEXT NOT NULL,
    login      TEXT NOT NULL UNIQUE,
    password   TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS ex02.courses (
    id          BIGINT DEFAULT nextval('ex02.courses_id_seq') PRIMARY KEY,
    start_date  DATE NOT NULL,
    end_date    DATE NOT NULL,
    name        TEXT NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS ex02.lessons (
    id          BIGINT DEFAULT nextval('ex02.lessons_id_seq') PRIMARY KEY,
    teacher_id  BIGINT NOT NULL,
    course_id   BIGINT NOT NULL,
    start_time  TIME NOT NULL,
    end_time    TIME NOT NULL,
    day_of_week TEXT NOT NULL,
    CONSTRAINT users FOREIGN KEY ( teacher_id ) REFERENCES ex02.users ( id ),
    CONSTRAINT courses FOREIGN KEY ( course_id ) REFERENCES ex02.courses ( id )
);

CREATE TABLE IF NOT EXISTS ex02.course_teachers (
    teacher_id BIGINT NOT NULL,
    course_id  BIGINT NOT NULL,
    CONSTRAINT PK_1 PRIMARY KEY (teacher_id, course_id),
    CONSTRAINT users FOREIGN KEY ( teacher_id ) REFERENCES ex02.users ( id ),
    CONSTRAINT courses FOREIGN KEY ( course_id ) REFERENCES ex02.courses ( id )
);

CREATE TABLE IF NOT EXISTS ex02.course_students (
    student_id BIGINT NOT NULL,
    course_id  BIGINT NOT NULL,
    CONSTRAINT PK_2 PRIMARY KEY (student_id, course_id),
    CONSTRAINT users FOREIGN KEY ( student_id ) REFERENCES ex02.users ( id ),
    CONSTRAINT courses FOREIGN KEY ( course_id ) REFERENCES ex02.courses ( id )
);

ALTER SEQUENCE ex02.users_id_seq OWNED BY ex02.users.id;
ALTER SEQUENCE ex02.courses_id_seq OWNED BY ex02.courses.id;
ALTER SEQUENCE ex02.lessons_id_seq OWNED BY ex02.lessons.id;