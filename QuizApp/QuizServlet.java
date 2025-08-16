package com.aurionpro;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

@WebServlet("/quiz")
public class QuizServlet extends HttpServlet {

    private static final int QUIZ_DURATION_SECONDS = 180; // 3 minutes

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        
        Object userId = (session != null) ? session.getAttribute("userId") : null;
        Object username = (session != null) ? session.getAttribute("username") : null;
        if (session == null || (userId == null && username == null)) {
            resp.sendRedirect("login.html");
            return;
        }
        
        Long quizStartTime = (Long) session.getAttribute("quizStartTime");
        if (quizStartTime == null) {
            quizStartTime = System.currentTimeMillis();
            session.setAttribute("quizStartTime", quizStartTime);
        }

        long elapsedMillis = System.currentTimeMillis() - quizStartTime;
        int remainingSeconds = QUIZ_DURATION_SECONDS - (int) (elapsedMillis / 1000);
        if (remainingSeconds <= 0) {
            resp.sendRedirect("submit");
            return;
        }

        session.setMaxInactiveInterval(QUIZ_DURATION_SECONDS + 60);

        @SuppressWarnings("unchecked")
        List<Integer> quizIds = (List<Integer>) session.getAttribute("quizIds");
        if (quizIds == null) {
            resp.sendRedirect("login.html");
            return;
        }

        int index = 0;
        try {
            index = Integer.parseInt(req.getParameter("index"));
        } catch (Exception ignored) {}

        if (index < 0) index = 0;
        if (index >= quizIds.size()) index = quizIds.size() - 1;

        int qid = quizIds.get(index);

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM questions WHERE id = ?");
            ps.setInt(1, qid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                resp.getWriter().println("Question not found");
                return;
            }

            String qtext = rs.getString("question_text");
            String[] opts = {
                rs.getString("opt1"), rs.getString("opt2"),
                rs.getString("opt3"), rs.getString("opt4")
            };

            @SuppressWarnings("unchecked")
            Map<Integer, Integer> answers = (Map<Integer, Integer>) session.getAttribute("answers");
            if (answers == null) {
                answers = new HashMap<>();
                session.setAttribute("answers", answers);
            }

            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("<!doctype html><html><head><meta charset='utf-8'><title>Quiz</title>");
            out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("<link href='css/style.css' rel='stylesheet'>");
            out.println("</head><body class='bg-lav'>");

            // Navbar
            out.println("<div class='container mt-3'>");
            out.println("<nav class='navbar navbar-quiz p-3 rounded-4'>");
            out.println("<div class='container-fluid'>");
            out.println("<span class='nav-brand'>QuizPop</span>");
            out.println("<div class='float-end'>");
            out.println("<span style='margin-right:12px'>Welcome, " + escapeHtml((String) username) + "</span>");
            out.println("<a href='logout' class='btn btn-sm btn-light rounded-pill'>Logout</a>");
            out.println("</div></div></nav></div>");

            // Quiz UI
            out.println("<div class='container mt-4 quiz-container'>");
            out.println("<div class='left-col'>");
            out.println("<div class='qcard'>");
            out.println("<h5>Question " + (index + 1) + " of " + quizIds.size() + "</h5>");
            out.println("<div class='mt-3 p-3 rounded-3' style='background:#f7f7ff'><strong>" 
                        + escapeHtml(qtext) + "</strong></div>");

            out.println("<form method='post' action='quiz'>");
            out.println("<input type='hidden' name='index' value='" + index + "'>");
            out.println("<input type='hidden' name='qid' value='" + qid + "'>");

            Integer selected = answers.get(qid);
            String[] classes = { "opt-red", "opt-green", "opt-yellow", "opt-blue" };
            for (int i = 0; i < 4; i++) {
                String selClass = (selected != null && selected == (i + 1)) ? " opt-selected" : "";
                out.println("<button name='option' value='" + (i + 1) + "' type='submit' class='w-100 text-start opt-box "
                        + classes[i] + selClass + "'>");
                out.println("<span style='font-weight:600;margin-right:10px'>" + (char) ('A' + i) + ".</span> "
                        + escapeHtml(opts[i]) + "</button>");
            }
            out.println("</form>");

            out.println("<div class='mt-3 d-flex justify-content-between'>");
            if (index > 0) {
                out.println("<a href='quiz?index=" + (index - 1) + "' class='btn btn-outline-secondary rounded-pill'>Back</a>");
            } else {
                out.println("<a href='home' class='btn btn-outline-secondary rounded-pill'>Back to Home</a>");
            }

            if (index < quizIds.size() - 1) {
                out.println("<a href='quiz?index=" + (index + 1) + "' class='btn btn-primary rounded-pill'>Next</a>");
            } else {
                boolean allAnswered = true;
                for (Integer id : quizIds) {
                    if (!answers.containsKey(id)) {
                        allAnswered = false;
                        break;
                    }
                }
                if (allAnswered) {
                    out.println("<form method='post' action='submit' style='display:inline'>"
                              + "<button class='btn btn-success rounded-pill'>Submit Quiz</button></form>");
                } else {
                    out.println("<button class='btn btn-secondary rounded-pill' disabled>Submit (answer all)</button>");
                }
            }
            out.println("</div></div></div>");

            // Right column
            out.println("<div class='right-col'><div class='qcard'>");
            out.println("<div class='center mb-2'><strong>Questions</strong></div>");
            for (int i = 0; i < quizIds.size(); i++) {
                int idLoop = quizIds.get(i);
                boolean answered = answers.containsKey(idLoop);
                String cls = "qnum-box " + (answered ? "answered" : "unanswered") + (i == index ? " current" : "");
                out.println("<a href='quiz?index=" + i + "' class='" + cls + "'>" + (i + 1) + "</a>");
            }
            out.println("<hr><div class='mt-2 center'>Time left: <span id='timer'></span></div>");
            out.println("</div></div></div>");

            // Timer script
            out.println("<script>");
            out.println("let timeLeft = " + remainingSeconds + ";");
            out.println("const timerEl = document.getElementById('timer');");
            out.println("function updateTimer(){let m=Math.floor(timeLeft/60);let s=timeLeft%60;"
                      + "timerEl.textContent=m+':' + (s<10?'0'+s:s);"
                      + "if(timeLeft<=0){alert('Time is up! Submitting quiz.');window.location.href='submit';}"
                      + "timeLeft--;}"
                      + "updateTimer();setInterval(updateTimer,1000);");
            out.println("</script>");
            out.println("</body></html>");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || (session.getAttribute("userId") == null && session.getAttribute("username") == null)) {
            resp.sendRedirect("login.html");
            return;
        }
        String qidStr = req.getParameter("qid");
        String opt = req.getParameter("option");
        String indexStr = req.getParameter("index");
        int index = 0;
        try { index = Integer.parseInt(indexStr); } catch (Exception ignored) {}

        if (qidStr != null && opt != null) {
            int qid = Integer.parseInt(qidStr);
            int selected = Integer.parseInt(opt);
            @SuppressWarnings("unchecked")
            Map<Integer, Integer> answers = (Map<Integer, Integer>) session.getAttribute("answers");
            if (answers == null) {
                answers = new HashMap<>();
                session.setAttribute("answers", answers);
            }
            answers.put(qid, selected);
        }

        resp.sendRedirect("quiz?index=" + index);
    }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;");
    }
}
