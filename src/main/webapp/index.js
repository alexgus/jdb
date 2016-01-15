
var notes;
var tags = new Array();

////////////////////////
// Util
////////////////////////

function onload(){
	getTags();
	getNotes();
	setTimeout(function(){ // wait for tags to be reeived
		openEdit();
	},200);
}

function toURL(texte){
	var newTexte = "";
	for(var i = 0 ; i < texte.length ; ++i){
		if((texte.charCodeAt(i) < 48)
				|| (texte.charCodeAt(i) > 57 && texte.charCodeAt(i) < 65)
				|| (texte.charCodeAt(i) > 90 && texte.charCodeAt(i) < 97)
				|| (texte.charCodeAt(i) > 122 && texte.charCodeAt(i) < 140)
				|| (texte.charCodeAt(i) > 140 && texte.charCodeAt(i) < 156)
				|| (texte.charCodeAt(i) > 156 && texte.charCodeAt(i) < 192)){
			if(texte.charCodeAt(i) < 16)
				newTexte += "%0" + parseInt(texte.charCodeAt(i),10).toString(16); // convert base 10 to 16
			else
				newTexte += "%" + parseInt(texte.charCodeAt(i),10).toString(16); // convert base 10 to 16
		}else
			newTexte += texte[i];
	}
	return newTexte;
}

////////////////////////
// Com
////////////////////////

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

function sendNote(i){
	var urlPOST;
	var note = toURL($("textarea").val());
	var tag = toURL($("#tag").val());
	
	if(i != undefined){
		urlPOST = "http://localhost/proxy9000/note/"+notes[i]._id+"/"+notes[i]._rev + "/" + tag + "/" + note;
	}
	else{
		urlPOST = "http://localhost/proxy9000/note/"+ tag + "/" + note;
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
					modDisplayedNote(data,i);
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

function getTags(){
	$.ajax({
		method: "GET",
		url: "http://localhost/proxy9000/note/gTag",
		success : function(d){
			tags = new Array();
			for(var i = 0 ; i < d.rows.length ; ++i)
				tags[tags.length] = d.rows[i].key
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

function openEdit(i){
	$.ajax({
		method: "POST",
		url: "http://localhost/jdb/form.html",
		success : function(data){
			$("#in").html(data);
			$("#tag").autocomplete({
				  source: tags,
				  minLength: 0,
				  position: { 
					  my : "top", 
					  at: "bottom",
					  collision: "flip"
				  },
				  close : function(){
					  // count enter
				  }
			});
			
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

////////////////////////
// View
////////////////////////

function displayNote(i){
	notes[i].note = notes[i].note.replace(/\r\n/g, "</br>");
	notes[i].note = notes[i].note.replace(/\r/g, "</br>");
	notes[i].note = notes[i].note.replace(/\n/g, "</br>");
	$("#in").html(
			"<div class=\"label label-default\">Date : " + new Date(notes[i].date) + "</div></br>"
			+ "<div class=\"label label-info\">Tag : " + notes[i].tag + "</div></br></br>"
			+ "<div class=\"alert alert-info\" role=\"alert\" ondblclick=\"openEdit("+i+")\">" + notes[i].note + "</div>"
	);
}

function modDisplayedNote(note, i){
	console.log(i)
	var list = $("#list > div").children()
	var element = $(list[list.length - 1 - i]); 
	element.find(".note").html(note.note)
	element.find(".label").html(note.tag)
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


