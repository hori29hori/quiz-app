package com.example.quiz.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.quiz.entity.Quiz;
import com.example.quiz.form.QuizForm;
import com.example.quiz.service.QuizService;
/** Quizコントローラ*/
@Controller
@RequestMapping("/quiz")
public class QuizController {
	/** DI対象*/
	@Autowired
	QuizService service;
	/** 「form-backing bean」の初期化*/
	@ModelAttribute
	public QuizForm setUpForm() {
		QuizForm form = new QuizForm();
		//ラジオボタンのデフォルト値設定
		form.setAnswer(true);
		form.setNewQuiz(true);
		return form;
	}
	/** Quizの一覧を表示します*/
	@GetMapping
	public String showList(Model model) {

	    QuizForm quizForm = new QuizForm();
	    quizForm.setAnswer(true);
	    quizForm.setNewQuiz(true);

	    model.addAttribute("quizForm", quizForm);
	    model.addAttribute("list", service.selectAll());
	    model.addAttribute("title", "登録用フォーム");

	    return "crud";
	}
	/** Quizデータを1件挿入 */
	@PostMapping("/insert")
	public String insert(@Validated QuizForm quizForm,
	                     BindingResult bindingResult,
	                     Model model,
	                     RedirectAttributes redirectAttributes) {

	    if (bindingResult.hasErrors()) {
	        model.addAttribute("quizForm", quizForm);
	        model.addAttribute("list", service.selectAll());
	        model.addAttribute("title", "登録用フォーム");
	        return "crud";
	    }

	    Quiz quiz = new Quiz();
	    quiz.setQuestion(quizForm.getQuestion());
	    quiz.setAnswer(quizForm.getAnswer());
	    quiz.setAuthor(quizForm.getAuthor());

	    service.insertQuiz(quiz);

	    redirectAttributes.addFlashAttribute("complete", "登録が完了しました。");
	    return "redirect:/quiz";
	}
	/** Quizデータを１件取得し、フォーム内に表示する*/
	@GetMapping("/{id}")
	public String showUpdate(@PathVariable Integer id, Model model) {

	    Optional<Quiz> quizOpt = service.selectOneById(id);

	    if (quizOpt.isPresent()) {
	        QuizForm quizForm = makeQuizForm(quizOpt.get());
	        quizForm.setNewQuiz(false);

	        model.addAttribute("quizForm", quizForm);
	        model.addAttribute("title", "更新用フォーム");

	    } else {
	        model.addAttribute("quizForm", new QuizForm());
	        model.addAttribute("title", "データが見つかりません");
	    }

	    return "crud";
	}
	/** 更新用のＭｏｄｅｌを作成する*/
	private void makeUpdateModel(QuizForm quizForm, Model model) {

	    if (quizForm == null) {
	        quizForm = new QuizForm();
	        quizForm.setAnswer(true);
	        quizForm.setNewQuiz(false);
	    }

	    model.addAttribute("quizForm", quizForm);
	    model.addAttribute("title", "更新用フォーム");
	}
	/** idをＫｅｙにしてデータを更新する*/
	@PostMapping("/update")
	public String update(
			@Validated QuizForm quizForm,
			BindingResult result,
			Model model,
			RedirectAttributes redirectAttributes) {
		// QuizFormからＱｕｉｚに詰めなおす
		Quiz quiz = makeQuiz(quizForm);
		//　入力チェック
		if (!result.hasErrors()) {
			//更新処理、フラッシュスコープの使用、リダイレクト（個々の編集ページ）
			service.updateQuiz(quiz);
			redirectAttributes.addFlashAttribute("complete", "更新処理が完了しました");
			// 更新画面を表示する
			return "redirect:/quiz/" + quiz.getId();
		} else {
			//更新用のModelを作成する
			makeUpdateModel(quizForm, model);
			return "crud";
		}
	}
	// ---------- 【以下はFormとDomainObjectの詰めなおし】 ----------
	/** QuizFormからQuizに詰め直して戻り値とし返します　*/
	private Quiz makeQuiz(QuizForm quizForm) {
		Quiz quiz = new Quiz();
		quiz.setId(quizForm.getId());
		quiz.setQuestion(quizForm.getQuestion());
		quiz.setAnswer(quizForm.getAnswer());
		quiz.setAuthor(quizForm.getAuthor());
		return quiz;
	}
	/** QuizからQuizFormに詰め直して戻り値とし返します　*/
	private QuizForm makeQuizForm(Quiz quiz) {
		QuizForm form = new QuizForm();
		form.setId(quiz.getId());
		form.setQuestion(quiz.getQuestion());
		form.setAnswer(quiz.getAnswer());
		form.setAuthor(quiz.getAuthor());
		form.setNewQuiz(false);
		return form;
	}
	/** idをKeyにしてデータを削除する*/
	@PostMapping("/delete")
	public String delete(
		@RequestParam("id") String id,
		Model model,
		RedirectAttributes redirectAttributes) {
		//タスクを１件削除してリダイレクト
		service.deleteQuizById(Integer.parseInt(id));
		redirectAttributes.addFlashAttribute("delcomplete", "削除が完了しました");
		return "redirect:/quiz";
	}
	// ---------- 【以下はクイズで遊ぶ機能】 ----------
	/** Quizデータをランダムで１件取得し、画面に表示する */
	@GetMapping("/play")
	public String showQuiz(QuizForm quizForm, Model model) {
		//Quizを取得（Optionalでラップ）
		Optional<Quiz> quizOpt = service.selectOneRandomQuiz();
		//値が入っているか判定する
		if(quizOpt.isPresent()) {
			// QuizFormへの詰めなおし
			Optional<QuizForm> quizFormOpt = quizOpt.map(t -> makeQuizForm(t));
			quizForm = quizFormOpt.get();
		} else {
			model.addAttribute("quizForm", quizForm);
			return "play";
		}
		// 表示用「Model」への格納
		model.addAttribute("quizForm", quizForm);
		return "play";
	}
	/** クイズの正解/不正解を判定する */
	@PostMapping("/check")
	public String checkQuiz(QuizForm quizForm, @RequestParam Boolean answer, Model model) {
		if (service.checkQuiz(quizForm.getId(), answer)) {
			model.addAttribute("msg", "正解です！");
		} else {
			model.addAttribute("msg","残念、不正解です・・・");
		}
		return "answer";
	}
	
	//１０問
	@GetMapping("/play10/start")
	public String startPlay10(HttpSession session) {

	    // ① 10問分のIDをランダムに取得
	    List<Integer> ids = service.selectRandomIds(10);

	    // ② セッションに保存
	    session.setAttribute("playQuizIds", ids);
	    session.setAttribute("currentIndex", 0);
	    session.setAttribute("answers", new ArrayList<Boolean>());

	    // ③ 最初の問題へ
	    return "redirect:/quiz/play10/question";
	}
	// 10問プレイ中の問題を表示する処理
	@GetMapping("/play10/question")
	public String play10Question(HttpSession session, Model model) {

	    // セッションから10問のIDリストと現在の問題番号を取得
	    List<Integer> ids = (List<Integer>) session.getAttribute("playQuizIds");
	    Integer index = (Integer) session.getAttribute("currentIndex");

	    // セッションが無い or 10問終わっていたら結果画面へ
	    if (ids == null || index == null || index >= ids.size()) {
	        return "redirect:/quiz/play10/result";
	    }

	    // 今の問題IDを取得
	    Integer quizId = ids.get(index);

	    // DBから問題を1件取得
	    Quiz quiz = service.selectOneById(quizId).get();

	    // Quiz → QuizForm に詰め替え
	    QuizForm quizForm = makeQuizForm(quiz);

	    // 画面に渡す
	    model.addAttribute("quizForm", quizForm);
	    model.addAttribute("currentNumber", index + 1); // 第◯問の表示用

	    return "play10"; // play10.html を表示
	}
	// 10問プレイの回答を受け取る処理
	@PostMapping("/play10/answer")
	public String play10Answer(
	        @RequestParam Boolean answer,
	        HttpSession session) {

	    // セッションから回答リスト・IDリスト・現在の問題番号を取得
	    List<Boolean> answers = (List<Boolean>) session.getAttribute("answers");
	    List<Integer> ids = (List<Integer>) session.getAttribute("playQuizIds");
	    Integer index = (Integer) session.getAttribute("currentIndex");

	    // 今の問題IDを取得
	    Integer quizId = ids.get(index);

	    // 正解かどうかを判定（true/false）
	    boolean correct = service.checkQuiz(quizId, answer);

	    // 回答結果をリストに追加
	    answers.add(correct);

	    // 次の問題へ進めるため index を +1
	    session.setAttribute("currentIndex", index + 1);

	    // 次の問題へ
	    return "redirect:/quiz/play10/question";
	}
	
	// 10問プレイの結果を表示する処理
	@GetMapping("/play10/result")
	public String play10Result(HttpSession session, Model model) {

	    // セッションから回答結果（true/false）を取得
	    List<Boolean> answers = (List<Boolean>) session.getAttribute("answers");

	    // 正解数を数える（true の数）
	    long correctCount = answers.stream().filter(a -> a).count();

	    // 画面に渡す
	    model.addAttribute("correctCount", correctCount);

	    return "result10"; // result10.html を表示
	}
	

}
