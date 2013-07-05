$(function(){
	/*var editor = ace.edit("editor");
    	editor.setTheme("ace/theme/monokai");
    	editor.getSession().setMode("ace/mode/javascript");*/

	var docListTemplate = Handlebars.compile($("#document-list-template").html());
	$("body").append(docListTemplate({"names": ["ca/updateContacts", "it/updateContacts"]}));
});
