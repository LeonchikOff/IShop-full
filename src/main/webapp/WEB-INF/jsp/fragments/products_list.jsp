<%@ page pageEncoding="UTF-8" trimDirectiveWhitespaces="true" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<c:forEach var="product" items="${requestScope.productsList}">
    <div class="col-xs-12 col-sm-6 col-md-4 col-lg-3 col-xlg-2">
        <div id="product${product.id}" class="panel panel-default product">
            <div class="panel-body">
                <div class="thumbnail">
                    <img src="<c:url value='${product.imageLink}'/>" alt="${product.name}">
                    <div class="desc">
                        <div class="cell">
                            <p>
                                <span class="title">Details</span> ${product.description}
                            </p>
                        </div>
                    </div>
                </div>
                <h4 class="name">${product.name}</h4>
                <div class="code">Code: ${product.id}</div>
                <div class="price">${product.price}₽</div>
                <a class="btn btn-primary pull-right buy-btn" data-id-product="${product.id}">Buy</a>
                <ul class="list-group">
                    <li class="list-group-item"><small>Category:</small>
                        <span class="category">${product.categoryName}</span>
                    </li>
                    <li class="list-group-item"><small>Producer:</small>
                        <span class="producer">${product.producerName}</span>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</c:forEach>

