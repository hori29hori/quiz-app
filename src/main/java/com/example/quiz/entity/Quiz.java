package com.example.quiz.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * quizテーブル用：Entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quiz")

public class Quiz {

    /** ID **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** クイズの内容 **/
    private String question;

    /** クイズの解答 **/
    private Boolean answer;

    /** 作成者 **/
    private String author;
}
