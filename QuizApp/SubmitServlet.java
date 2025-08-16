package com.aurionpro;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

@WebServlet("/submit")
public class SubmitServlet extends HttpServlet {
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session == null || session.getAttribute("userId") == null) {
			resp.sendRedirect("login.html");
			return;
		}

		Integer userId = (Integer) session.getAttribute("userId");
		@SuppressWarnings("unchecked")
		List<Integer> quizIds = (List<Integer>) session.getAttribute("quizIds");
		@SuppressWarnings("unchecked")
		Map<Integer, Integer> answers = (Map<Integer, Integer>) session.getAttribute("answers");

		if (quizIds == null || answers == null) {
			resp.sendRedirect("login.html");
			return;
		}

		int score = 0;

		try (Connection con = DBConnection.getConnection()) {
			
			String placeholders = String.join(",", Collections.nCopies(quizIds.size(), "?"));
			PreparedStatement ps = con.prepareStatement("SELECT id, question_text, opt1, opt2, opt3, opt4, correct_opt "
					+ "FROM questions WHERE id IN (" + placeholders + ")");
			for (int i = 0; i < quizIds.size(); i++) {
				ps.setInt(i + 1, quizIds.get(i));
			}
			ResultSet rs = ps.executeQuery();

			Map<Integer, Integer> correctMap = new HashMap<>();
			Map<Integer, String[]> questionData = new HashMap<>();
			while (rs.next()) {
				int id = rs.getInt("id");
				correctMap.put(id, rs.getInt("correct_opt"));
				questionData.put(id, new String[] { rs.getString("question_text"), rs.getString("opt1"),
						rs.getString("opt2"), rs.getString("opt3"), rs.getString("opt4") });
			}

			List<Integer> incorrectIds = new ArrayList<>();
			for (int qid : quizIds) {
				Integer sel = answers.get(qid);
				Integer correct = correctMap.get(qid);
				if (sel != null && sel.equals(correct)) {
					score++;
				} else {
					incorrectIds.add(qid);
				}
			}

			PreparedStatement ins = con.prepareStatement(
					"INSERT INTO results (user_id, score, total_questions) VALUES (?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			ins.setInt(1, userId);
			ins.setInt(2, score);
			ins.setInt(3, quizIds.size());
			ins.executeUpdate();
			ResultSet gen = ins.getGeneratedKeys();
			int resultId = -1;
			if (gen.next())
				resultId = gen.getInt(1);

			PreparedStatement insAns = con.prepareStatement(
					"INSERT INTO user_answers (result_id, question_id, selected_opt, is_correct) VALUES (?,?,?,?)");
			for (int qid : quizIds) {
				Integer sel = answers.get(qid);
				boolean correct = sel != null && sel.equals(correctMap.get(qid));
				insAns.setInt(1, resultId);
				insAns.setInt(2, qid);
				insAns.setInt(3, sel == null ? 0 : sel);
				insAns.setBoolean(4, correct);
				insAns.executeUpdate();
			}

			session.removeAttribute("quizIds");
			session.removeAttribute("answers");

			resp.setContentType("text/html");
			PrintWriter out = resp.getWriter();

			out.println("<!doctype html><html><head><meta charset='utf-8'><title>Quiz Result</title>");
			out.println(
					"<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>");
			out.println("<style>");
			out.println(".opt-box {padding:10px;border-radius:8px;margin-bottom:8px;border:2px solid transparent;}");
			out.println(".correct-border {border-color:green !important;}");
			out.println(".incorrect-border {border-color:red !important;}");
			out.println(".opt-white {background:#fff;}");
			out.println("</style>");
			out.println("</head><body class='p-4 bg-light'>");

			out.println("<div class='container'>");
			out.println("<h2>Your Score: " + score + " / " + quizIds.size() + "</h2>");
			if (incorrectIds.isEmpty()) {
				out.println("<div class='alert alert-success'>Perfect score! No mistakes 🎉</div>");
			} else {
				out.println("<h4 class='mt-4'>Questions you got wrong:</h4>");
			}

			for (int idx = 0; idx < incorrectIds.size(); idx++) {
				int qid = incorrectIds.get(idx);
				String[] qData = questionData.get(qid); 
				Integer sel = answers.get(qid);
				Integer correct = correctMap.get(qid);

				out.println("<div class='card mb-4'>");
				out.println("<div class='card-body'>");
				out.println("<h5>Question " + (idx + 1) + "</h5>");
				out.println("<div class='mt-3 p-3 rounded-3 bg-white'><strong>" + qData[0] + "</strong></div>");

				for (int optIndex = 0; optIndex < 4; optIndex++) {
					String borderClass = "";
					if (correct != null && (optIndex + 1) == correct) {
						borderClass = " correct-border";
					} else if (sel != null && (optIndex + 1) == sel) {
						borderClass = " incorrect-border";
					}
					out.println("<div class='opt-box opt-white " + borderClass + "'>");
					out.println("<span style='font-weight:600;margin-right:10px'>" + (char) ('A' + optIndex)
							+ ".</span> " + qData[optIndex + 1]); 
					out.println("</div>");
				}

				out.println("</div></div>");
			}

			out.println("<a href='home' class='btn btn-primary mt-3'>Back to Home</a>");
			out.println("</div></body></html>");

		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}

}
