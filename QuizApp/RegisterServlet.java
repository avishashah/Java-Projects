package com.aurionpro;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String phone = req.getParameter("phone");
		String username = req.getParameter("username");
		String password = req.getParameter("password");

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new ServletException("MySQL JDBC Driver not found in classpath.", e);
		}

		String url = "jdbc:mysql://localhost:3306/quiz_app";
		String dbUser = "root";
		String dbPass = "Avisha@2606";

		try (Connection con = DriverManager.getConnection(url, dbUser, dbPass)) {

			PreparedStatement ps = con.prepareStatement("SELECT id FROM users WHERE username = ?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				resp.setContentType("text/html");
				PrintWriter out = resp.getWriter();

				out.println("<!doctype html>");
				out.println("<html lang='en'>");
				out.println("<head>");
				out.println("  <meta charset='utf-8'>");
				out.println("  <title>Register - QuizPop</title>");
				out.println("  <meta name='viewport' content='width=device-width, initial-scale=1'>");
				out.println(
						"  <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>");
				out.println("  <link href='css/style.css' rel='stylesheet'>");
				out.println("</head>");
				out.println("<body class='bg-lav'>");
				out.println(
						"<div class='container d-flex justify-content-center align-items-center' style='min-height:90vh'>");
				out.println("  <div class='card p-4 rounded-4 shadow-sm register-card'>");
				out.println("    <h3 class='text-center mb-3'>Register</h3>");

				out.println("    <div class='alert alert-danger'>Username already taken. Choose another.</div>");

				out.println("    <form action='register' method='post' novalidate>");
				out.println("      <div class='mb-2'>");
				out.println("        <label class='form-label'>Name</label>");
				out.println("        <input name='name' value='"
						+ "' required class='form-control rounded-pill'>");
				out.println("      </div>");
				out.println("      <div class='mb-2'>");
				out.println("        <label class='form-label'>Email</label>");
				out.println("        <input name='email' type='email' value='" 
						+ "' required class='form-control rounded-pill'>");
				out.println("      </div>");
				out.println("      <div class='mb-2'>");
				out.println("        <label class='form-label'>Phone</label>");
				out.println("        <input name='phone' value='" 
						+ "' class='form-control rounded-pill'>");
				out.println("      </div>");
				out.println("      <div class='mb-2'>");
				out.println("        <label class='form-label'>Username</label>");
				out.println("        <input name='username' value='"
						+ "' required class='form-control rounded-pill'>");
				out.println("      </div>");
				out.println("      <div class='mb-3'>");
				out.println("        <label class='form-label'>Password</label>");
				out.println(
						"        <input name='password' type='password' required class='form-control rounded-pill'>");
				out.println("      </div>");
				out.println("      <div class='d-grid gap-2'>");
				out.println("        <button class='btn btn-primary rounded-pill' type='submit'>Register</button>");
				out.println("        <a class='text-center' href='login.html'>Already have account? Login</a>");
				out.println("      </div>");
				out.println("    </form>");

				out.println("  </div>");
				out.println("</div>");
				out.println("</body>");
				out.println("</html>");
				return;
			}

			ps = con.prepareStatement("INSERT INTO users (name,email,phone,username,password) VALUES (?,?,?,?,?)");
			ps.setString(1, name);
			ps.setString(2, email);
			ps.setString(3, phone);
			ps.setString(4, username);
			ps.setString(5, password);
			ps.executeUpdate();

			resp.sendRedirect("login.html");
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}

	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.getRequestDispatcher("register.html").forward(req, resp);
	}
}
