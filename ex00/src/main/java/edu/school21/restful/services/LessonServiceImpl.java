package edu.school21.restful.services;

import edu.school21.restful.model.Lesson;
import edu.school21.restful.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Transactional
@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public Lesson save(Lesson entity) {
        return lessonRepository.save(entity);
    }

    @Override
    public void delete(Lesson entity) {
        lessonRepository.delete(entity);
    }

    @Override
    public List<Lesson> findAll() {
        List<Lesson> lessons = new LinkedList<>();
        lessonRepository.findAll().forEach(lessons::add);
        return lessons;
    }

    @Override
    public Lesson update(Lesson entity) {
        return lessonRepository.save(entity);
    }

    @Override
    public boolean existsById(Long id) {
        return lessonRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        lessonRepository.deleteById(id);
    }

    @Override
    public Page<Lesson> findAll(Pageable pageable) {
        return lessonRepository.findAll(pageable);
    }

    @Override
    public List<Lesson> findAllByCourseId(Long courseId, Pageable pageable) {
        return lessonRepository.findByCourseId(courseId, pageable);
    }
}
