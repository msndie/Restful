package edu.school21.restful.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.school21.restful.utils.DayOfWeekDeserializer;
import edu.school21.restful.utils.View;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonView(View.LessonsView.class)
    private Long id;

    @Column(name = "start_time", nullable = false)
    @JsonView(View.LessonsView.class)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    @JsonView(View.LessonsView.class)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
//    @Type(type = "org.hibernate.type.TextType")
    @JsonDeserialize(using = DayOfWeekDeserializer.class)
    @JsonView(View.LessonsView.class)
    private DayOfWeek dayOfWeek;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

//    @ToString.Exclude
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "course_id", nullable = false)
//    private Course course;

//    @Transient
//    @JsonView(View.LessonsView.class)
//    private Long teacherId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;
}