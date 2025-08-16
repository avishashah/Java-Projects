package com.aurionpro;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        if (session.getAttribute("quizIds") == null) {
            try (Connection con = DBConnection.getConnection()) {
                PreparedStatement psQ = con.prepareStatement("SELECT id FROM questions ORDER BY RAND() LIMIT 3");
                ResultSet rsq = psQ.executeQuery();
                List<Integer> qids = new ArrayList<>();
                while (rsq.next()) qids.add(rsq.getInt("id"));
                session.setAttribute("quizIds", qids);
                session.setAttribute("answers", new HashMap<Integer, Integer>());
                session.setAttribute("quizStartTime", null);
            } catch (SQLException e) {
                throw new IOException(e);
            }
        }

        List<Map<String, Object>> leaderboard = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                SELECT u.username, SUM(r.score) AS total_score
                FROM results r
                JOIN users u ON r.user_id = u.id
                GROUP BY u.id, u.username
                ORDER BY total_score DESC
                LIMIT 3
            """;
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("username", rs.getString("username"));
                    entry.put("score", rs.getInt("total_score"));
                    leaderboard.add(entry);
                }
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("  <meta charset='UTF-8'>");
        out.println("  <meta name='viewport' content='width=device-width, initial-scale=1'>");
        out.println("  <title>Quiz App</title>");
        out.println("  <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css' rel='stylesheet'>");
        out.println("  <style>");
        out.println("    body { background-color: lavender; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }");
        out.println("    .navbar-quiz { background-color: #783ACC; padding: 0.8rem 1rem; border-radius: 1rem; color: white; }");
        out.println("    .nav-brand { font-size: 1.5rem; font-weight: bold; font-family: 'Trebuchet MS', sans-serif; color: white; }");
        out.println("    .main-content { margin-top: 2rem; }");
        out.println("    .podium { display: flex; justify-content: center; align-items: flex-end; gap: 15px; margin-top: 2rem; }");
        out.println("    .podium div { background: #783ACC; color: white; text-align: center; width: 100px; border-radius: 8px 8px 0 0; }");
        out.println("    .first { height: 200px; background-color: #DAA520 !important; font-weight: bold; }");
        out.println("    .second { height: 150px; background-color: silver !important; font-weight: bold; }");
        out.println("    .third { height: 120px; background-color: #CD7F32 !important; font-weight: bold; }");

        out.println("    .podium span { display: block; padding: 5px; background: rgba(0,0,0,0.2); border-radius: 8px 8px 0 0; }");
        out.println("  </style>");
        out.println("</head>");
        out.println("<body>");

        // Navbar
        out.println("  <div class='container mt-3'>");
        out.println("    <nav class='navbar navbar-quiz p-3 rounded-4'>");
        out.println("      <div class='container-fluid'>");
        out.println("        <span class='nav-brand'>QuizPop</span>");
        out.println("        <a href='logout' class='btn btn-sm btn-light rounded-pill'>Logout</a>");
        out.println("      </div>");
        out.println("    </nav>");
        out.println("  </div>");

        // Main content
        out.println("  <div class='container text-center main-content'>");
        out.println("    <h1 class='mb-4'>Welcome to the Quiz App</h1>");
        out.println("    <a href='quiz' class='btn btn-primary btn-lg mx-2'>Take Quiz</a>");
        out.println("    <a href='viewResults' class='btn btn-primary btn-lg mx-2'>View Result</a>");
        out.println("  </div>");

        // Leaderboard Podium
        out.println("  <div class='container text-center'>");
        out.println("    <h3 class='mt-5'>Leaderboard</h3>");
        out.println("    <div class='podium'>");

        String second = leaderboard.size() > 1 ? (String) leaderboard.get(1).get("username") : "-";
        String first = leaderboard.size() > 0 ? (String) leaderboard.get(0).get("username") : "-";
        String third = leaderboard.size() > 2 ? (String) leaderboard.get(2).get("username") : "-";

        out.println("      <div class='second'><span>" + second + "</span><br>2</div>");
        out.println("      <div class='first'><span>" + first + "</span><br>1</div>");
        out.println("      <div class='third'><span>" + third + "</span><br>3</div>");
        out.println("    </div>");
        out.println("  </div>");

        out.println("  <script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js'></script>");
        out.println("</body>");
        out.println("</html>");
    }
}
