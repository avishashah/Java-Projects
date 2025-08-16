package com.aurionpro;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT id, password, name FROM users WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String pwd = rs.getString("password");
                if (pwd.equals(password)) {
                    int userId = rs.getInt("id");

                    HttpSession session = req.getSession(true);
                    session.setAttribute("userId", userId);
                    session.setAttribute("username", username);
                    session.setAttribute("name", rs.getString("name"));

                    PreparedStatement psQ = con.prepareStatement("SELECT id FROM questions ORDER BY RAND() LIMIT 3");
                    ResultSet rsq = psQ.executeQuery();
                    List<Integer> qids = new ArrayList<>();
                    while (rsq.next()) qids.add(rsq.getInt("id"));
                    session.setAttribute("quizIds", qids);
                    session.setAttribute("answers", new HashMap<Integer, Integer>());
                    session.setAttribute("quizStartTime", null);

                    resp.sendRedirect("home");
                    return;
                }
            }

            resp.setContentType("text/html");
            resp.getWriter().println("<html><body><script>alert('Invalid username/password');window.location='login.html';</script></body></html>");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
