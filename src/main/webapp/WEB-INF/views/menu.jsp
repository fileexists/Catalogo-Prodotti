<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>      
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		<nav>
		    <ul>
		    	<c:if test="${utenteSessione != null && utenteSessione.loggato == true}">
			    	<li><a href="/ricercaProdotti">Ricerca uno specifico prodotto</a></li>
			    	<c:if test="${utenteSessione.ruolo.nome.equals('admin')}">
			    		<li><a href="/creaProdotto">Crea un prodotto</a></li>
		    		</c:if>
			    	<li><a href="/logout">Effettua Logout</a></li>
		    	</c:if>
		    </ul>
		</nav>
	</body>
</html>