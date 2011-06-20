<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.apache.lucene.document.Document"%>
<%@page import="ro.calin.Constants"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search Engine</title>

<style type="text/css">
	@IMPORT url("css/styles.css");
</style>

<script type="text/javascript" src="js/dojo.js">
</script>

<script type="text/javascript">

var currentPage = 1;
var query = null;
var requesting = false;

function makeQuery() {
	query = dojo.byId('searchBox').value;
	
	requestResults(query, 1);
}

function requestResults(query, page) {
	if(requesting)
		return;

	requesting = true;
	
	currentPage = page;
	
	var requestUrl = '<%=request.getContextPath()%>/QueryHandler?<%=Constants.REQUEST_PARAMETER_QUERY%>=' + 
						query + '&<%=Constants.REQUEST_PARAMETER_PAGE%>=' + page;

	dojo.xhrGet({
		url: requestUrl,
		handleAs: "json",
		load: function(data) {
			requesting = false;
			populateResults(data);
		},
		error: function(data, args){
			requesting = false;
			console.warn("error!");
			console.log(data);
			console.log(args);
		}
	});	
}

function populateResults(data) {
	var resultListContainer = dojo.byId("resultList");
	var pagetTopContainer = dojo.byId("pagerTop");
	var pagetBottomContainer = dojo.byId("pagerBottom");

	totalPages = data['<%=Constants.JSON_TOTAL_PAGES%>'];

	if(totalPages > 0) {
		buildPager(pagetTopContainer, currentPage, totalPages);
		buildResultList(resultListContainer, data['<%=Constants.JSON_RESULTS%>']);
		buildPager(pagetBottomContainer, currentPage, totalPages);
	} else {
		pagetTopContainer.innerHTML = "";
		resultListContainer.innerHTML = "Nu exista nici un rezultat."
		pagetBottomContainer.innerHTML = "";
	}
	
	
}

function buildPager(container, currentPage, lastPage) {
	var generatedContent = "";

	if(currentPage == 1) {
		generatedContent += "<span class='AtStart'>«Inapoi</span>";
	} else {
		generatedContent += "<a class='Prev' href='#' onclick=\"requestResults('" + 
							query + "', " + (currentPage - 1) + ")\">«Inapoi</a>";
	}

	var first = currentPage - 3;
	var last = currentPage + 3;

	if(first < 1) {
		var possibleLast = (last - first) + 1;
		last = possibleLast <= lastPage? possibleLast : lastPage;
		first = 1;
	} else if(last > lastPage) {
		var possibleFirst = (first + (lastPage - last));
		first = possibleFirst >= 1? possibleFirst : 1;
		last = lastPage;
	}

	if(first > 1) {
		generatedContent += "<span class='break'>...</span>";
	}
	
	for(var i = first; i <= last; i++) {
		if(i == currentPage) {
			generatedContent += "<span class='this-page'>" + i + "</span>";
		} else {
			generatedContent += "<a href='#' onclick=\"requestResults('" + 
								query + "', " + i + ")\">" + i + "</a>";
		}
	}

	if(last < lastPage) {
		generatedContent += "<span class='break'>...</span>";
	}

	if(currentPage == lastPage) {
		generatedContent += "<span class='AtEnd'>Inainte»</span>";
	} else {
		generatedContent += "<a class='Next' href='#' onclick=\"requestResults('" + 
							query + "', " + (currentPage + 1) + ")\">Inainte»</a>";
	}

	container.innerHTML = generatedContent;
}

function buildResultList(container, results) {
	var generatedContent = "<ul>";

	for(var ind in results) {
		var result = results[ind];

		generatedContent += "<li>";

		generatedContent += "<div class='title'>";
		generatedContent += "<a href='" + result['<%=Constants.JSON_RESULT_URL%>'] + 
							"'>" + result['<%=Constants.JSON_RESULT_TITLE%>'] + "</a>";
		generatedContent += "</div>";
		
		generatedContent += "<div class='summary'>";
		generatedContent += result['<%=Constants.JSON_RESULT_CONTENT_SEQUENCE%>'];
		generatedContent += "</div>";

		generatedContent += "<cite>";
		generatedContent += result['<%=Constants.JSON_RESULT_URL%>'];
		generatedContent += "</cite>";
		
		generatedContent += "</li>";
	} 
	
	generatedContent += "</ul>";

	container.innerHTML = generatedContent;
}

</script>

</head>
<body>
	<div id="searchQuery">
		<input id="searchBox" type="text">
		<input id="searchButton" type="button" value="Cauta..." onclick="makeQuery();">
	</div>
	
	<div id="searchResults">
		<div class="Pages">
			<div id="pagerTop" class="Paginator"></div>
		</div>
		<div id="resultList"></div>
		<div class="Pages">
			<div id="pagerBottom" class="Paginator"></div>
		</div>
	</div>
</body>
</html> 