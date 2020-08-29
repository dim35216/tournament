var participants = [];
var teams = [];
var state_participants = "";
var state_teams = "";

console.log(participants);
console.log(typeof(participants));

$(document).ready(function(){
	$("#participant-submit-button").on("click", function(){
		var newParticipant = ["", $("#participant-firstname").val(), $("#participant-lastname").val(), $("#participant-referee").prop("checked")];
		participants.push(newParticipant);
		updateParticipantsTable();
		
		var jsonvar = "{ \"vorname\": \"" + $("#participant-firstname").val() + "\", \"nachname\": \"" + $("#participant-lastname").val() + "\", \"schiedsrichter\": \"" + $("#participant-referee").prop("checked") + "\"}";
		console.log(jsonvar);
		$.ajax({
			method: "POST",
			data: jsonvar,
			dataType: "json",
			headers: {  
				"Content-type": "application/json"
			},
			url: "http://localhost:8081/restapi/tournament/participant",
			success: function(response) {
				console.log(response);
			}
		});
		
		$("#participant-firstname").val("");
		$("#participant-lastname").val("");
		$("#participant-referee").prop("checked", false);
	});
	
	$("#team-submit-button").on("click", function(){
		var newTeam = ["", $("#team-name").val()];
		teams.push(newTeam);
		updateTeamsTable();
		
		var jsonvar = "{ \"name\": \"" + $("#team-name").val() + "\"}";
		console.log(jsonvar);
		$.ajax({
			method: "POST",
			data: jsonvar,
			dataType: "json",
			headers: {  
				"Content-type": "application/json"
			},
			url: "http://localhost:8081/restapi/tournament/team",
			success: function(response) {
				console.log(response);
			}
		});
		
		$("#team-name").val("");
	});
	
	$("#myaktualisierebutton").on("click", function(){
		updateParticipants();
	});
	
	$("#participants-table button").on("click", function () {
		console.log("Löschen-Button gedrückt");
		$(this).parent("td").parent("tr").remove();
	});
	
});

// Zeitgesteuerte Anfrage an Server alle 10 Sekunden
var intervalServerConnection = setInterval(function() { 
    console.log("Verbindung zum Server...");
	
	$.ajax({
		method: "GET",
		url: "http://localhost:8081/restapi/tournament/participants/state",
		success: function(response) {
			// console.log("Participants state: " + response);
			
			if(response != state_participants) {
				state_participants = response;
				updateParticipantsTable();
			}
		}
	});
	
	$.ajax({
		method: "GET",
		url: "http://localhost:8081/restapi/tournament/teams/state",
		success: function(response) {
			// console.log("Teams state: " + response);
			
			if(response != state_teams) {
				state_teams = response;
				updateTeamsTable();
			}
		}
	});
	
}, 10000);


function updateParticipants() {
	$.ajax({
		method: "GET",
		headers: {  
			"Accept": "application/json"
		},
		url: "http://localhost:8081/restapi/tournament/participants",
		success: function(response) {
			console.log(response);
			participants = response;
			updateParticipantsTable();
		}
	});
}

function updateParticipantsTable() {
	// Aufsteigend nach Nachname, Vorname sortieren
	participants.sort(function(a, b) {
		console.log("a: " + a);
		console.log("b: " + b);
	  	var diff = a[2] - b[2];
		if(diff == 0) {
			diff = a[1] - b[1];
		}
		return diff;
	});
	
	$("#participants-table").children("tbody").empty();
	var i;
	for(i = 0; i < participants.length; i++) {
		$("#participants-table").children("tbody").append("<tr><td><input type=\"text\" value=\"" + participants[i][1] + "\" readonly/></td><td><input type=\"text\" value=\"" + participants[i][2] + "\" readonly/></td><td><input type=\"text\" value=\"" + participants[i][3] + "\" readonly/></td><td><button>Löschen</button></td></tr>");
	}
}

function updateTeams() {
	$.ajax({
		method: "GET",
		headers: {  
			"Accept": "application/json"
		},
		url: "http://localhost:8081/restapi/tournament/teams",
		success: function(response) {
			console.log(response);
			teams = response;
			updateTeamsTable();
		}
	});
}

function updateTeamsTable() {
	// Aufsteigend nach dem Namen sortieren
	teams.sort(function(a, b) {
		return a[1] - b[1];
	});
	
	$("#teams-table").children("tbody").empty();
	var i;
	for(i = 0; i < teams.length; i++) {
		$("#teams-table").children("tbody").append("<tr><td><input type=\"text\" value=\"" + teams[i][1] + "\" readonly/></td><td></td><td></td></tr>");
	}
}


// Intervall beenden 
//clearInterval(meinIntervall);
