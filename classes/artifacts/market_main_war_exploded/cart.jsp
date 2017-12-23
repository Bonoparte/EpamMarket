<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User's cart</title>
</head>
<body>
<div>
    <table style="table-layout: auto" border="2" align="center" width="600">
        <tr>
            <th>Goods ID</th>
            <th>Amount</th>
        </tr>
        <c:forEach var="cart" items="${userCart}">
            <tr>
                <td colspan="2"><c:out value='${cart.goodId}'/>
                    <form action="cartwithdelete" method="post">
                    <input type="hidden" name="goodsId" value="${cart.goodId}">
                    <input type="submit" value="Delete">
                </form></td>
                <td><c:out value='${cart.amount}'/>
            </tr>
        </c:forEach>
    </table>
</div>

<form action="price_list" method="get">
    <input type="submit" value="go back to price list"/>
</form>
</body>
</html>
