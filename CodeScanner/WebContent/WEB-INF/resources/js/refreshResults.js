/* Global variables */

// HttpClient object used for sending HTTP requests to REST API
var client = new HttpClient();
// Keeps track of how many rows have been added to the table
var currNumResults = 0;
// Match rating threshold at which results will not be displayed
var matchThreshold = parseInt(getParameterByName('match').toString());
if (matchThreshold == null) matchThreshold = 50; // default to 50
// Match length threshold at which results will not be displayed
var lengthThreshold = parseInt(getParameterByName('length').toString());
if (lengthThreshold == null || lengthThreshold < 2) lengthThreshold = 2; // default to 2 if null or less than 2



// Set threshold slider and number values on page load or when updated
document.getElementById('match-slider').value = matchThreshold;
document.getElementById('match-out').innerHTML = matchThreshold;
document.getElementById('length-text').value = lengthThreshold;
var setSliderVal = function(val) {
	document.getElementById('match-out').innerHTML = val;
};

// Update filter settings
var updateFilters = function() {
	var url = location.href;
	url = urlAddParameter(url, 'match', document.getElementById('match-slider').value);
	url = urlAddParameter(url, 'length', document.getElementById('length-text').value);
	window.location.href = url;
};

// Get a list of results and push them to the results page
var getResults = function() {
	client.get(serverBaseUrl+'api/results/getResults?sessionID=' + getParameterByName('session').toString(), function(response) {
		var results = JSON.parse(response);
		if (results.success)
			updateResultsPage(results);
		else {
			document.getElementById('search-progress-info').innerHTML =
				'An error occurred while retrieving search results. ' +
				'Please refresh the page to try again.';
		}
	});
};

// Populate results page with results object obtained from REST API call
var updateResultsPage = function(results) {
	// For each result, construct a table row in HTML for it
	for (var i = currNumResults; i < results.numResults; i++) {
		var curr = results.results[i];
		if (curr.matchRating*100 > matchThreshold && curr.length >= lengthThreshold) { // only add if not filtered by threshold
			var resultsTable = document.getElementById('results-table');
			var row = resultsTable.insertRow(resultsTable.rows.length); // insert at bottom of table
			row.id = 'result-table-row-' + i;
			if (resultsTable.rows.length % 2 == 0)
				row.style = 'background-color: rgba(255, 255, 255, 0.6);';
			row.insertCell(0).innerHTML = '<td><a onclick="getSnippets('+curr.uuid+')" class="result-file" title="Click to see relevant source code">'+curr.sourceFileName+'</a></td>\n';
			row.insertCell(1).innerHTML = '<td><a href="'+curr.candidateUrl+'" target="_blank" class="result-file">'+curr.candidateFileName.replace(/%2F/g, '/')+'</a></td>\n'
			row.insertCell(2).innerHTML = '<td>'+curr.sourceStartLine+'</td>\n';
			row.insertCell(3).innerHTML = '<td>'+curr.candidateStartLine+'</td>\n';
			row.insertCell(4).innerHTML = '<td>'+curr.length+'</td>\n';
			row.insertCell(5).innerHTML = '<td>'+Math.round((curr.matchRating*100)*100)/100+'%</td>\n'
			// Constructs a call to the function below which toggles between OK and not;
			var okCell = row.insertCell(6);
			okCell.innerHTML = curr.isOkay ?
				'<a id="result-ok-'+i+'" onclick="toggleOK('+curr.uuid+', '+i+', true)" style="font-size:1.8em;cursor:pointer;color:#00AA00;">&#10004;</a>\n' : // insert green check mark
				'<a id="result-ok-'+i+'" onclick="toggleOK('+curr.uuid+', '+i+', false)" style="font-size:1.8em;cursor:pointer;color:#AA0000;">&#10006;</a>\n'; // or, insert red cross mark
		}
		currNumResults = results.numResults;
	}
	
	// Fully iterate through results again to update OK marks
	for (var i = 0; i < results.numResults; i++) {
		var cell = document.getElementById('result-table-row-' + i);
		if (cell != null)
			cell = cell.getElementsByTagName("td")[6]; // get OK cell for current row
		var curr = results.results[i];
		if (curr.matchRating*100 > matchThreshold && curr.length >= lengthThreshold) { // only update if not filtered by threshold
			cell.innerHTML = curr.isOkay ?
				'<a id="result-ok-'+i+'" onclick="toggleOK('+curr.uuid+', '+i+', true)" style="font-size:1.8em;cursor:pointer;color:#00AA00;">&#10004;</a>\n' : // insert green check mark
				'<a id="result-ok-'+i+'" onclick="toggleOK('+curr.uuid+', '+i+', false)" style="font-size:1.8em;cursor:pointer;color:#AA0000;">&#10006;</a>\n'; // or, insert red cross mark
		}
	}
};

// Toggle whether a result is marked OK or not
var toggleOK = function(id, resultRow, value) {
	client.put(serverBaseUrl+'api/results/updateResults?resultID=' + id, function(response) {
		var resultCell = document.getElementById('result-ok-'+resultRow);
		if (JSON.parse(response).success) {
			if (value) {
				resultCell.innerHTML = '&#10006;';
				resultCell.style = "color:#AA0000;font-size:1.8em;cursor:pointer;";
				resultCell.setAttribute("onclick", "toggleOK("+id+", "+resultRow+", false)");
			} else {
				resultCell.innerHTML = '&#10004;';
				resultCell.style = "color:#00AA00;font-size:1.8em;cursor:pointer;";
				resultCell.setAttribute("onclick", "toggleOK("+id+", "+resultRow+", true)");
			}
		}
	});
};

// Get corresponding code snippets for a particular result
var getSnippets = function(id) {
	client.get(serverBaseUrl+'api/results/getSnippet?resultID=' + id, function(response) {
		var snippets = JSON.parse(response);
		if (snippets.success) {
			document.getElementById('source-text').innerHTML = snippets.sourceSnippet;
			document.getElementById('candidate-text').innerHTML = snippets.candidateSnippet;
		}
	});
};

// Get corresponding code snippets for a particular result
var getSnippets = function(id) {
	client.get(serverBaseUrl+'api/results/getSnippet?resultID=' + id, function(response) {
		var snippets = JSON.parse(response);
		if (snippets.success) {
			document.getElementById('source-text').innerHTML = snippets.sourceSnippet;
			document.getElementById('candidate-text').innerHTML = snippets.candidateSnippet;
		}
	});
}

// "Main" function which sets an interval on all getter functions
var setUp = function() {
	getResults();
	setInterval(getResults, 5000);
}();