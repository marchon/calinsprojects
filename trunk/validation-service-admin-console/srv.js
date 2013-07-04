var express = require('express');
var rest = require('restler');

var app = express();

app.configure(function(){
  app.use(express.static(__dirname + "/client"));
  app.use(express.bodyParser());
});

app.get('/*',function(req,res,next){
    res.header('Content-Type' , 'application/json');
    next();
});

app.get("/store/*", function(req, res) {
	rest.get('http://localhost:8092/' + req.route.params[0]).on('complete', function(result) {
  		if (result instanceof Error) {
		    	sys.puts('Error: ' + result.message);
		} else {		
			res.send(result)
		}
	});
});

app.post("/store", function(req, res) {
});

app.put("/store", function(req, res) {
});

app.delete("/store", function(req, res) {
});

app.listen(3000);
