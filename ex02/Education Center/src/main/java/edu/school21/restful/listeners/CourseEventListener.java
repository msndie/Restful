package edu.school21.restful.listeners;

import edu.school21.restful.exceptions.BadRequestException;
import edu.school21.restful.models.Course;
import edu.school21.restful.models.CourseState;
import edu.school21.restful.models.Role;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

@Component
public class CourseEventListener extends AbstractRepositoryEventListener<Course> {
    @Override
    protected void onBeforeCreate(Course entity) {
        validation(entity);
        entity.setState(CourseState.DRAFT);
    }

    @Override
    protected void onBeforeSave(Course entity) {
        validation(entity);
    }

    private void validation(Course course) {
        if (course.getTeachers() != null) {
            course.getTeachers().forEach(user -> {
                if (user.getRole() != Role.TEACHER) {
                    throw new BadRequestException();
                }
            });
        }
        if (course.getStudents() != null) {
            course.getStudents().forEach(user -> {
                if (user.getRole() != Role.STUDENT) {
                    throw new BadRequestException();
                }
            });
        }
        if (course.getName() == null || course.getDescription() == null
                || course.getEndDate() == null || course.getStartDate() == null
                || course.getName().trim().isEmpty() || course.getDescription().trim().isEmpty()
                || course.getEndDate().isBefore(course.getStartDate())) {
            throw new BadRequestException();
        }
    }
}
