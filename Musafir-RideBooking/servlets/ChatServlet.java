package com.musafir.servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;

@WebServlet("/ChatServlet")
public class ChatServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.print("<h2>Chat Window with Driver</h2>");
        out.print("<textarea rows='5' cols='40'></textarea><br>");
        out.print("<input type='text'><button>Send</button>");
    }
}
