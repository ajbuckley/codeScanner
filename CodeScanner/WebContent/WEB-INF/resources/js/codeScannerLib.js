// Base URL for accessing the REST API
var serverBaseUrl = 'http://localhost:8080/';

// Used for fetching URL parameters and returning as strings
// Source: http://stackoverflow.com/a/901144
// NOTE: If there are duplicate parameters, this function will
//       ONLY return the first parameter.
var getParameterByName = function(name, url) {
	if (!url)
    	url = window.location.href;
	name = name.replace(/[\[\]]/g, "\\$&");
	var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
		results = regex.exec(url);
	if (!results) return null;
	if (!results[2]) return '';
	return decodeURIComponent(results[2].replace(/\+/g, " "));
}

// Refreshes the page while appending a parameter to the current URL
var urlAddParameter = function(url, param, value){
    var hash       = {};
    var parser     = document.createElement('a');
    parser.href    = url;
    var parameters = parser.search.split(/\?|&/);
    
    for(var i=0; i < parameters.length; i++) {
        if(!parameters[i])
            continue;
        var ary      = parameters[i].split('=');
        hash[ary[0]] = ary[1];
    }
    
    hash[param] = value;
    var list = [];  
    Object.keys(hash).forEach(function (key) {
        list.push(key + '=' + hash[key]);
    });
    parser.search = '?' + list.join('&');
    return parser.href;
}

// Client class for making HTTP requests
var HttpClient = function() {
    this.get = function(url, callback) {
        var httpRequest = new XMLHttpRequest();
        httpRequest.onreadystatechange = function() { 
            if (httpRequest.readyState == 4 && httpRequest.status == 200)
                callback(httpRequest.responseText);
        }

        httpRequest.open('GET', url, true);            
        httpRequest.send(null);
    }
    
    this.post = function(url, callback) {
    	var httpRequest = new XMLHttpRequest();
        httpRequest.onreadystatechange = function() { 
            if (httpRequest.readyState == 4 && httpRequest.status == 200)
                callback(httpRequest.responseText);
        }

        httpRequest.open('POST', url, true);            
        httpRequest.send(null);
    }
    
    this.put = function(url, callback) {
    	var httpRequest = new XMLHttpRequest();
        httpRequest.onreadystatechange = function() { 
            if (httpRequest.readyState == 4 && httpRequest.status == 200)
                callback(httpRequest.responseText);
        }

        httpRequest.open('PUT', url, true);            
        httpRequest.send(null);
    }
}