package edu.school21.ex02_testing.repositories;

import edu.school21.ex02_testing.models.Lesson;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface LessonRepository extends PagingAndSortingRepository<Lesson, Long> {
}