function(doc) {
  if(doc.$table == "fr.nikk.model.note.Note")
	  emit(doc.tag, doc);
}