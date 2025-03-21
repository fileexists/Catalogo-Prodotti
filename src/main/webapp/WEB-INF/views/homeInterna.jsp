<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Dashboard</title>
	    <style>
	        .center {
	            display: flex;
	            justify-content: center;
	            align-items: center;
	            height: 100vh;
	            flex-direction: column;
	            text-align: center;
	        }
    	</style>		
	</head>
	<body>
		<jsp:include page="./menu.jsp"></jsp:include>
		<div class="center">
			<p>Benvenuto ${utenteSessione.username}</p>
		</div>
	</body>
</html>