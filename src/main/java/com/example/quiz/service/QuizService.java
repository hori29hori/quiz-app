package com.example.quiz.service;

import java.util.List;
import java.util.Optional;

import com.example.quiz.entity.Quiz;

	
	/** Quizサービス処理：Service*/
	public interface  QuizService{
		/** クイズ情報を全件取得します*/
		Iterable<Quiz> selectAll();
		/** クイズ情報を、idキーに１件取得します*/
		Optional<Quiz> selectOneById(Integer id);
		/** クイズ情報をランダムで１件取得します*/
		Optional<Quiz> selectOneRandomQuiz();
		/** クイズの正解/不正解を判定します*/
		Boolean checkQuiz(Integer id, Boolean myAnswer);
		/** クイズを登録します*/
		void insertQuiz(Quiz quiz);
		/** クイズを更新します*/
		void updateQuiz(Quiz quiz);
		/** クイズを削除します*/
		void deleteQuizById(Integer id);
		/** クイズIDをランダムに指定数取得します */
		List<Integer> selectRandomIds(int count);
		/** 指定したIDのクイズを取得する。*/
		Quiz findById(Integer id);

	}

