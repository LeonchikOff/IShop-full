<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="u" uri="/WEB-INF/tags.tld" %>

<%@ attribute name="classes" required="true" type="java.lang.String" %>

<form action="<c:url value="/sign_in"/>" method="post">
    <c:if test="${
    fn:startsWith(requestScope.CURRENT_REQUEST_URL, '/search')
    or fn:startsWith(requestScope.CURRENT_REQUEST_URL, '/products')
    or requestScope.CURRENT_REQUEST_URL == '/shopping-cart'}">
        <u:URLEncodeTag url="${requestScope.CURRENT_REQUEST_URL}" var="encodedURL"/>
        <input type="hidden" name="target" value="${encodedURL}">
    </c:if>
    <button type="submit" class="btn btn-primary ${classes}">
        <i class="fa fa-facebook-official" aria-hidden="true"></i> Sign in
    </button>
</form>

