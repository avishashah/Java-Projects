package com.musafir.servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import com.musafir.util.DBConnection;

@WebServlet("/bookRide")
public class RideBookingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        if (session == null || session.getAttribute("userId") == null) {
            out.println("<div class='text-danger'>Please log in to book a ride.</div>");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        String pickup = req.getParameter("pickup");
        String drop = req.getParameter("drop");
        String payment = req.getParameter("payment");
        String vehicleType = req.getParameter("vehicleType"); // ✅ new field

        try (Connection con = DBConnection.getConnection()) {
            // ✅ Get all drivers in the same city + matching vehicle type
            PreparedStatement ps = con.prepareStatement(
                "SELECT d.id, u.name " +
                "FROM drivers d " +
                "JOIN users u ON d.user_id = u.id " +
                "WHERE u.city = (SELECT city FROM users WHERE id = ?) " +
                "AND u.role = 'driver' " +
                "AND d.vehicle_type = ?"
            );
            ps.setInt(1, userId);
            ps.setString(2, vehicleType);
            ResultSet rs = ps.executeQuery();

            List<Integer> driverIds = new ArrayList<>();
            Map<Integer, String> driverNames = new HashMap<>();

            while (rs.next()) {
                int driverId = rs.getInt("id");
                String driverName = rs.getString("name");
                driverIds.add(driverId);
                driverNames.put(driverId, driverName);
            }

            if (driverIds.isEmpty()) {
                out.println("<div class='alert alert-warning'>No " + vehicleType + " driver available in your city.</div>");
            } else {
                // ✅ Pick a random driver
                Random random = new Random();
                int driverId = driverIds.get(random.nextInt(driverIds.size()));
                String driverName = driverNames.get(driverId);

                PreparedStatement pst = con.prepareStatement(
                    "INSERT INTO rides(user_id, driver_id, pickup_location, drop_location, payment_method, fare) " +
                    "VALUES (?, ?, ?, ?, ?, ?)"
                );
                pst.setInt(1, userId);
                pst.setInt(2, driverId);
                pst.setString(3, pickup);
                pst.setString(4, drop);
                pst.setString(5, payment);

                // ✅ Fare based on vehicle type
                double fare = switch (vehicleType) {
                    case "car_premium" -> 300.0;
                    case "car_6seater" -> 250.0;
                    case "auto" -> 120.0;
                    case "bike" -> 80.0;
                    default -> 150.0; // car_economy
                };

                pst.setDouble(6, fare);
                pst.executeUpdate();

                out.println("<div class='alert alert-success'>");
                out.println("Ride booked with driver: <strong>" + driverName + "</strong> (" + vehicleType + ")");
                out.println("<br>Fare: ₹" + fare);
                out.println("<br><a href='ChatServlet'>Open Chat</a>");
                out.println("</div>");
            }
        } catch (Exception e) {
            out.println("<div class='alert alert-danger'>Error: " + e.getMessage() + "</div>");
        }
    }
}
