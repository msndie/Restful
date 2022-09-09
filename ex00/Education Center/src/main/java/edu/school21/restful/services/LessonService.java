package edu.school21.restful.services;

import edu.school21.restful.model.Lesson;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LessonService extends Service<Lesson> {
    List<Lesson> findAllByCourseId(Long courseId, Pageable pageable);
    Optional<Lesson> findById(Long id);
    Optional<Lesson> findByIdAndCourseId(Long id, Long courseId);
}
