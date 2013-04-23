String.prototype.format = function () {
    var args = arguments;
    return this.replace(/{(\d+)}/g, function (match, number) {
        return typeof args[number] != 'undefined'
            ? args[number]
            : match
            ;
    });
};
String.prototype.int = function () {return parseInt(this)}

$(document).ready(function () {
    var list = $('div#list ul');
    var formContainer = $('div#upload');
    var form = $('div#upload form');
    var imgContainer = $('div#img');

    var picTemplate = $('#picTemplate').html().trim();
    var annotationTemplate = $('#annotationTemplate').html().trim();


    var selected = null;
    var clicked = null;
    var cancelEdit = null;
    $(document).click(function () {
        if (clicked) {
            clicked.removeClass("click").css({zIndex:100});
            clicked = null;
        }
        if (cancelEdit) {
            cancelEdit.click();
        }
    });

    function addPicToList(pic) {
        var path = 'pics/{0}/img.jpg'.format(pic);
        var annot = 'pics/{0}/annot.json'.format(pic);

        list.append(
            $(picTemplate.format(pic, path))
                .click(function () {
                    if (selected) selected.removeClass("selected");
                    selected = $(this).addClass("selected");

                    $.getJSON(annot, function (data) {
                        imgContainer.empty();
                        var img = $(new Image()).attr('src', path);
                        imgContainer.append(img);

                        $.each(data, function (idx, model) {
                            var annot = $(annotationTemplate)
                                .css({top:model.top, left:model.left, width:model.width, height:model.height})
                                .css({zIndex:100})
                                .hover(function () {
                                    if (!clicked) $(this).addClass("hover")
                                }, function () {
                                    $(this).removeClass("hover")
                                })
                                .click(function (e) {
                                    if (!clicked) {
                                        clicked = $(this).addClass("click").css({zIndex:1000});
                                        e.stopPropagation();
                                    }
                                });

                            imgContainer.append(annot);
                            var details = annot.children('div.details')
                                .css({marginLeft:model.width + 5, width:model.textWidth})
                                .click(function(e){e.stopPropagation()});

                            var content = details.children('div.content');

                            details.children('.edit').click(function () {
                                cancelEdit = details.children('.cancel');

                                annot.addClass("editable")
                                    .draggable({ containment:img, cancel:".details" })
                                    .resizable({ containment:img, resize:function () {
                                        details.css({marginLeft:annot.width() + 5})
                                    }});

                                details.resizable({ containment:img, handles: "e"});

                                content.editable({toggleFontSize:false}).editable("open");
                                content.keyup(function(e) {
                                    if (e.keyCode == 27) {
                                        $(this).editable("close");
                                    }
                                });
                            });

                            details.children('.cancel, .accept').click(function () {
                                annot.removeClass("editable").draggable("destroy").resizable("destroy");
                                details.resizable("destroy");
                                content.editable("destroy");
                                cancelEdit = null;
                            });
                            details.children('.cancel').click(function () {
                                annot.css({top:model.top, left:model.left, width:model.width, height:model.height});
                                details.css({marginLeft:model.width + 5, width:model.textWidth});
                                content.html(model.text);
                            });
                            details.children('.accept').click(function () {
                                //save changes
                                model = {
                                    top:annot.css('top').int(), left:annot.css('left').int(), width:annot.width(), height:annot.height(),
                                    textWidth : details.width(), text: content.html()
                                }
                                console.log(model);
                            });

                            content.html(model.text);
                        });
                    });
                })
        );
    }

    $.getJSON('/pics', function (data) {
        list.empty();
        $.each(data, function (i, pic) {
            addPicToList(pic)
        });
    });

    $('#new').click(function () {
        formContainer.show()
    });
    form.children(".cancel").click(function () {
        formContainer.hide()
    });

    form.ajaxForm({
        beforeSubmit:function (data) {
            formContainer.hide();
        },
        complete:function (xhr) {
            if (xhr.status == 200) {
                addPicToList(form.children("input[type=text]").val());
            }
        }
    });
})