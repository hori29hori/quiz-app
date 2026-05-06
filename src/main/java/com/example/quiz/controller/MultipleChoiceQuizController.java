package com.example.quiz.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.quiz.entity.MultipleChoiceQuiz;
import com.example.quiz.repository.MultipleChoiceQuizRepository;

@Controller
public class MultipleChoiceQuizController {

    private final MultipleChoiceQuizRepository repository;

    public MultipleChoiceQuizController(MultipleChoiceQuizRepository repository) {
        this.repository = repository;
    }

    /** 一覧表示 */
    @GetMapping("/quiz/mc/list")
    public String list(Model model) {
        List<MultipleChoiceQuiz> list = (List<MultipleChoiceQuiz>) repository.findAll();
        model.addAttribute("list", list);
        return "mc_list";
    }

    /** 登録画面 */
    @GetMapping("/quiz/mc/add")
    public String showAddForm(Model model) {
        model.addAttribute("quiz", new MultipleChoiceQuiz());
        return "mc_add";
    }

    /** 登録 */
    @PostMapping("/quiz/mc/add")
    public String addQuiz(MultipleChoiceQuiz quiz) {
        repository.save(quiz);
        return "redirect:/quiz/mc/list";
    }

    /** 編集画面 */
    @GetMapping("/quiz/mc/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        MultipleChoiceQuiz quiz = repository.findById(id).orElse(null);
        model.addAttribute("quiz", quiz);
        return "mc_edit";
    }

    /** 更新 */
    @PostMapping("/quiz/mc/update")
    public String update(MultipleChoiceQuiz quiz) {
        repository.save(quiz);
        return "redirect:/quiz/mc/list";
    }

    /** 削除 */
    @PostMapping("/quiz/mc/delete")
    public String delete(@RequestParam Integer id) {
        repository.deleteById(id);
        return "redirect:/quiz/mc/list";
    }

    /** 4択問題出題 */
    @GetMapping("/quiz/mc/play")
    public String play(Model model) {

        List<MultipleChoiceQuiz> list =
                (List<MultipleChoiceQuiz>) repository.findAll();

        if (list == null || list.isEmpty()) {
            model.addAttribute("msg", "問題がありません");
            return "mc_play";
        }

        MultipleChoiceQuiz quiz =
                list.get((int) (Math.random() * list.size()));

        model.addAttribute("quiz", quiz);

        return "mc_play";
    }

    /** 回答チェック */
    @PostMapping("/quiz/mc/check")
    public String check(
            @RequestParam Integer id,
            @RequestParam(required = false) Integer answer,
            Model model) {

        MultipleChoiceQuiz quiz =
                repository.findById(id).orElse(null);

        if (quiz == null) {
            model.addAttribute("msg", "問題が見つかりません");
            return "mc_result";
        }

        if (answer == null) {
            model.addAttribute("msg", "選択してください");
            return "mc_result";
        }

        if (answer.equals(quiz.getAnswer())) {
            model.addAttribute("msg", "正解！");
        } else {
            model.addAttribute("msg", "不正解...");
        }

        return "mc_result";
    }
    @PostMapping("/quiz/mc/csv")
    public String uploadCsv(@RequestParam("file") MultipartFile file, Model model) {
    	
    	 if (file.isEmpty()) {
    	        model.addAttribute("msg", "ファイルを選択してください");
    	        return "csv";
    	    }

        try {
            List<String> lines = new BufferedReader(
                    new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
                    .lines().toList();
            
            if (lines.isEmpty()) {
                model.addAttribute("msg", "CSVファイルが空です");
                return "csv";
            }

            for (String line : lines) {
                String[] data = line.split(",");
                
                if (data.length < 6) {
                    continue;
                }

                MultipleChoiceQuiz quiz = new MultipleChoiceQuiz();
                quiz.setQuestion(data[0]);
                quiz.setChoice1(data[1]);
                quiz.setChoice2(data[2]);
                quiz.setChoice3(data[3]);
                quiz.setChoice4(data[4]);
                quiz.setAnswer(Integer.parseInt(data[5]));

                repository.save(quiz);
            }

            model.addAttribute("msg", "CSV登録が完了しました！");

        } catch (Exception e) {
            model.addAttribute("msg", "CSVの読み込みに失敗しました");
        }

        return "csv";  
    }
    @GetMapping("/quiz/mc/csv")
    public String showCsvForm() {
        return "csv";
    }


}
