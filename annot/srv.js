var express = require('express');
var fs = require('fs');

var app = express();
var clientfolder = __dirname + '/client';
var datafolder = __dirname + '/data';

String.prototype.int = function () {
    var i = parseInt(this);
    if(isNaN(i)) throw new Error();
    return i
}

function writeAnnotation(name, annotation, cb) {
	fs.writeFile(datafolder + "/" + name + "/annot.json", JSON.stringify(annotation), cb);
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
				if(!err) writeAnnotation(req.body.name, [], function (err) {
					if(!err) res.send(200);
					else res.send(500);
				});
				else res.send(500);
			});
		});
	});
});


app.post("/pics/:name/annot", function(req, res) {
    fs.readFile(datafolder + "/" + req.params.name + "/annot.json", function (err, data) {
        if(!err) {
            var annotations = JSON.parse(data);
            try {
                console.log(req.body);
                annotations.push(req.body);
            } catch (err) {
                res.send(400);
            }
            writeAnnotation(req.params.name, annotations, function (err) {
                if(!err) res.send(200, {uid: annotations.length - 1});
                else res.send(500);
            });
        }
        else res.send(500);
    });
});
app.put("/pics/:name/annot/:uid", function(req, res) {
    fs.readFile(datafolder + "/" + req.params.name + "/annot.json", function (err, data) {
        if(!err) {
            var annotations = JSON.parse(data);
            try {
                annotations[req.params.uid.int()] = req.body;
            } catch (err) {
                res.send(400);
            }
            writeAnnotation(req.params.name, annotations, function (err) {
                if(!err) res.send(200);
                else res.send(500);
            });
        }
        else res.send(500);
    });
});
app.delete("/pics/:name/annot/:id", function(req, res) {
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