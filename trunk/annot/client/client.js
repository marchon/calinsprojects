String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function(match, number) { 
    return typeof args[number] != 'undefined'
      ? args[number]
      : match
    ;
  });
};

$(document).ready(function() {
	var list = $('div#list ul');
	var formContainer = $('div#upload');
	var form = $('div#upload form');
	var imgContainer = $('div#img');
	
	var picTemplate = $('#picTemplate').html().trim();
	var annotationTemplate = $('#annotationTemplate').html().trim();
	var detailsTemplate = $('#detailsTemplate').html().trim();
	
	
	var selected = null;
	var clicked = null;
	var cancelEdit = null;
	$(document).click(function() {
		if(clicked) {clicked.removeClass("click").css({zIndex:100}); clicked=null;}
		if(cancelEdit) {cancelEdit.click();}
	});
	function addPicToList(pic) {
		var path = 'pics/{0}/img.jpg'.format(pic);
		var annot = 'pics/{0}/annot.json'.format(pic);

		list.append(
			$(picTemplate.format(pic, path))
				.click(function(){
					if(selected) selected.removeClass("selected");
					selected = $(this).addClass("selected");
					
					$.getJSON(annot, function(data){
						imgContainer.empty();
						var img = $(new Image()).attr('src', path);
						imgContainer.append(img);

						$.each(data, function(idx, model) {
							var annot = $(annotationTemplate)
										.css({top: model.top, left: model.left, width: model.width, height: model.height})
										.css({zIndex:100})
										.hover(function(){if(!clicked) $(this).addClass("hover")}, function(){$(this).removeClass("hover")})
										.click(function(){if(!clicked) {clicked = $(this).addClass("click").css({zIndex:1000}); return false}});
										
							imgContainer.append(annot);
							var details = annot.children('div.details')
											.css({marginLeft: model.width + 5, width: model.textWidth})
											.click(function(){return false});

							details.children('.edit').click(function(){
								annot.addClass("editable")
									.draggable({ containment: img, cancel: ".details" })
									.resizable({ containment: img, resize: function(){details.css({marginLeft: annot.width() + 5})}}); 
								cancelEdit = details.children('.cancel');
								
								details.find("h4").editable({lineBreaks : false, closeOnEnter: true, toggleFontSize: false, event : 'click'});
								details.find("p").editable({toggleFontSize: false, event : 'click'});
							});
							details.children('.cancel, .accept').click(function(){
								annot.removeClass("editable").draggable("destroy").resizable("destroy");
								details.find('h4, p').editable("destroy");
								cancelEdit = null;
							});
							details.children('.cancel').click(function(){
								//bug.
								console.log(model)
								annot.css({top: model.top, left: model.left, width: model.width, height: model.height});
								details.css({marginLeft: model.width + 5, width: model.textWidth});
							});
							details.children('.accept').click(function(){
								//save changes
								model = {top: annot.position().top, left: annot.position().left, width: annot.width(), height: annot.height()}
								console.log(model)
							});
							
							var list = details.children('ul');
							$.each(model.text, function(heading, content) {
								list.append($(detailsTemplate.format(heading, content)));
							});					
						});
					});
				})
		);
	}
	
	$.getJSON('/pics', function(data) {
		list.empty();
		$.each(data, function(i, pic) {addPicToList(pic)});
	});
	
	$('#new').click(function(){formContainer.show()});
	form.children(".cancel").click(function(){formContainer.hide()});

	form.ajaxForm({
        beforeSubmit: function(data) {
			formContainer.hide();
        },
        complete: function(xhr) {
			if(xhr.status == 200) {
				addPicToList(form.children("input[type=text]").val());
			}
        }
    });
})