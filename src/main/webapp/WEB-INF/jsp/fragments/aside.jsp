<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ishop" tagdir="/WEB-INF/tags" %>

<div class="visible-xs-block xs-option-container">
    <a class="pull-right" data-toggle="collapse" href="#productCatalog">Product catalog<span class="caret"></span></a>
    <a data-toggle="collapse" href="#findProducts">Find products<span class="caret"></span></a>
</div>
<!-- Search form -->
<form class="search" action="<c:url value="/search"/>">
    <div id="findProducts" class="panel panel-success collapse">
        <div class="panel-heading">Find products</div>
        <div class="panel-body">
            <div class="input-group">
                <input type="text" name="query" class="form-control" placeholder="Search query"
                       value="${requestScope.searchForm.query}">
                <span class="input-group-btn">
                <a id="goSearch" class="btn btn-default">Go!</a>
              </span>
            </div>
            <div class="more-options">
                <a data-toggle="collapse" href="#searchOptions">More filters<span class="caret"></span></a>
            </div>
        </div>
        <div id="searchOptions"
             class="collapse ${requestScope.searchForm.categoriesNotEmpty or requestScope.searchForm.producersNotEmpty ? 'in' : ''}">
            <ishop:category-filter categories="${applicationScope.CATEGORY_LIST}"
                                   searchForm="${requestScope.searchForm}"/>
            <ishop:producer-filter producers="${applicationScope.PRODUCER_LIST}"
                                   searchForm="${requestScope.searchForm}"/>
        </div>
    </div>
</form>
<!-- Categories-->
<div id="productCatalog" class="panel panel-success collapse">
    <div class="panel-heading">Product catalog</div>
    <div class="list-group">
        <c:forEach var="category" items="${applicationScope.CATEGORY_LIST}">
            <a href="<c:url value="/products${category.url}"/>"
               class="list-group-item ${requestScope.selectedCategoryUrl == category.url ? 'active' : ''}">
                <span class="badge">${category.productCount}</span> ${category.name}
            </a>
        </c:forEach>
    </div>
</div>

