/**package com.example.quiz.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.quiz.entity.Quiz;
import com.example.quiz.service.QuizService;

@Controller
public class Play10Controller {

    private final QuizService quizService;

    public Play10Controller(QuizService quizService) {
        this.quizService = quizService;
    }
    @GetMapping("/quiz/play10/start")
    public String startPlay10(HttpSession session) {

        // 10問のIDをランダム取得
        List<Integer> ids = quizService.selectRandomIds(10);

        // セッションに保存
        session.setAttribute("play10_ids", ids);   // 10問のIDリスト
        session.setAttribute("play10_index", 0);   // 現在の問題番号（0から開始）
        session.setAttribute("play10_score", 0);   // 正解数

        // 1問目へ
        return "redirect:/quiz/play10/question";
    }
    @GetMapping("/quiz/play10/question")
    public String showQuestion(HttpSession session, Model model) {

        // セッションから情報を取り出す
        List<Integer> ids = (List<Integer>) session.getAttribute("play10_ids");
        Integer index = (Integer) session.getAttribute("play10_index");

        // 10問終わったら結果へ
        if (index >= ids.size()) {
            return "redirect:/quiz/play10/result";
        }

        // 今の問題IDを取得
        Integer quizId = ids.get(index);

        // 問題を取得（Service の findById を使う）
        Quiz quiz = quizService.findById(quizId);

        // HTML に渡す
        model.addAttribute("quiz", quiz);
        model.addAttribute("number", index + 1); // 第◯問の表示用

        return "play10"; // play10.html を表示
    }
    @PostMapping("/quiz/play10/answer")
    public String answer(
            @RequestParam("id") Integer id,
            @RequestParam("answer") Boolean myAnswer,
            HttpSession session) {

        // 正誤判定
        boolean isCorrect = quizService.checkQuiz(id, myAnswer);

        // スコア取得
        Integer score = (Integer) session.getAttribute("play10_score");
        if (isCorrect) {
            score++;
            session.setAttribute("play10_score", score);
        }

        // 次の問題へ進める
        Integer index = (Integer) session.getAttribute("play10_index");
        session.setAttribute("play10_index", index + 1);

        // 次の問題へ
        return "redirect:/quiz/play10/question";
    }
    @GetMapping("/quiz/play10/result")
    public String showResult(HttpSession session, Model model) {

        // スコアを取得
        Integer score = (Integer) session.getAttribute("play10_score");

        // HTML に渡す
        model.addAttribute("score", score);
        model.addAttribute("total", 10);

        return "result10"; // result10.html を表示
    }

} */
