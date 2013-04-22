var express = require('express');
var fs = require('fs');

var app = express();
var clientfolder = __dirname + '/client';
var datafolder = __dirname + '/data';

function writeAnnotation(name, json, cb) {
	fs.writeFile(datafolder + "/" + name + "/annot.json", json, cb);
}

app.configure(function(){
  app.use(express.static(clientfolder));
  app.use("/pics", express.static(datafolder));
  app.use(express.bodyParser());
});

app.get("/pics", function(req, res) {
	fs.readdir(datafolder, function(err, list){res.send(list)});
});

app.post("/pics", function(req, res) {
	//TODO: checks
	var path = datafolder + '/' + req.body.name
	
	fs.readFile(req.files.image.path, function (err, data) {
		//TODO: assert jpg, handle errors
		fs.mkdir(path, function() {		
			fs.writeFile(path + "/img.jpg", data, function (err) {
				if(!err) writeAnnotation(req.body.name, "[]", function (err) {
					if(!err) res.send(200);
					else res.send(500);
				});
				else res.send(500);
			});
		});
	});
});

app.put("/pics/:name", function(req, res) {
	console.log(req.body)
	writeAnnotation(req.params.name, JSON.stringify(req.body), function (err) {
		if(!err) res.send(200);
		else res.send(500);
	});
});

app.delete("/pics/:name", function(req, res) {
	var path = datafolder + '/' + req.params.name
	
	fs.unlink(path + "/img.jpg", function(err) {
		fs.unlink(path + "/annot.json", function(err) {
			fs.rmdir(path, function(err) {
				if(!err) res.send(200);
				else res.send(500);
			});
		});
	})
});

app.listen(3000);