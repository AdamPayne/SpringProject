<%@include file="taglib_includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><spring:message code="App.Title"></spring:message></title>
<script type="text/javascript" src="js/contacts.js"></script>
</head>
<body style="font-family: Verdana;">
	<div class="center">
		<p>
			<c:if test="${! empty loggedInUser }">Welcome ${loggedInUser}
		
		</p>
		<input type="button" value="Log Out"
			onclick="javascript:go('logOut.do');" class="right" /> <input
			type="button" value="Back"
			onclick="javascript:go('searchContacts.do');" class="back"/>
		<table style="border-collapse: collapse;" border="1"
			bordercolor="#778899" width="500">
			<tr bgcolor="purple">
				<th>Latest Searches</th>
			</tr>
			<c:if test="${empty SEARCH_QUERIES_RESULTS_KEY}">
				<tr>
					<td colspan="4">No Results found</td>
				</tr>
			</c:if>
			<c:if test="${! empty SEARCH_QUERIES_RESULTS_KEY}">
				<c:forEach var="searchQ" items="${SEARCH_QUERIES_RESULTS_KEY}">
					<tr>
						<td><a href="searchContacts.do?search=${searchQ.query}"><c:out
									value="${searchQ.query}"></c:out></a></td>
					</tr>
				</c:forEach>
			</c:if>
		</table>
		</c:if>
	</div>
</body>
</html>