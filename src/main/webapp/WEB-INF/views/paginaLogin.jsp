<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Loggati</title>
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
			<c:if test="${message != null && !message.isEmpty()}">
				<p style="color:orange;">${message}</p>
			</c:if>
			<form:form action="/login" method="post" modelAttribute="utente">
				Username  <form:input path="username"/>
				<br>
				Password  <form:input path="password" type="password"/>
				<br>
				<form:button>Login</form:button>
			</form:form>
			<c:if test="${loginError != null && !loginError.isEmpty()}">
				<p>${loginError}</p>
			</c:if>		
		</div>
	</body>
</html>