function(doc) {
  if(doc.$table == "fr.utbm.to52.smarthome.model.note.Note")
	  emit(doc.$table, doc);
}