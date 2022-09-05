package edu.school21.restful.utils;

import edu.school21.restful.dto.LessonDtoIn;
import edu.school21.restful.dto.LessonDtoOut;
import edu.school21.restful.model.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper {

    private UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public LessonDtoOut toDto(Lesson lesson) {
        return new LessonDtoOut(lesson.getId(),
                lesson.getStartTime(),
                lesson.getEndTime(),
                lesson.getDayOfWeek(),
                userMapper.toDto(lesson.getTeacher()));
    }

    public Lesson toDomain(LessonDtoIn lessonDtoIn) {
        Lesson lesson = new Lesson();
        lesson.setId(lessonDtoIn.getId());
        lesson.setStartTime(lessonDtoIn.getStartTime());
        lesson.setEndTime(lessonDtoIn.getEndTime());
        lesson.setDayOfWeek(lessonDtoIn.getDayOfWeek());
//        lesson.setTeacherId(lessonDtoIn.getTeacherId());
        return lesson;
    }
}
