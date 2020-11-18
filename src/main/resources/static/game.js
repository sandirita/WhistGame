
function endGame() {
	var searchParams = new URLSearchParams(window.location.search);
	$.ajax({
  		url: "game", 
		method: "DELETE", 
		data: {id: searchParams.get("id")}
	}).done(function(data, textStatus) {
  		if (textStatus == "nocontent") {
			window.location.href = "/";
    	}
	});
}


/* $.ajax({
  url: "gameState",
}).done(function() {
	
}); */

var eventSource = new EventSource('/sse');
eventSource.addEventListener("playerJoined", 
							function(event) {
	console.log('playerJoined: ' + event.data);
	
	var ulElement = document.getElementById('players');
    ulElement.innerHTML = '';
	var playersList = JSON.parse(event.data);
    for (var i = 0; i < playersList.length; i++)
    {
        var liElement = document.createElement('li');
        var textNode = document.createTextNode(playersList[i].name);
        liElement.appendChild(textNode);
        ulElement.appendChild(liElement);
	}


})

