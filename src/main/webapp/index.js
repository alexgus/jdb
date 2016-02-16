var noteServiceURL = "http://localhost/proxy9000";
var noteServiceBigDataURL = "http://localhost/proxy8080";
var pageURL = "http://localhost/jdb"; // TODO could be deduce with page info

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

function paste(e){
	if(typeof e.clipboardData != 'undefined') {
		var copiedData = e.clipboardData.items[0]; //Get the clipboard data
		/*If the clipboard data is of type image, read the data*/
		if(copiedData.type.indexOf("image") == 0) {
			var imageFile = copiedData.getAsFile(); 
			/*We will use HTML5 FileReader API to read the image file*/
			var reader = new FileReader();
			
			reader.onload = function (evt) {
				var result = evt.target.result; //base64 encoded image
				/*Create an image element and append it to the content editable div*/
				var img = document.createElement("img");
				img.src = result;
				document.getElementById("editablediv").appendChild(img);
				
				// TODO Save in db (cannot be saved via REST URL)
			};
			/*Read the image file*/
			reader.readAsDataURL(imageFile);
		}
	}
}

////////////////////////
// Com
////////////////////////

function getNotes(){
	$.ajax({
		method: "GET",
		url: noteServiceURL + "/note",
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
	var _data;
	var note = toURL($("#textEdit").html());
	var tag = toURL($("#tag").val());
	
	/*if(i != undefined){
		urlPOST = noteServiceURL + "/note/"+notes[i]._id+"/"+notes[i]._rev + "/" + tag + "/" + note;
	}
	else{
		urlPOST = noteServiceURL + "/note/"+ tag + "/" + note;
    }*/
	
	if(i != undefined){
		urlPOST = noteServiceBigDataURL + "/note/"+notes[i]._id+"/"+notes[i]._rev + "/" + tag + "/" + note;
		_data = {
				"id" : notes[i]._id,
				"rev" : notes[i]._rev,
				"tag" : tag,
				"note" : note
		}
	}
	else{
		urlPOST = noteServiceBigDataURL + "/note/"+ tag + "/" + note;
		_data = {
				"tag" : tag,
				"note" : note
		}
	}
	
	console.log(urlPOST)
	$.ajax({
		method: "POST",
		url: urlPOST,
		data : _data,
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
		url: noteServiceURL + "/note/"+notes[i]._id+"/"+notes[i]._rev,
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
		url: noteServiceURL + "/note/gTag",
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
		url: noteServiceURL + "/note/gTag/" + $("#search input").val(),
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

var editing;
function openEdit(i){
	editing = i;
	$.ajax({
		method: "POST",
		url: pageURL + "/form.html",
		success : function(data){
			$("#in").html(data);
			$("#textEdit").on("paste", paste);
			
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
				//notes[i].note = notes[i].note.replace(/<\/br>/gi, "\r\n"); // Textarea to div with editable content
				$("#textEdit").html(notes[i].note);
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
	
	if(notes[i].dateModif != undefined && notes[i].dateModif.length > 0){
		var dates = "";
		for(var j = notes[i].dateModif.length - 1 ; j >=0 && j > notes[i].dateModif.length - 4; --j){
			dates += "&nbsp;" + new Date(notes[i].dateModif[j]) + "&nbsp;</br>";
		}
		dates += "&nbsp;" + new Date(notes[i].dateModif[notes[i].dateModif.length - 1])
	}
	
	$("#in").html(
			"<div class=\"label label-primary label-tag\">Creation date : " + new Date(notes[i].date) + "</div></br>"
			+ (dates == undefined ? "" : "<div class=\"label label-info label-tag\">Modification dates : " + dates + "</div></br>")
			+ "<div class=\"label label-danger label-tag\">Tag : " + notes[i].tag + "</div></br></br>"
			+ "<div class=\"alert alert-info label-tag\" role=\"alert\" ondblclick=\"openEdit("+i+")\">" + notes[i].note + "</div>"
			+ "<button type=\"button\" class=\"btn btn-danger\" onclick=\"deleteNote("+i+")\" style=\"float:right\">Supress</button>"
	);
}

function modDisplayedNote(note, i){
	console.log(i)
	var list = $("#list > div").children()
	var element = $(list[list.length - 1 - i]); 
	element.find(".note").html(note.note)
	element.find(".label-danger").html(note.tag)
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
			+ "<div class=\"label label-primary\">" + new Date(note.date) + "</div>" 
			+ "<span onclick=\"deleteNote("+i+")\" class=\"glyphicon glyphicon-remove deleteFromList\" aria-hidden=\"true\"></span></br>"
			+ "<div class=\"label label-danger\">" + note.tag + "</div>"
			+ "<div class=\"note\">" + note.note + "</div>" +
		"</a>");
}

// for decrement cursor position
var niceLength = 0;
function niceText(text){
	if(text != undefined){
		var back = text;
		
		text = text.replace(/-&gt;/gi,"\u2192");
		if(back != text){
			niceLength = 1;
			back = text;
		}
		text = text.replace(/=&gt;/gi,"\u21D2");
		if(back != text){
			niceLength = 1;
			back = text;
		}
		text = text.replace(/&nbsp;\*/gi," •");
		if(back != text){
			niceLength = 0;
			back = text;
		}
		text = text.replace(/->/gi,"\u2192");
		if(back != text){
			niceLength = 1;
			back = text;
		}
		text = text.replace(/=>/gi,"\u21D2");
		if(back != text){
			niceLength = 1;
			back = text;
		}
		text = text.replace(/ \*/gi," •");
		if(back != text){
			niceLength = 0;
			back = text;
		}
		
		return text;
	}
}

function niceInput(){
	var range = document.getSelection().getRangeAt(0);
	var startPoint = range.startContainer;
	var startOffset = range.startOffset;
	var endPoint = range.endContainer;
	var endOffset = range.endOffset;

	var copyContent = startPoint.textContent;
	if(startPoint.textContent != niceText(copyContent)){ // If content modified
		// Search modified node
		var tab = $("#textEdit").get(0).childNodes;
		var i = 0;
		while(i < tab.length && tab[i].textContent != startPoint.textContent)
			++i;
		if(i < tab.length){ // Found at i
			startPoint.textContent = niceText(startPoint.textContent);
			console.log(niceLength)
			range.setStart(tab[i], startOffset - niceLength);
			range.setEnd(tab[i], endOffset - niceLength);
		}
	}
}


