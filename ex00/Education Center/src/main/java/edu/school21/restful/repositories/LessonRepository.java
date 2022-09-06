package edu.school21.restful.repositories;

import edu.school21.restful.model.Lesson;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends PagingAndSortingRepository<Lesson, Long> {
    List<Lesson> findByCourseId(Long courseId, Pageable pageable);
}
