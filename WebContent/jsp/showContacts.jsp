<%@include file="taglib_includes.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title><spring:message code="App.Title"></spring:message></title>
<script type="text/javascript" src="js/contacts.js"></script>
</head>
<body style="font-family: Verdana; text-align: center;">
	<div class="center">
	<input type="button" value="Log Out"
						onclick="javascript:go('logOut.do');" class="right"/>
		<p>
			<c:if test="${! empty loggedInUser }">Welcome ${loggedInUser}
		</p>
		<form action="searchContacts.do" method="get">
			<table border="0" bordercolor="#006699" width="500">
				<tr>
					<td><input type="text" name="search" placeholder="Search"/></td>
					<td><input type="button" value="New Contact"
						onclick="javascript:go('saveContact.do');" /></td>
					<td><input type="button" value="Latest Searches"
						onclick="javascript:go('viewSearch.do');" />
				</tr>
			</table>
		</form>
		
		<table style="border-collapse: collapse;" border="1"
			bordercolor="#778899" width="500">
			<tr bgcolor="purple">
				<th>Id</th>
				<th>Name</th>
				<th>Address</th>
				<th>Mobile</th>
				<th></th>
				<th></th>
			</tr>
			<c:if test="${empty SEARCH_CONTACTS_RESULTS_KEY}">
				<tr>
					<td colspan="4">No Results found</td>
				</tr>
			</c:if>
			<c:if test="${! empty SEARCH_CONTACTS_RESULTS_KEY}">
				<c:forEach var="contact" items="${SEARCH_CONTACTS_RESULTS_KEY}">
					<tr>
						<td><c:out value="${contact.id}"></c:out></td>
						<td><c:out value="${contact.name}"></c:out></td>
						<td><c:out value="${contact.address}"></c:out></td>
						<td><c:out value="${contact.mobile}"></c:out></td>
						<td>&nbsp;<a href="updateContact.do?id=${contact.id}">Edit</a>
							&nbsp;&nbsp;<a
							href="javascript:deleteContact('deleteContact.do?id=${contact.id}');">Delete</a>
						</td>
						<td>&nbsp;&nbsp;<a href="downVcard.do?id=${contact.id}">vCard</a>
						</td>
					</tr>
				</c:forEach>
			</c:if>
		</table>
		<input type="button" value="Export All"
			onclick="javascript:go('export.do');" class="exp"/>
		</c:if>
	</div>
</body>
</html>