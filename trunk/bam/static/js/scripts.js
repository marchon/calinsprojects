//http://blogs.jetbrains.com/idea/2010/11/javascript-libraries-in-intellij-idea-10/

//http://code.google.com/apis/chart/
$(document).ready(function() {
    $("#main").accordion();

    var catnb = 0;
    $('#create').click(function(){
        catnb ++;
        $.get('/category',
            {op: "add", name: "cat" + catnb, rule: "rule" + catnb},
            function(data) {
                alert(data.message);
            },
            'json'
        ).error(function(){
                alert('Check server!');
        });
    });

    var tmpl = $('#categs').compile(false);
    $("#test").click(function(){
        $.get('/category?op=list', function(data) {
            $('#categs').autoRender({'categ': data.content}, tmpl);
        }, 'json');
    });
});