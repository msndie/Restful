package edu.school21.restful.repositories;

import edu.school21.restful.model.Course;
import edu.school21.restful.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {
}
