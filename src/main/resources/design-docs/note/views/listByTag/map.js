function(doc) {
  if(doc.$table == "fr.nikk.model.note.Note")
	  if(doc.tag.length > 0)
		  emit(doc.tag, doc);
}