<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>          
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Risultati della tua ricerca</title>
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
            <c:if test="${prodotti.size() > 0}">
                <table border="2">
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>Prezzo</th>
                        <c:if test="${utenteSessione.ruolo.nome.equals('admin')}">
                            <th>Modifica Completa</th>
                            <th>Modifica [Nome]</th>
                            <th>Modifica [Prezzo]</th>
                            <th>Elimina</th>
                        </c:if>
                    </tr>
                    <c:forEach var="prodotto" items="${prodotti}">
                        <tr>
                            <td>${prodotto.id}</td>
                            <td>${prodotto.nome}</td>
                            <td>${prodotto.prezzo}</td>
                            <c:if test="${utenteSessione.ruolo.nome.equals('admin')}">
                                <td>
                                    <form action="/totalUpdate" method="post">
                                        <input type="hidden" name="id" value="${prodotto.id}"/>
                                        <input type="text" name="nome" value="${prodotto.nome}" placeholder="Nome prodotto"/>
                                        <input type="number" name="prezzo" value="${prodotto.prezzo}" step="any" placeholder="Prezzo prodotto"/>
                                        <button type="submit">Modifica Completa</button>
                                    </form>
								</td>
								<td>
                                    <form action="/partialUpdate" method="post">
                                        <input type="hidden" name="id" value="${prodotto.id}"/>
                                        <input type="text" name="nome" value="${prodotto.nome}" placeholder="Nome prodotto"/>
                                        <button type="submit">Modifica Nome</button>
                                    </form>
								</td>
								<td>
                                    <form action="/partialUpdate" method="post">
                                        <input type="hidden" name="id" value="${prodotto.id}"/>
                                        <input type="number" name="prezzo" value="${prodotto.prezzo}" step="any" placeholder="Prezzo prodotto"/>
                                        <button type="submit">Modifica Prezzo</button>
                                    </form>
								</td>
								<td>
                                    <form action="/delete" method="post">
                                        <input type="hidden" name="id" value="${prodotto.id}"/>
                                        <button type="submit">Elimina</button>
                                    </form>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>
        </div>
    </body>
</html>
