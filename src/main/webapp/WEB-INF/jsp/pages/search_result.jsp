<%@ page pageEncoding="UTF-8" trimDirectiveWhitespaces="true" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="alert alert-info">
    <p>Found <strong>${requestScope.productCount} products</strong></p>
</div>
<jsp:include page="products.jsp"/>
