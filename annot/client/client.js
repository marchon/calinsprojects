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


    var selectedImage = null;
    var clickedAnnotation = null;
    var cancelButton = null;

    var richEditor = new nicEditor();
    richEditor.setPanel('nicPanel');

    $('#uploadBtn').click(function () {
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
                prepareImage(form.children("input[type=text]").val());
            }
        }
    });

    $.getJSON('/pics', function (data) {
        list.empty();
        $.each(data, function (i, pic) {
            prepareImage(pic)
        });
    });

    $('#newBtn').click(function (e) {
        e.stopPropagation();
        var img = imgContainer.children("img");
        var annot = prepareAnnotation({
            top:200, left:200,
            width:100, height:100,
            textWidth : 200, text: "<h4>Edit me</h4>"
        }, null, img, $.data(img[0], "name"));
        annot.click();
        annot.find(".edit").click();
    });

    var revert = function () {
        if(cancelButton && !confirm("Revert all changes?")) return false;

        if (clickedAnnotation) {
            clickedAnnotation.removeClass("click").css({zIndex:100});
            clickedAnnotation = null;
        }
        if (cancelButton) {
            cancelButton.click();
        }

        return true;
    }

    $("div#img").click(revert);

    function prepareImage(imgName) {
        var imageUrl = 'pics/{0}/img.jpg'.format(imgName);
        var annotListUrl = 'pics/{0}/annot.json'.format(imgName);

        list.append(
            $(picTemplate.format(imgName, imageUrl))
                .click(function () {
                    if(!revert()) return;
					if ($(this).hasClass("selected")) return; //return if already selected
                    if (selectedImage) selectedImage.removeClass("selected");
                    selectedImage = $(this).addClass("selected");

                    $.getJSON(annotListUrl, function (data) {
                        imgContainer.empty();
                        var img = $(new Image()).attr('src', imageUrl);
                        $.data(img[0], "name", imgName);
                        imgContainer.append(img);

                        $.each(data, function (uid, model) {
                            prepareAnnotation(model, uid, img, imgName);
                        });
                    });
                })
        );
    }

    function prepareAnnotation(model, uid, img, imgName){
        var annotUrl = 'pics/{0}/annot/{1}';

        var annot = $(annotationTemplate)
            .css({top:model.top, left:model.left, width:model.width, height:model.height})
            .css({zIndex:100})
            .hover(function () {
                if (!clickedAnnotation) annot.addClass("hover")
            }, function () {
                $(this).removeClass("hover")
            })
            .click(function (e) {
                if (!clickedAnnotation) {
                    clickedAnnotation = annot.addClass("click").css({zIndex:1000});
                    e.stopPropagation();
                }
            });
        var details = annot.children('div.details')
            .css({marginLeft:model.width + 5, width:model.textWidth})
            .click(function(e){e.stopPropagation()});

        var content = details.children('div.content');
        content.html(model.text);

        imgContainer.append(annot);

        details.children('.edit').click(function (e) {
            e.stopPropagation();
            cancelButton = details.children('.cancel');

            annot.addClass("editable")
                .draggable({ containment: img, cancel: ".details" })
                .resizable({ containment: img, resize: function () {
                    details.css({marginLeft: annot.width() + 5})
                }});

            details.resizable({ containment:img, handles: "e"});

            richEditor.addInstance(content[0])
        });

        details.children('.cancel, .accept').click(function () {
            annot.removeClass("editable").draggable("destroy").resizable("destroy");
            details.resizable("destroy");
            richEditor.removeInstance(content[0])
            cancelButton = null;
        });
        var reset = function () {
            annot.css({top: model.top, left: model.left, width: model.width, height: model.height});
            details.css({marginLeft: model.width + 5, width: model.textWidth});
            content.html(model.text);
        };
        details.children('.cancel').click(reset);

        details.children('.accept').click(function () {
            var text = content.html();
            if (/<script.*>.*<\/script>/igm.exec(text) != null) {
                reset();
                alert("No js biatch!!!");
                return;
            }

            //save changes
            model = {
                top:annot.css('top').int(), left:annot.css('left').int(),
                width:annot.width(), height:annot.height(),
                textWidth : details.width(), text: content.html()
            }

            var furl = annotUrl.format(imgName, uid != null ? uid : "");
            console.log("Creating/updating annotation: ", furl);
            $.ajax({
                url: furl,
                type: uid != null ? "PUT" : "POST",
                data: JSON.stringify(model),
                contentType: "application/json"
            }).done(function (msg) {
                if(uid == null) uid = msg.uid;
                console.log("Created/updated annotation: ", furl);
            }).fail(function (msg) {
                console.log("Error creating/updating annotation: ", furl, msg);
            });
        });

        return annot;
    }
})