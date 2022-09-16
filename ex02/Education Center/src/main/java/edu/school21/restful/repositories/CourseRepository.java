package edu.school21.restful.repositories;

import edu.school21.restful.model.Course;
import edu.school21.restful.model.dto.CourseDto;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//@RepositoryRestResource(excerptProjection = CourseDto.class)
public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {

}
