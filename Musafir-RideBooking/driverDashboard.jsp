<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Driver Dashboard - Musafir</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="css/style.css">
</head>
<body>
<%@ include file="navbar.jsp" %>

<div class="container mt-5 d-flex justify-content-center">
  <div class="card shadow-lg border-0 rounded col-md-6 p-4 text-center">
    <h2 class="text-danger fw-bold mb-4">Driver Dashboard</h2>
    <button id="checkBalanceBtn" class="btn btn-danger btn-lg w-100 mb-3">Check My Balance</button>
    <!-- Container where balance or messages will show -->
    <div id="balanceMessage"></div>
  </div>
</div>

<%@ include file="footer.jsp" %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
document.getElementById('checkBalanceBtn').onclick = function() {
    fetch('driverDashboard')
    .then(response => response.text())
    .then(html => {
        document.getElementById('balanceMessage').innerHTML = html;
    })
    .catch(error => {
        document.getElementById('balanceMessage').innerHTML = "<div class='text-danger'>Error fetching balance.</div>";
    });
};
</script>
</body>
</html>
