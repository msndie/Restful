package edu.school21.restful.repositories;

import edu.school21.restful.model.Role;
import edu.school21.restful.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByIdAndRole(Long id, Role role);
}
