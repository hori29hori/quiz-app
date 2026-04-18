package com.example.quiz.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.quiz.entity.MultipleChoiceQuiz;

public interface MultipleChoiceQuizRepository
        extends CrudRepository<MultipleChoiceQuiz, Integer> {
}
