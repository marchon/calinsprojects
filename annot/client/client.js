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

    $(document).click(function () {
        if(cancelButton && !confirm("Revert all changes?")) return;

        if (clickedAnnotation) {
            clickedAnnotation.removeClass("click").css({zIndex:100});
            clickedAnnotation = null;
        }
        if (cancelButton) {
            cancelButton.click();
        }
    });

    function prepareImage(imgName) {
        var imageUrl = 'pics/{0}/img.jpg'.format(imgName);
        var annotListUrl = 'pics/{0}/annot.json'.format(imgName);

        list.append(
            $(picTemplate.format(imgName, imageUrl))
                .click(function () {
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

        imgContainer.append(annot);
        var details = annot.children('div.details')
            .css({marginLeft:model.width + 5, width:model.textWidth})
            .click(function(e){e.stopPropagation()});

        var content = details.children('div.content');

        details.children('.edit').click(function (e) {
            e.stopPropagation();
            cancelButton = details.children('.cancel');

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
            cancelButton = null;
        });
        details.children('.cancel').click(function () {
            annot.css({top:model.top, left:model.left, width:model.width, height:model.height});
            details.css({marginLeft:model.width + 5, width:model.textWidth});
            content.html(model.text);
        });

        details.children('.accept').click(function () {
            //save changes
            model = {
                top:annot.css('top').int(), left:annot.css('left').int(),
                width:annot.width(), height:annot.height(),
                textWidth : details.width(), text: content.html()
            }
            console.log(model);
            console.log(uid);
            $.ajax({
                url: annotUrl.format(imgName, uid?uid:""),
                type: uid?"PUT":"POST",
                data: JSON.stringify(model),
                contentType: "application/json"
            }).done(function (msg) {
                console.log(uid)
                if(uid == null) uid = msg.uid;
                console.log(uid)
                console.log("Data Saved: " + msg);
            }).fail(function (msg) {
                console.log("Data Saved: " + msg);
            });
        });

        content.html(model.text);
        return annot;
    }
})