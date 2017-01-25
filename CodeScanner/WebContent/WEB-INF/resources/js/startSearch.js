// Initialize a search
var startSearch = function(repoUrl) {
	console.log(repoUrl);
	var client = new HttpClient();
	client.post(serverBaseUrl + 'api/start/submitRepoUrl?searchUrl=' + repoUrl, function(response) {
		response = JSON.parse(response);
		if (response.success)
			window.location = 'results.html?session=' + response.sessionID + '&match=50&length=2';
		else {
			document.getElementById('error-msg').innerHTML = response.errorMsg;
		}
	});
}