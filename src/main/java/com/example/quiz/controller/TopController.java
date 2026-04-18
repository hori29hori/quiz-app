package com.example.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.quiz.form.QuizForm;
import com.example.quiz.service.QuizService;

@Controller
public class TopController {

    @Autowired
    QuizService service;

    @GetMapping("/")
    public String top(Model model) {

        QuizForm quizForm = new QuizForm();
        quizForm.setAnswer(true);
        quizForm.setNewQuiz(true);

        model.addAttribute("quizForm", quizForm);
        model.addAttribute("list", service.selectAll());
        model.addAttribute("title", "登録用フォーム");

        return "crud";
    }
}

