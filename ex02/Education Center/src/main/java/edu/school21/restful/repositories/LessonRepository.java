package edu.school21.restful.repositories;

import edu.school21.restful.model.Lesson;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

//@RepositoryRestResource(exported = false)
public interface LessonRepository extends PagingAndSortingRepository<Lesson, Long> {
    List<Lesson> findByCourseId(Long courseId, Pageable pageable);
    Optional<Lesson> findByIdAndCourseId(Long id, Long courseId);
}
