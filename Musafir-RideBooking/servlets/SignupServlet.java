package com.musafir.servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import com.musafir.util.DBConnection;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String phone = req.getParameter("phone");
        String city = req.getParameter("city");

        try(Connection con = DBConnection.getConnection()) {
            PreparedStatement pst = con.prepareStatement(
              "INSERT INTO users(name,email,password,phone,city,role) VALUES(?,?,?,?,?,?)");
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, password);
            pst.setString(4, phone);
            pst.setString(5, city);
            pst.setString(6, "user");
            pst.executeUpdate();
            resp.sendRedirect("login.jsp");
        } catch(Exception e) {
            resp.getWriter().println("Error: " + e.getMessage());
        }
    }
}
