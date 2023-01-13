<%@ page pageEncoding="UTF-8" trimDirectiveWhitespaces="true" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="ishop" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--Main dynamic page--%>
<div id="productsList" data-page-count="${requestScope.countPage}" data-page-current-number="1">
    <div class="row">
        <jsp:include page="../fragments/products_list.jsp"/>
    </div>
    <c:if test="${requestScope.countPage > 1}">
        <div class="text-center hidden-print">
            <a id="loadMore" class="btn btn-success">Load More Products</a>
        </div>
    </c:if>
</div>
<ishop:modal-add-product-popup/>


