package com.musafir.servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;

@WebServlet("/checkPrice")
public class PriceCheckServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String vehicle = req.getParameter("vehicle");
        String pickup = req.getParameter("pickup");
        String drop = req.getParameter("drop");

        double baseFare = 50;

        // Null check to avoid 500 error if vehicle parameter is missing
        if (vehicle == null) {
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("<h3 style='color:red'>Error: Vehicle type not selected or form data incorrect.</h3>");
            return;
        }

        if (vehicle.equals("car_premium")) {
            baseFare = 200;
        } else if (vehicle.equals("car_6seater")) {
            baseFare = 150;
        } else if (vehicle.equals("auto")) {
            baseFare = 100;
        } else if (vehicle.equals("bike")) {
            baseFare = 70;
        }

        double fare = baseFare + Math.random() * 100;

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<h3 style='color:red'>Estimated Fare: Rs. " + (int)fare + "</h3>");
    }
}
