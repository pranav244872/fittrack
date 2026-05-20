package com.pranav244872.fitness_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pranav244872.fitness_tracker.model.Category;
import com.pranav244872.fitness_tracker.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUser(User user);
    Optional<Category> findByIdAndUser(Long id, User user);
}
