package com.aurionpro;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/viewResults")
public class ViewResultsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            out.println("<p>Please <a href='login.html'>log in</a> to view results.</p>");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT taken_at, score, total_questions FROM results WHERE user_id = ? ORDER BY taken_at DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Quiz Results</title>");
            out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("</head>");
            out.println("<body class='bg-light'>");

            out.println("<div class='container py-5'>");
            out.println("<h1 class='mb-4'>Your Previous Results</h1>");

            if (!rs.isBeforeFirst()) {
                out.println("<div class='alert alert-info'>No quizzes taken yet. <a href='quiz'>Take a quiz</a></div>");
            } else {
                while (rs.next()) {
                    out.println("<div class='card shadow-sm mb-3'>");
                    out.println("<div class='card-body'>");
                    out.println("<h5 class='card-title'>Date: " + rs.getTimestamp("taken_at") + "</h5>");
                    out.println("<p class='card-text'><strong>Score:</strong> " 
                                + rs.getInt("score") + " / " + rs.getInt("total_questions") + "</p>");
                    out.println("</div>");
                    out.println("</div>");
                }
            }

            out.println("<a href='home.html' class='btn btn-primary mt-4'>Back to Home</a>");
            out.println("</div>");

            out.println("</body>");
            out.println("</html>");

        } catch (SQLException e) {
            out.println("<div class='container py-5'><div class='alert alert-danger'>Error retrieving results.</div></div>");
            e.printStackTrace(out);
        }
    }
}
