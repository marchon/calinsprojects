//http://blogs.jetbrains.com/idea/2010/11/javascript-libraries-in-intellij-idea-10/

Jaml.register('cat', function(cat){
    div(
        h1(cat.name),
        p(cat.rule)
    )
});

$(document).ready(function() {
    $("#main").accordion();

    $("#test").click(function(){
        $.get('/category?op=list', function(data) {
            $("#testcontent").html(Jaml.render('cat', data.content));
        }, 'json');
    });
});