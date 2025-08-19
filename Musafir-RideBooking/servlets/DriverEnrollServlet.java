package com.musafir.servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import com.musafir.util.DBConnection;

@WebServlet("/enrollDriver")
public class DriverEnrollServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        int userId = (int) session.getAttribute("userId");

        String vehicle = req.getParameter("vehicle");
        String plate = req.getParameter("plate");

        try(Connection con = DBConnection.getConnection()) {
            PreparedStatement pst = con.prepareStatement("INSERT INTO drivers(user_id,vehicle_type,number_plate) VALUES(?,?,?)");
            pst.setInt(1, userId);
            pst.setString(2, vehicle);
            pst.setString(3, plate);
            pst.executeUpdate();

            PreparedStatement updateRole = con.prepareStatement("UPDATE users SET role='driver' WHERE id=?");
            updateRole.setInt(1, userId);
            updateRole.executeUpdate();

            resp.sendRedirect("driverDashboard.jsp");
        } catch(Exception e) {
            resp.getWriter().println("Error: "+e.getMessage());
        }
    }
}
