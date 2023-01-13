<%@ page pageEncoding="UTF-8" trimDirectiveWhitespaces="true" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ishop" tagdir="/WEB-INF/tags" %>

<div id="order">
    <c:if test="${requestScope.CURRENT_MESSAGE != null}">
        <div class="alert alert-success" role="alert">
                ${requestScope.CURRENT_MESSAGE}
        </div>
    </c:if>
    <h4 class="text-center">Order #${requestScope.order.id}</h4>
    <hr>
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>Product</th>
            <th>Price</th>
            <th>Count</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="orderItem" items="${requestScope.order.orderItemsList}">
            <tr id="product${orderItem.product.id}" class="product-item">
                <td class="text-center">
                    <img class="small" height="80px"
                         src="<c:url value="${orderItem.product.imageLink}"/>"
                         alt="${orderItem.product.name}">
                    <br>${orderItem.product.name}
                </td>
                <td class="price">${orderItem.product.price}</td>
                <td class="count">${orderItem.count}</td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="2" class="text-right"><strong>Total cost:</strong></td>
            <td colspan="2" class="total-cost">${requestScope.orderTotalCost}₽</td>
        </tr>
        </tbody>
    </table>
    <div class="row hidden-print">
        <div class="col-md-4 col-md-offset-4 col-lg-2 col-lg-offset-5">
            <a href="<c:url value="/my_orders"/>" class="btn btn-primary btn-block">Go to my orders</a>
        </div>
    </div>
</div>