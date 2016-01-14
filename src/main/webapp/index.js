
function sendNote(i){
	var urlPOST; 
    var note = $("textarea").val().replace(/\n/gi, "%0D%0A");

	if(i != undefined){
		urlPOST = "http://localhost/proxy9000/note/"+notes[i]._id+"/"+notes[i]._rev + "/" + $("#tag").val() + "/" + note;
	}
	else{
		urlPOST = "http://localhost/proxy9000/note/"+ $("#tag").val() + "/" + note;
    }
	console.log(urlPOST)
	$.ajax({
		method: "POST",
		url: urlPOST,
		success : function(data){
			$("#notif").html("<div class=\"alert alert-success\" role=\"alert\">Saved !</div>");
			if(i != undefined){
				notes[i] = data;
				setTimeout(function(){
					displayNote(i);
					addNote(data,notes.length-1);
				},500)
			}else{
				notes[notes.length] = data;
				setTimeout(function(){
					displayNote(notes.length-1);
					addNote(data,notes.length-1);
				},500)
			}
		},
		error: function(xhr, status, error){
			$("#notif").html("<div class=\"alert alert-danger\" role=\"alert\">Server error !</div>");
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	})
	
}

function openEdit(i){
	$.ajax({
		method: "POST",
		url: "http://localhost/jdb/form.html",
		success : function(data){
			$("#in").html(data);
			if(i != undefined){
				notes[i].note = notes[i].note.replace(/<\/br>/gi, "\r\n");
				$("textarea").val(notes[i].note);
				$("#tag").val(notes[i].tag)
				$("#sendButton").prop("onclick", null);
				$("#sendButton").click(function(){
					sendNote(i);
				});
			}
		},
		error: function(xhr, status, error){
			$("#in").html("Error");
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	})
}

function displayNote(i){
	notes[i].note = notes[i].note.replace(/\r\n/g, "</br>")
	$("#in").html(
			"<div class=\"label label-default\">Date : " + new Date(notes[i].date) + "</div></br>"
			+ "<div class=\"label label-info\">Tag : " + notes[i].tag + "</div></br></br>"
			+ "<div class=\"alert alert-info\" role=\"alert\" ondblclick=\"openEdit("+i+")\">" + notes[i].note + "</div>"
	);
}

var notes;

function deleteNote(i){
	$(".note"+i).remove();
	// ask confirm
	$.ajax({
		method: "DELETE",
		url: "http://localhost/proxy9000/note/"+notes[i]._id+"/"+notes[i]._rev,
		success : function(data){
			setTimeout(function(){
				openEdit();
			},10);
		},
		error: function(xhr, status, error){
			$("#in").html("Error");
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	})
	
}

function deleteAllNotes(){
	for(var i = 0 ; i < notes.length ; ++i)
		$(".note"+i).remove();
	notes = new Array();
}

function resetSearch(){
	deleteAllNotes();
	$("#search input").val("");
	onload();
}

function addNote(note,i){
	$($("#list > div").children()[1]).after("<a href=\"#\" id=\"itemList\" class=\"list-group-item note"+ i +"\" onclick=\"displayNote("+i+")\">" 
			+ "<div class=\"label label-default\">" + new Date(note.date) + "</div>" 
			+ "<span onclick=\"deleteNote("+i+")\" class=\"glyphicon glyphicon-remove deleteFromList\" aria-hidden=\"true\"></span></br>"
			+ "<div class=\"label label-info\">" + note.tag + "</div>"
			+ "<div class=\"note\">" + note.note + "</div>" +
		"</a>");
}

function getNotes(){
	$.ajax({
		method: "GET",
		url: "http://localhost/proxy9000/note",
		success : function(d){
			notes = d;
			for(var i = 0 ; i < d.length ; ++i){
				addNote(d[i],i);
			}
		},
		error: function(xhr, status, error){
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	})
}

function searchTag(){
	deleteAllNotes();
	$.ajax({
		method: "GET",
		url: "http://localhost/proxy9000/note/gTag/" + $("#search input").val(),
		success : function(d){
			notes = d;
			for(var i = 0 ; i < d.length ; ++i){
				addNote(d[i],i);
			}
		},
		error: function(xhr, status, error){
			console.log(xhr);
			console.log(status);
			console.log(error);
		}
	})
}

function onload(){
	getNotes();
	openEdit();
}


