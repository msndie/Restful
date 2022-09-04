package edu.school21.restful.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private Role role;

    @Column(name = "login", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String login;

    @Column(name = "password", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String password;

    @ManyToMany
    @JoinTable(name = "course_students",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> coursesAsStudent = new LinkedHashSet<>();

    @OneToMany(mappedBy = "teacher")
    private Set<Lesson> lessons = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "course_teachers",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> coursesAsTeacher = new LinkedHashSet<>();
}