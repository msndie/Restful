package edu.school21.restful.services;

import edu.school21.restful.model.Lesson;
import edu.school21.restful.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

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
}
