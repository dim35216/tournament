var participants = [];
var teams = [];
var state_participants = "";
var state_teams = "";
var editingTeams = false;
var initial_id_participants = 1;
var initial_id_teams = 1;


$(document).ready(function(){
	update();
	
	$("#participant-submit-button").on("click", function() {
		var newParticipant = ["" + initial_id_participants++, $("#participant-firstname").val(), $("#participant-lastname").val(), $("#participant-referee").prop("checked")];
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
		
		console.log(participants);
	});
	
	$("#team-submit-button").on("click", function() {
		var newTeam = ["" + initial_id_teams++, $("#team-name").val(), 2, []];
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
		
		console.log(teams);
	});
	
	$("#groups-create-button").on("click", function() {
		$.ajax({
			method: "POST",
			headers: {  
				"Accept": "application/json"
			}, 
			url: "http://localhost:8081/restapi/tournament/groups",
			success: function(response) {
				console.log(response);
			}
		});
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
		$("#participants-table").children("tbody").append("<tr><td>" + participants[i][1] + "</td><td>" + participants[i][2] + "</td><td><input type=\"checkbox\" " + (participants[i][3] ? "checked":"") + " disabled/><label/></td><td><button class=\"delete-button\" onclick=\"deleteRowFromParticipants(this)\">Löschen</button></td></tr>");
	}
}

function deleteRowFromParticipants(clickedButton) {
	var id = participants[$(clickedButton).parent("td").parent("tr").index()][0];
	participants.splice($(clickedButton).parent("td").parent("tr").index(), 1);
	
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
	
	updateParticipantsTable();
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
		var newRow = $("<tr><td>" + teams[i][1] + "</td><td><button class=\"edit-button\" onclick=\"editRowFromTeams(this)\">Teilnehmer hinzufügen</button></td><td><button class=\"delete-button\" onclick=\"deleteRowFromTeams(this)\">Löschen</button></td></tr>");
		newRow.data("index", i);
		if(teams[i][3].length == teams[i][2]) {
			newRow.find(".edit-button").hide();
		}
		$("#teams-table").children("tbody").append(newRow);
		
		var j;
		for(j = 0; j < teams[i][3].length; j++) {
			var participant_id = teams[i][3][j];
			var index_participant = -1;
			
			var k;
			for(k = 0; k < participants.length; k++) {
				if(participant_id.localeCompare(participants[k][0]) == 0) {
					index_participant = k;
					break;
				}
			}
			
			if(index_participant != -1) {
				console.log("index_participant: " + index_participant);
				console.log(typeof index_participant);
				
				var newRow = $("<tr><td></td><td>" + participants[index_participant][1] + " " + participants[index_participant][2] + "</td><td><button class=\"remove-button\" onclick=\"removeParticipantFromTeam(this)\">Entfernen</button></td></tr>");
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
	
	updateTeamsTable();
}

function editRowFromTeams(clickedButton) {
	editingTeams = true;
	$("#team-submit-button").attr("disabled", true);
	$(".edit-button, .remove-button, .delete-button").attr("disabled", true);
	
	var index_team = $(clickedButton).parent("td").parent("tr").data("index");
	var participants_in_team = teams[index_team][3];
	
	$(clickedButton).parent("td").parent("tr").children().eq(2).replaceWith("<td><button class=\"select-button\" onclick=\"selectParticipantForTeam(this)\">Auswählen</button></td>");
	
	var optionList = "<select id=\"optionParticipantsForTeam\"><option value=0>---Teilnehmer auswählen---</option>";
	var i;
	for(i = 0; i < participants.length; i++) {
		var already_in_team = false;
		var j;
		for(j = 0; j < participants_in_team.length; j++) {
			if(participants_in_team[j].localeCompare(participants[i][0]) == 0) {
				already_in_team = true;
			}
		}
		if(already_in_team) {
			continue;
		}
		optionList = optionList.concat("<option value=\"" + participants[i][0] + "\">" + participants[i][1] + " " + participants[i][2] + "</option>");
	}
	optionList = optionList.concat("</select>");
	$(clickedButton).parent("td").parent("tr").children().eq(1).replaceWith("<td>" + optionList + "</td>");
}

function selectParticipantForTeam(clickedButton) {
	var id_participant = $("#optionParticipantsForTeam").val();
	console.log("ID des gewählten Teilnehmers: " + id_participant);
	
	if("0".localeCompare(id_participant) != 0) {
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
	}
	
	editingTeams = false;
	$("#team-submit-button").attr("disabled", false);
	$(".edit-button, .remove-button, .delete-button").attr("disabled", false);
	
	updateTeamsTable();
}

function removeParticipantFromTeam(clickedButton) {
	var index_team = $(clickedButton).parent("td").parent("tr").data("index");
	var id_team = teams[index_team][0];
	
	var index_participant = $(clickedButton).parent("td").parent("tr").data("index_participant");
	var id_participant = participants[index_participant][0];
	
	var pos_participant_in_team = teams[index_team][3].indexOf(id_participant);
	teams[index_team][3].splice(pos_participant_in_team, 1);
	
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
	
	updateTeamsTable();
	
	console.log(teams);
}


// Intervall beenden 
//clearInterval(meinIntervall);
