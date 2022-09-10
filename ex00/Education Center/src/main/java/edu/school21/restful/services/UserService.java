package edu.school21.restful.services;

import edu.school21.restful.model.Role;
import edu.school21.restful.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService extends Service<User> {
    boolean existsByLogin(String login);
    boolean existsByIdAndRole(Long id, Role role);
    Optional<User> findById(Long id);
    List<User> findAllStudentsByCourseId(Long courseId, Pageable pageable);
    List<User> findAllTeachersByCourseId(Long courseId, Pageable pageable);
    List<User> findAllStudentsByCourseId(Long courseId);
    List<User> findAllTeachersByCourseId(Long courseId);
}
