package com.aurionpro;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.sql.*;

@WebServlet("/score")
public class ScoreServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session==null || session.getAttribute("userId")==null) {
            resp.sendRedirect("login.html");
            return;
        }
        Integer rid = (Integer) session.getAttribute("lastResultId");
        if (rid == null) {
            resp.getWriter().println("No result found");
            return;
        }
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT r.score, r.total_questions, r.taken_at, u.username FROM results r JOIN users u ON r.user_id = u.id WHERE r.id = ?");
            ps.setInt(1, rid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                resp.getWriter().println("Result not found");
                return;
            }
            int score = rs.getInt("score");
            int total = rs.getInt("total_questions");
            String username = rs.getString("username");
            resp.setContentType("text/html");
            resp.getWriter().println("<!doctype html><html><head><meta charset='utf-8'><title>Score</title>");
            resp.getWriter().println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>");
            resp.getWriter().println("<link href='css/style.css' rel='stylesheet'></head><body class='bg-lav'>");
            resp.getWriter().println("<div class='container d-flex justify-content-center align-items-center' style='min-height:90vh'>");
            resp.getWriter().println("<div class='card p-4 rounded-4 shadow-sm text-center'>");
            resp.getWriter().println("<h3>Well done, "+"!</h3>");
            resp.getWriter().println("<p>Your Score: <strong>"+score+"</strong> / "+total+"</p>");
            resp.getWriter().println("<a href='login.html' class='btn btn-primary rounded-pill'>Take another quiz</a> ");
            resp.getWriter().println("<a href='logout' class='btn btn-outline-danger rounded-pill'>Logout</a>");
            resp.getWriter().println("</div></div></body></html>");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

   
}
