package edu.school21.restful.services;

import edu.school21.restful.model.Role;
import edu.school21.restful.model.User;

import java.util.Optional;

public interface UserService extends Service<User> {
    boolean existsByIdAndRole(Long id, Role role);
    Optional<User> findById(Long id);
}
