package edu.school21.restful.services;

import edu.school21.restful.model.Role;
import edu.school21.restful.model.User;
import edu.school21.restful.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User entity) {
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
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
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return userRepository.save(entity);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    @Override
    public boolean existsByIdAndRole(Long id, Role role) {
        return userRepository.existsByIdAndRole(id, role);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public List<User> findAllStudentsByCourseId(Long courseId, Pageable pageable) {
        return userRepository.findByCoursesAsStudent_Id(courseId, pageable);
    }

    @Override
    public List<User> findAllTeachersByCourseId(Long courseId, Pageable pageable) {
        return userRepository.findByCoursesAsTeacher_Id(courseId, pageable);
    }

    @Override
    public List<User> findAllStudentsByCourseId(Long courseId) {
        return userRepository.findByCoursesAsStudent_Id(courseId, Pageable.unpaged());
    }

    @Override
    public List<User> findAllTeachersByCourseId(Long courseId) {
        return userRepository.findByCoursesAsTeacher_Id(courseId, Pageable.unpaged());
    }
}
