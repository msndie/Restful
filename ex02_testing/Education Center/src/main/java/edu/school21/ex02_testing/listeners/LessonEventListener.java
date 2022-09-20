package edu.school21.ex02_testing.listeners;

import edu.school21.ex02_testing.exceptions.BadRequestException;
import edu.school21.ex02_testing.models.Lesson;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

@Component
public class LessonEventListener extends AbstractRepositoryEventListener<Lesson> {
    @Override
    protected void onBeforeCreate(Lesson entity) {
        if (!isLessonValid(entity)) {
            throw new BadRequestException();
        }
    }

    @Override
    protected void onBeforeSave(Lesson entity) {
        if (!isLessonValid(entity)) {
            throw new BadRequestException();
        }
    }

    private boolean isLessonValid(Lesson entity) {
        return entity.getCourse() != null && entity.getEndTime() != null
                && entity.getStartTime() != null && entity.getTeacher() != null
                && entity.getDayOfWeek() != null && !entity.getEndTime().isBefore(entity.getStartTime())
                && entity.getCourse().getTeachers().contains(entity.getTeacher());
    }
}
