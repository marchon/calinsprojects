$(function(){
	/*var editor = ace.edit("editor");
    	editor.setTheme("ace/theme/monokai");
    	editor.getSession().setMode("ace/mode/javascript");*/

var source   = $("#entry-template").html();
var template = Handlebars.compile(source);
var context = {title: "My New Post", body: "This is my first post!"}
var html    = template(context);
$("body").append(html);
});
