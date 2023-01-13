<%--<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>--%>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
<%--<%@ attribute name="orderItemsList" required="true" type="java.util.Collection" %>--%>
<%--<%@ attribute name="totalCost" required="true" type="java.lang.Number" %>--%>
<%--<%@ attribute name="showActionColumn" required="true" type="java.lang.Boolean" %>--%>
<%--<table class="table table-bordered">--%>
<%--    <thead>--%>
<%--    <tr>--%>
<%--        <th>Product</th>--%>
<%--        <th>Price</th>--%>
<%--        <th>Count</th>--%>
<%--        <c:if test="${showActionColumn}">--%>
<%--            <th class="hidden-print">Action</th>--%>
<%--        </c:if>--%>
<%--    </tr>--%>
<%--    </thead>--%>
<%--    <tbody>--%>
<%--    <c:forEach var="orderItemsList" items="${orderItemsList}">--%>
<%--        <tr id="product${orderItemsList.product.id}" class="product-item">--%>
<%--            <td class="text-center">--%>
<%--                <img class="small" src="<c:url value="${orderItemsList.product.imageLink}"/>"--%>
<%--                     alt="${orderItemsList.product.name}">--%>
<%--                <br>${orderItemsList.product.name}--%>
<%--            </td>--%>
<%--            <td class="price">${orderItemsList.product.price}₽</td>--%>
<%--            <td class="count">${orderItemsList.product.quantityOfUniqueProduct}</td>--%>
<%--            <c:if test="${showActionColumn}">--%>
<%--                <td class="hidden-print">--%>
<%--                    <c:choose>--%>
<%--                        <c:when test="${orderItemsList.product.quantityOfUniqueProduct > 1}">--%>
<%--                            <a class="btn btn-danger remove-product" data-id-product="${orderItemsList.product.id}"--%>
<%--                               data-count="1">Remove one</a><br><br>--%>
<%--                            <a class="btn btn-danger remove-product all" data-id-product="${orderItemsList.product.id}"--%>
<%--                               data-count="${orderItemsList.product.quantityOfUniqueProduct}">Remove all</a>--%>
<%--                        </c:when>--%>
<%--                        <c:otherwise>--%>
<%--                            <a class="btn btn-danger remove-product" data-id-product="${orderItemsList.product.id}"--%>
<%--                               data-count="1">Remove one</a>--%>
<%--                        </c:otherwise>--%>
<%--                    </c:choose>--%>
<%--                </td>--%>
<%--            </c:if>--%>
<%--        </tr>--%>
<%--    </c:forEach>--%>
<%--    <tr>--%>
<%--        <td colspan="2" class="text-right"><strong>Total cost:</strong></td>--%>
<%--        <td colspan="${showActionColumn ? 2 : 1}" class="total-cost">${totalCost}₽</td>--%>
<%--    </tr>--%>
<%--    </tbody>--%>
<%--</table>--%>
