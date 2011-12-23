//http://code.google.com/apis/chart/
//http://pinchzoom.com/contact/
$(document).ready(function() {

    //SECTIONS
    var content = {
        'transactions' : $('#transactions'),
        'categories' : $('#categories').hide(),
        'upload' : $('#upload').hide(),
        'charts' : $('#charts').hide()
    };

    var last = content['transactions'];

    $('#menu li a').click(function(e) {
        //TODO: back button??
        e.preventDefault();
        var id = this.href.split('#')[1];
        last.hide();
        last = content[id].show();
    });


    //PURE TEMPLATES
    var catTemplate = content['categories'].find('table tbody');
    var catTemplateFunction = catTemplate.compile({
       'tr' : {
           'cat <- result' : {
               '.name input@value': 'cat.name',
               '.rule input@value': 'cat.rule'
           }
       }
    });


    var offset = 0;
    var next = content['categories'].find('.next').click(function(e){
        e.preventDefault();
        offset += 1;
        getNext(offset);
        if(offset == 1) prev.show();
    });
    var prev = content['categories'].find('.prev').hide().click(function(e){
        e.preventDefault();
        offset -= 1;
        getNext(offset);
        if(offset == 0) prev.hide();
    });

    var getNext = function(offset) {
        $.post(
            'crud',
            {
                json: JSON.stringify({
                        "ent":"Category",
                        "op":"lst",
                        "params": {
                            "limit": 1,
                            "offset": offset
                        }
                    }
                )
            },
            function(data) {
                if(data.type == 'success') {
                    catTemplate = catTemplate.render(data.content, catTemplateFunction);
                    if(data.content.hasNext) {
                        next.show();
                    } else {
                        next.hide();
                    }
                } else {
                    alert('Error: ' + data.message);
                }
            },
            'json'
        ).error(function() {
            alert('Error connecting to server.')
        });
    };
    getNext(offset);
});