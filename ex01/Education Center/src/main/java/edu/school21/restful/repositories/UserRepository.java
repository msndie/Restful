package edu.school21.restful.repositories;

import edu.school21.restful.model.Role;
import edu.school21.restful.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    boolean existsByLogin(String login);
    boolean existsByIdAndRole(Long id, Role role);
    List<User> findByCoursesAsStudent_Id(Long id, Pageable pageable);
    List<User> findByCoursesAsTeacher_Id(Long id, Pageable pageable);
    Optional<User> findByLogin(String login);

}
