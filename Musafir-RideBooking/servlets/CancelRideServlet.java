package com.musafir.servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import com.musafir.util.DBConnection;

@WebServlet("/cancelRide")
public class CancelRideServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        int rideId = Integer.parseInt(req.getParameter("rideId"));
        String reason = req.getParameter("reason");
        try(Connection con = DBConnection.getConnection()) {
            PreparedStatement pst = con.prepareStatement("UPDATE rides SET status='cancelled', cancellation_reason=? WHERE id=?");
            pst.setString(1, reason);
            pst.setInt(2, rideId);
            pst.executeUpdate();
            resp.getWriter().println("<h3>Ride Cancelled. Reason: "+reason+"</h3>");
        } catch(Exception e) {
            resp.getWriter().println("Error: "+e.getMessage());
        }
    }
}
