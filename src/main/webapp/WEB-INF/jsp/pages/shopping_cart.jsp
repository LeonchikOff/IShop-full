<%@ page pageEncoding="UTF-8" trimDirectiveWhitespaces="true" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ishop" tagdir="/WEB-INF/tags" %>

<div id="shoppingCart">
    <c:if test="${sessionScope.CURRENT_ACCOUNT == null}">
        <div class="alert alert-warning hidden-print" role="alert">To make order, please sign in</div>
    </c:if>
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>Product</th>
            <th>Price</th>
            <th>Count</th>
            <th class="hidden">Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="shoppingCartItem" items="${sessionScope.CURRENT_SHOPPING_CART.shoppingCartItems}">
            <tr id="product${shoppingCartItem.product.id}" class="product-item">
                <td class="text-center">
                    <img class="small"
                         src="<c:url value="${shoppingCartItem.product.imageLink}"/>"
                         alt="${shoppingCartItem.product.name}">
                    <br>${shoppingCartItem.product.name}
                </td>
                <td class="price">${shoppingCartItem.product.price}</td>
                <td class="count">${shoppingCartItem.quantityOfUniqueProduct}</td>
                <td class="hidden-print">
                    <c:choose>
                        <c:when test="${shoppingCartItem.quantityOfUniqueProduct > 1}">
                            <a class="btn btn-danger remove-product" data-id-product="${shoppingCartItem.product.id}"
                               data-count="1">Remove one</a><br><br>
                            <a class="btn btn-danger remove-product all"
                               data-id-product="${shoppingCartItem.product.id}"
                               data-count="${shoppingCartItem.quantityOfUniqueProduct}">Remove all</a>
                        </c:when>
                        <c:otherwise>
                            <a class="btn btn-danger remove-product" data-id-product="${shoppingCartItem.product.id}"
                               data-count="1">Remove one</a>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="2" class="text-right"><strong>Total cost:</strong></td>
            <td colspan="2" class="total-cost">${sessionScope.CURRENT_SHOPPING_CART.totalCostOfAllProducts}₽</td>
        </tr>
        </tbody>
    </table>
    <div class="row hidden-print">
        <div class="col-md-4 col-md-offset-4 col-lg-2 col-lg-offset-5">
            <c:choose>
                <c:when test="${sessionScope.CURRENT_ACCOUNT != null}">
                    <a href="javascript:void(0);" class="post-request btn btn-primary btn-block"
                       data-url="<c:url value="/order"/>">
                        Make order
                    </a>
                </c:when>
                <c:otherwise>
                    <ishop:sign_in classes="btn-block"/>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>


<%--    <table class="table table-bordered">--%>
<%--        <thead>--%>
<%--        <tr>--%>
<%--            <th>Product</th>--%>
<%--            <th>Price</th>--%>
<%--            <th>Count</th>--%>
<%--            <th class="hidden-print">Action</th>--%>
<%--        </tr>--%>
<%--        </thead>--%>
<%--        <tbody>--%>
<%--        <c:forEach var="orderItem" items="${sessionScope.CURRENT_SHOPPING_CART.shoppingCartItems}">--%>
<%--            <tr id="product${orderItem.product.id}" class="product-item">--%>
<%--                <td class="text-center">--%>
<%--                    <img class="small" src="<c:url value="${orderItem.product.imageLink}"/>" alt="${orderItem.product.name}">--%>
<%--                    <br>${orderItem.product.name}--%>
<%--                </td>--%>
<%--                <td class="price">${orderItem.product.price}</td>--%>
<%--                <td class="count">${orderItem.quantityOfUniqueProduct}</td>--%>
<%--                <td class="hidden-print">--%>
<%--                    <c:choose>--%>
<%--                        <c:when test="${orderItem.quantityOfUniqueProduct > 1}">--%>
<%--                            <a class="btn btn-danger remove-product" data-id-product="${orderItem.product.id}"--%>
<%--                               data-count="1">Remove one</a><br><br>--%>
<%--                            <a class="btn btn-danger remove-product all" data-id-product="${orderItem.product.id}"--%>
<%--                               data-count="${orderItem.quantityOfUniqueProduct}">Remove all</a>--%>
<%--                        </c:when>--%>
<%--                        <c:otherwise>--%>
<%--                            <a class="btn btn-danger remove-product" data-id-product="${orderItem.product.id}"--%>
<%--                               data-count="1">Remove one</a>--%>
<%--                        </c:otherwise>--%>
<%--                    </c:choose>--%>
<%--                </td>--%>
<%--            </tr>--%>
<%--        </c:forEach>--%>
<%--        <tr>--%>
<%--            <td colspan="2" class="text-right"><strong>Total cost:</strong></td>--%>
<%--            <td colspan="2" class="total-cost">${sessionScope.CURRENT_SHOPPING_CART.totalCostOfAllProducts}₽</td>--%>
<%--        </tr>--%>
<%--        </tbody>--%>
<%--    </table>--%>
