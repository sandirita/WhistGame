var gamesList;
function onGameCreatedTemplatesDownloaded(template) {
	var joinFormsinString = Mustache.render(template, {games: gamesList});
	$("#joinForms").html(joinFormsinString);
}

function onGameCreated(event) {
	gamesList = JSON.parse(event.data);
	$.get( "MustacheTemp/joinGame.html", onGameCreatedTemplatesDownloaded);
	}


$( document ).ready(function() {
  // Handler for .ready() called.

    console.log('DOM fully loaded and parsed');
	var nameElement = document.getElementById("name");
	
	var url = new URL(window.location);
	if (url.searchParams.has('error')) {
		var errorMessage = url.searchParams.get('error');
		nameElement.setCustomValidity(errorMessage);
		nameElement.reportValidity();
	}

	nameElement.addEventListener('change', (event) => {
  		event.target.setCustomValidity("");
	});

	var eventSource = new EventSource('/sse');
	eventSource.addEventListener("gameCreated", onGameCreated);
	
	$( "#name" ).change(function() {
		var nameSaved = $(this).val();
  		$( ".name" ).val(nameSaved);
	});
	
	$( ".submits" ).submit(function( event ) {
  		if($(this).children(".name").val() === ""){
			event.preventDefault();
			var htmlElement = $("#name").get(0);
			htmlElement.setCustomValidity("You must fill out this field!");
			htmlElement.reportValidity();
  			
		}
	});
});