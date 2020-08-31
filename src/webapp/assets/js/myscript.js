var participants = [];
var teams = [];
var state_participants = "";
var state_teams = "";
var editingTeams = false;


$(document).ready(function(){
	update();
	
	$("#participant-submit-button").on("click", function(){
		var newParticipant = ["", $("#participant-firstname").val(), $("#participant-lastname").val(), $("#participant-referee").prop("checked")];
		participants.push(newParticipant);
		updateParticipantsTable();
		
		var jsonvar = "{ \"vorname\": \"" + $("#participant-firstname").val() + "\", \"nachname\": \"" + $("#participant-lastname").val() + "\", \"schiedsrichter\": \"" + $("#participant-referee").prop("checked") + "\"}";
		// console.log(jsonvar);
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
		var newTeam = ["", $("#team-name").val(), 2, []];
		teams.push(newTeam);
		updateTeamsTable();
		
		var jsonvar = "{ \"name\": \"" + $("#team-name").val() + "\", \"max_teilnehmer\": 2, \"teilnehmerListe\": []}";
		// console.log(jsonvar);
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
	
	/*$("#myaktualisierebutton").on("click", function(){
		updateParticipants();
	});*/
	
	$("#participants-table button").on("click", function () {
		console.log("Löschen-Button gedrückt");
		$(this).parent("td").parent("tr").remove();
	});
	
});

// Zeitgesteuerte Anfrage an Server alle 10 Sekunden
var intervalServerConnection = setInterval(function() { 
	
	update();
	
}, 10000);

function update() {
	console.log("Update... Verbindung zum Server...");
	
	$.ajax({
		method: "GET",
		url: "http://localhost:8081/restapi/tournament/participants/state",
		success: function(response) {
			// console.log("Participants state: " + state_participants + "   Response: " + response);
			
			if(response.localeCompare(state_participants) != 0) {
				state_participants = response;
				updateParticipants();
			}
		}
	});
	
	$.ajax({
		method: "GET",
		url: "http://localhost:8081/restapi/tournament/teams/state",
		success: function(response) {
			// console.log("Teams state: " + state_teams + "   Response: " + response);
			
			if(response.localeCompare(state_teams) != 0) {
				state_teams = response;
				updateTeams();
			}
		}
	});
}


function updateParticipants() {
	$.ajax({
		method: "GET",
		headers: {  
			"Accept": "application/json"
		},
		url: "http://localhost:8081/restapi/tournament/participants",
		success: function(response) {
			// console.log(response);
			participants = [];
			var i;
			for(i = 0; i < response.length; i++) {
				participants.push([response[i].id, response[i].vorname, response[i].nachname, response[i].schiedsrichter]);
			}
			updateParticipantsTable();
		}
	});
}

function updateParticipantsTable() {
	// Aufsteigend nach Nachname, Vorname sortieren
	participants.sort(function(a, b) {
	  	var diff = a[2].localeCompare(b[2]);
		if(diff == 0) {
			diff = a[1].localeCompare(b[1]);
		}
		return diff;
	});
	
	$("#participants-table").children("tbody").empty();
	var i;
	for(i = 0; i < participants.length; i++) {
		$("#participants-table").children("tbody").append("<tr><td>" + participants[i][1] + "</td><td>" + participants[i][2] + "</td><td><input type=\"checkbox\" " + (participants[i][3] ? "checked":"") + " disabled/><label/></td><td><button onclick=\"deleteRowFromParticipants(this)\">Löschen</button></td></tr>");
	}
}

function deleteRowFromParticipants(clickedButton) {
	var id = participants[$(clickedButton).parent("td").parent("tr").index()][0];
	participants.splice($(clickedButton).parent("td").parent("tr").index(), 1);
	
	$(clickedButton).parent("td").parent("tr").remove();
	
	$.ajax({
		method: "DELETE",
		headers: {  
			"Accept": "application/json"
		},
		url: "http://localhost:8081/restapi/tournament/participant/".concat(id),
		success: function(response) {
			// console.log(response);
		}
	});
}

function updateTeams() {
	$.ajax({
		method: "GET",
		headers: {  
			"Accept": "application/json"
		},
		url: "http://localhost:8081/restapi/tournament/teams",
		success: function(response) {
			// console.log(response);
			teams = [];
			var i;
			for(i = 0; i < response.length; i++) {
				teams.push([response[i].id, response[i].name, response[i].max_teilnehmer, response[i].teilnehmerListe]);
			}
			updateTeamsTable();
		}
	});
}

function updateTeamsTable() {
	if(editingTeams) { return; }
	// Aufsteigend nach dem Namen sortieren
	teams.sort(function(a, b) {
		return a[1].localeCompare(b[1]);
	});
	
	$("#teams-table").children("tbody").empty();
	var i;
	for(i = 0; i < teams.length; i++) {
		var newRow = $("<tr><td>" + teams[i][1] + "</td><td><button onclick=\"editRowFromTeams(this)\">Bearbeiten</button></td><td><button onclick=\"deleteRowFromTeams(this)\">Löschen</button></td></tr>");
		newRow.data("index", i);
		$("#teams-table").children("tbody").append(newRow);
		
		var j;
		for(j = 0; j < teams[i][3].length; j++) {
			var participant_id = teams[i][3][j];
			var index_participant = -1;
			
			var k;
			for(k = 0; k < participants.length; k++) {
				if(participant_id.localeCompare(participants[k][0]) == 0) {
					index_participant = i;
					break;
				}
			}
			
			if(index_participant != -1) {
				var newRow = $("<tr><td></td><td>" + participants[index_participant][1] + " " + participants[index_participant][2] + "</td><td><button onclick=\"removeParticipantFromTeam(this)\">Entfernen</button></td></tr>");
				newRow.data("index", i);
				newRow.data("index_participant", index_participant);
				$("#teams-table").children("tbody").append(newRow);
			}
		}
	}
}

function deleteRowFromTeams(clickedButton) {
	var index_team = $(clickedButton).parent("td").parent("tr").data("index");
	var id_team = teams[index_team][0];
	teams.splice(index_team, 1);
	
	$(clickedButton).parent("td").parent("tr").remove();
	
	$.ajax({
		method: "DELETE",
		headers: {  
			"Accept": "application/json"
		},
		url: "http://localhost:8081/restapi/tournament/team/".concat(id_team),
		success: function(response) {
			// console.log(response);
		}
	});
}

function editRowFromTeams(clickedButton) {
	editingTeams = true;
	
	$(clickedButton).parent("td").parent("tr").children().eq(2).replaceWith("<td><button onclick=\"saveRowFromTeams(this)\">Speichern</button></td>");
	
	var optionList = "<select id=\"optionParticipantsForTeams\"><option value=0>---Teilnehmer auswählen---</option>";
	var i;
	for(i = 0; i < participants.length; i++) {
		optionList = optionList.concat("<option value=\"" + participants[i][0] + "\">" + participants[i][1] + " " + participants[i][2] + "</option>");
	}
	optionList = optionList.concat("</select>");
	$(clickedButton).parent("td").parent("tr").children().eq(1).replaceWith("<td>" + optionList + "</td>");
}

function saveRowFromTeams(clickedButton) {
	console.log($("#optionParticipantsForTeams").val());
	var id_participant = $("#optionParticipantsForTeams").val();
	
	var index_team = $(clickedButton).parent("td").parent("tr").data("index");
	var id_team = teams[index_team][0];
	teams[index_team][3].push(id_participant);
	
	$.ajax({
		method: "PUT",
		headers: {  
			"Accept": "application/json"
		},
		url: "http://localhost:8081/restapi/tournament/team/".concat(id_team) + "/add/".concat(id_participant),
		success: function(response) {
			console.log(response);
		}
	});
	
	$(clickedButton).parent("td").parent("tr").children().eq(1).replaceWith("<td><button onclick=\"editRowFromTeams(this)\">Bearbeiten</button></td>");
	$(clickedButton).parent("td").parent("tr").children().eq(2).replaceWith("<td><button onclick=\"deleteRowFromTeams(this)\">Löschen</button></td>");
	
	editingTeams = false;
}

function removeParticipantFromTeam(clickedButton) {
	var index_team = $(clickedButton).parent("td").parent("tr").data("index");
	var id_team = teams[index_team][0];
	
	var index_participant = $(clickedButton).parent("td").parent("tr").data("index_participant");
	var id_participant = participants[index_participant][0];
	
	$(clickedButton).parent("td").parent("tr").remove();
	
	$.ajax({
		method: "PUT",
		headers: {  
			"Accept": "application/json"
		},
		url: "http://localhost:8081/restapi/tournament/team/".concat(id_team) + "/remove/".concat(id_participant),
		success: function(response) {
			console.log(response);
		}
	});
}


// Intervall beenden 
//clearInterval(meinIntervall);
