<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
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
				<p>${message}</p>
			</c:if>
		</div>
	</body>
</html>