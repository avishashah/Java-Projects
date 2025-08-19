package com.musafir.servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import com.musafir.util.DBConnection;

@WebServlet("/driverDashboard")
public class DriverDashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        if(session == null || session.getAttribute("userId") == null) {
            out.println("<div class='text-danger'>Please login first.</div>");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        try(Connection con = DBConnection.getConnection()) {
            PreparedStatement pst = con.prepareStatement("SELECT balance FROM users WHERE id=?");
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();

            if(rs.next()) {
                double balance = rs.getDouble("balance");
                out.println("<h4 class='text-success'>Driver Balance: Rs. " + balance + "</h4>");
            } else {
                out.println("<div class='text-warning'>Balance information not found.</div>");
            }
        } catch(Exception e) {
            out.println("<div class='text-danger'>Error: " + e.getMessage() + "</div>");
        }
    }
}
