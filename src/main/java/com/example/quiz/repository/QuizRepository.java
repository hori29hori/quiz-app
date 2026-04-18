package com.example.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.quiz.entity.Quiz;

/** Quizテーブル:Repository **/
@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    @Query(value = "SELECT id FROM quiz ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Integer findRandomId();
}
