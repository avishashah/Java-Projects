package com.aurionpro;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

@WebServlet("/leaderboard")
public class LeaderboardServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");

		try (PrintWriter out = resp.getWriter();
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz_app", "root",
						"Avisha@2606")) {

			String sql = """
					    SELECT u.username, SUM(r.score) AS total_score
					    FROM results r
					    JOIN users u ON r.user_id = u.id
					    GROUP BY u.id, u.username
					    ORDER BY total_score DESC
					    LIMIT 3
					""";

			List<String> usernames = new ArrayList<>();
			List<Integer> scores = new ArrayList<>();

			try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

				while (rs.next()) {
					usernames.add(rs.getString("username"));
					scores.add(rs.getInt("total_score"));
				}
			}

			out.println("<!DOCTYPE html>");
			out.println("<html lang='en'>");
			out.println("<head>");
			out.println("<meta charset='UTF-8'>");
			out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
			out.println("<title>Leaderboard</title>");
			out.println(
					"<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
			out.println("<style>");
			out.println(".podium { display: flex; align-items: flex-end; justify-content: center; margin-top: 30px; }");
			out.println(
					".podium-step { width: 120px; text-align: center; background-color: #f8f9fa; border-radius: 10px 10px 0 0; margin: 0 10px; padding-top: 10px; }");
			out.println(".username { font-weight: bold; margin-bottom: 10px; }");
			out.println("</style>");
			out.println("</head>");
			out.println("<body class='bg-light'>");

			out.println("<div class='container text-center mt-5'>");
			out.println("<h2 class='mb-4'>Leaderboard</h2>");
			out.println("<div class='podium'>");

			// Second place
			if (usernames.size() > 1) {
				out.println("<div class='podium-step second'>");
				out.println("<div class='username'>" + usernames.get(1) + "</div>");
				out.println("<div>" + scores.get(1) + " pts</div>");
				out.println("</div>");
			}

			// First place
			if (usernames.size() > 0) {
				out.println("<div class='podium-step first'>");
				out.println("<div class='username'>" + usernames.get(0) + "</div>");
				out.println("<div>" + scores.get(0) + " pts</div>");
				out.println("</div>");
			}

			// Third place
			if (usernames.size() > 2) {
				out.println("<div class='podium-step third'>");
				out.println("<div class='username'>" + usernames.get(2) + "</div>");
				out.println("<div>" + scores.get(2) + " pts</div>");
				out.println("</div>");
			}

			out.println("</div>"); 
			out.println("</div>"); 

			out.println("</body>");
			out.println("</html>");

		} catch (SQLException e) {
			throw new ServletException("Error fetching leaderboard", e);
		}
	}
}
