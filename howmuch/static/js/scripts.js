//http://code.google.com/apis/chart/
//http://pinchzoom.com/contact/

$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

$(document).ready(function() {
    //TODO: cookie is not passed
    $('#file_upload').uploadify({
        'uploader'  : '/static/uploadify/uploadify.swf',
        'script'    : '/upload',
        'cancelImg' : '/static/uploadify/cancel.png',
        'auto'      : false,
        'fileDataName' : 'data',
        'multi'       : true,
        'onComplete'  : function(event, ID, fileObj, response, data) {
            console.log(response);
        }
    });

    $('#upload a').click(function(e) {
        e.preventDefault();
        $('#file_upload').uploadifyUpload();
    });

    //SECTIONS
    var content = {
        'transactions' : $('#transactions'),
        'categories' : $('#categories').hide(),
        'upload' : $('#upload').hide(),
        'charts' : $('#charts').hide()
    };

    var ajaxDisable = $('.ajaxDisable');

    var last = content['transactions'];

    $('#menu li a').click(function(e) {
        //TODO: back button??
        e.preventDefault();
        var id = this.href.split('#')[1];
        last.hide();
        last = content[id].show();
    });


    //PURE TEMPLATES
    var catTemplate = content['categories'].find('table.list tbody');
    var catTemplateFunction = catTemplate.compile({
       'tr' : {
           'cat <- result' : {
               '.name input@value': 'cat.name',
               '.rule input@value': 'cat.rule'
           }
       }
    });

    var createForm = $('#categories .create'), list = $('#categories .list');
    createForm.hide();

    $('.new').click(function() {
        createForm.show();
        list.hide();
    });


    var offset = 0, pagesize = 10, filter = {};

    function doList(o, l, f) {
        crud('Category', 'lst', {
            "offset": o,
            "limit": l,
            "filter": f
        }, function(content){
            catTemplate = catTemplate.render(content, catTemplateFunction);
            var pageDetail = $('.list tfoot em');

            var current = offset / pagesize + 1;
            var total = Math.ceil(content['total'] / pagesize);
            pageDetail.first().html(current);
            pageDetail.last().html(total);

            if(offset == 0) {
                $('tfoot a.prev').hide();
            } else {
                $('tfoot a.prev').show();
            }

            if(current == total) {
                $('tfoot a.next').hide();
            } else {
                $('tfoot a.next').show();
            }
        }, function(err){
            //TODO: what to do?;))
        });
    }

    doList(offset, pagesize, filter);

    $('tfoot a.prev').click(function() {
        if(offset > 0) {
            offset -= pagesize;
            doList(offset, pagesize, filter);
        }
    });
    $('tfoot a.next').click(function() {
        offset += pagesize;
        doList(offset, pagesize, filter);
    });

    createForm.submit(function() {
        //TODO: move this at central point of ajax call?
        ajaxDisable.attr('disabled', true);
        crud('Category', 'put', [createForm.serializeObject()], function(content) {
            ajaxDisable.removeAttr('disabled');
            // empty content
            createForm.hide();
            list.show();

            offset = 0;
            filter = {};
            doList(offset, pagesize, filter);
        }, function(err) {
            //TODO: what to do?;))
        });

        return false;
    });

});

/**
 *
 * @param ent entity that supports crud operations
 * @param op "put" | "del" | "lst"
 * @param params see server doc
 * @param success callback
 * @param failure callback
 */
function crud(ent, op, params, success, failure) {
    $.post(
        'crud', {
            json: JSON.stringify({
                    "ent": ent,
                    "op": op,
                    "params": params
                }
            )
        }, function(data) {
            notification(data.message);

            if(data.type == 'success') {
                success(data.content);
            } else {
                failure(data.type);
            }
        },
        'json'
    ).error(function() {
            //TODO: use notification,
            alert('Error connecting to server.')
    });
}

function notification(message) {
    $('#notification div').show().html(message).slideDown(300).delay(1000).slideUp(300);
}