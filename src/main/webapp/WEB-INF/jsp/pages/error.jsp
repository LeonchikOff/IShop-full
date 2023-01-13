<%@ page pageEncoding="UTF-8" trimDirectiveWhitespaces="true" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ishop" tagdir="/WEB-INF/tags" %>

<div class="alert alert-danger hidden-print" role="alert">
    <h1>Code: ${requestScope.statusCode}</h1>
    <c:choose>
        <c:when test="${requestScope.statusCode == 403}">You don't have permissions to view this resource</c:when>
        <c:when test="${requestScope.statusCode == 404}">Requested resource not found</c:when>
        <c:otherwise>Can't process this request. Try again later...</c:otherwise>
    </c:choose>
</div>

