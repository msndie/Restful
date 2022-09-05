package edu.school21.restful.services;

import java.util.List;

public interface Service<T> {
    T save(T entity);
    void delete(T entity);
    List<T> findAll();
    T update(T entity);
    boolean existsById(Long id);
    void deleteById(Long id);
}
