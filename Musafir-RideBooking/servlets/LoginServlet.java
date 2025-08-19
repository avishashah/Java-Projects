package com.musafir.servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import com.musafir.util.DBConnection;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement pst = con.prepareStatement("SELECT * FROM users WHERE email=? AND password=?");
            pst.setString(1, email);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                HttpSession session = req.getSession();
                session.setAttribute("userId", rs.getInt("id"));           // store user ID
                session.setAttribute("role", rs.getString("role"));        // store role
                session.setAttribute("user", rs.getString("email"));       // ✅ now store user (for navbar)

                // Redirect based on role
                if ("driver".equals(rs.getString("role"))) {
                    resp.sendRedirect("driverDashboard.jsp");
                } else {
                    resp.sendRedirect("userDashboard.jsp");
                }
            } else {
                resp.setContentType("text/html");
                resp.getWriter().println("<p style='color:red;'>Invalid credentials</p>");
            }
        } catch (Exception e) {
            resp.setContentType("text/html");
            resp.getWriter().println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
        }
    }
}
