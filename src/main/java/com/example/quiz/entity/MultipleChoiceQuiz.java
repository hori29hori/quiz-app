package com.example.quiz.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

/**
 * 4択問題のエンティティ（DBの multiple_choice_quiz と対応）
 */
@Entity
@Table(name = "multiple_choice_quiz")
@Data
public class MultipleChoiceQuiz {

    /** 主キー */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 問題文 */
    private String question;

    /** 選択肢1 */
    private String choice1;

    /** 選択肢2 */
    private String choice2;

    /** 選択肢3 */
    private String choice3;

    /** 選択肢4 */
    private String choice4;

    /** 正解番号（1〜4） */
    private Integer answer;
}
