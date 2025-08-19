<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
    // Get user info from session
    String user = (String) session.getAttribute("user"); 
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Musafir</title>
<!-- Bootstrap CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-white shadow rounded">
  <div class="container-fluid">
    <a class="navbar-brand fw-bold text-danger" style="font-family:cursive; font-size:1.5rem;" href="index.jsp">
        Musafir
    </a>
    <div class="collapse navbar-collapse justify-content-end">
      <ul class="navbar-nav align-items-center">
        <li class="nav-item"><a class="nav-link text-dark fw-semibold" href="ride.jsp">Ride</a></li>
        <li class="nav-item"><a class="nav-link text-dark fw-semibold" href="driver.jsp">Drive</a></li>

        <%
            if (user == null) {
                // User not logged in -> show Login & Signup
        %>
                <li class="nav-item mx-2"><a class="btn btn-outline-danger" href="login.jsp">Login</a></li>
                <li class="nav-item"><a class="btn btn-danger" href="signup.jsp">Signup</a></li>
        <%
            } else {
                // User logged in -> show Logout and Welcome
        %>
                <li class="nav-item me-3">
                    <span class="fw-semibold text-dark">Welcome, <%= user %></span>
                </li>
                <li class="nav-item mx-2"><a class="btn btn-outline-danger" href="logout.jsp">Logout</a></li>
        <%
            }
        %>
      </ul>
    </div>
  </div>
</nav>

<!-- Bootstrap JS (for navbar toggle on mobile) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
