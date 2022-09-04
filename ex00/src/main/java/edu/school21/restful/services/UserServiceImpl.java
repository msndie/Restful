package edu.school21.restful.services;

import edu.school21.restful.model.User;
import edu.school21.restful.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public void delete(User entity) {
        userRepository.delete(entity);
    }

    @Override
    public List<User> findAll() {
        List<User> users = new LinkedList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @Override
    public User update(User entity) {
        return userRepository.save(entity);
    }
}
