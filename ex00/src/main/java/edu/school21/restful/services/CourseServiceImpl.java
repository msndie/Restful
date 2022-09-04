package edu.school21.restful.services;

import edu.school21.restful.model.Course;
import edu.school21.restful.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course save(Course entity) {
        return courseRepository.save(entity);
    }

    @Override
    public void delete(Course entity) {
        courseRepository.delete(entity);
    }

    @Override
    public List<Course> findAll() {
        List<Course> courses = new LinkedList<>();
        courseRepository.findAll().forEach(courses::add);
        return courses;
    }

    @Override
    public Course update(Course entity) {
        return courseRepository.save(entity);
    }

    @Override
    public Optional<Course> getById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return courseRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        courseRepository.deleteById(id);
    }
}
