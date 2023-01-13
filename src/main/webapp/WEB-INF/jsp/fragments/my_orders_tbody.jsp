<%@ page pageEncoding="UTF-8" trimDirectiveWhitespaces="true" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:forEach var="order" items="${requestScope.listMyOrders}">
    <tr class="item">
        <td><a href="<c:url value="/order?id=${order.id}"/>">Order #${order.id}</a></td>
        <td><fmt:formatDate value="${order.dateOfCreated}" pattern="yyyy-MM-dd HH:mm"/></td>
    </tr>
</c:forEach>

