/* FEEDER COMMANDS */

//TODO: fix bugs: formatting, update workspace in nonhighlighting mode and switch to high* makes text dissapear
var $ = jQuery;
var feederService = {
  url : "http://ubirepo.appspot.com/",
  serviceUrl : "http://ubirepo.appspot.com/service",
  REQ_GETFEED : 0,
  REQ_SETFEED : 1,
  REQ_GETFEEDJS : 2,
  REQ_CREATEFEED : 3,
  REQ_DELETEFEED : 4,
  REQ_FEEDLIST : 5,
  
  errors : {
    brqc : 'Bad request code.',
    irq : 'Invalid request.',
    mtpl : 'Multiple feed with same name.',
    nsfe : 'No such feed exists.',
    fae : 'Feed already exists.'
  },
  
  callService : function(params, callback) {
    jQuery.ajax( {
      url : feederService.serviceUrl,
      cache : false,
      dataType : "json",
      type : "POST",
      data : params,
      success : function(response) {
        CmdUtils.log("RESPONSE: ", response);
        
        if (response.success) {
          callback(response.content);
        } else {
          var error = feederService.errors[response.details];
          if (error == "undefined" || !error) {
            error = response.details;
          }
          
          displayMessage("Error: " + error);
        }
      },
      error : function(xhr, status, error) {
        CmdUtils.log("ERROR: ", xhr, status, error);
        displayMessage("Error with server comunication!");
      }
    });
  },
  
  isEditorPage : function() {
    return CmdUtils.getDocument().location.href == "chrome://ubiquity/content/editor.html";
  },
  
  getWorkspaceContent : function() {
      return $('#editor', CmdUtils.getDocumentInsecure())[0].editor.getCode();
  },
  
  setWorkspaceContent : function(content) {
    $('#editor', CmdUtils.getDocumentInsecure())[0].editor.setCode(content);
  },
  
  doCommandPreview : function(pblock, input, msg, showFeeds) {
    if (!input.text) {
      pblock.innerHTML = "Specify the feed name.";
    } else {
      pblock.innerHTML = msg + "<b>" + input.text + "</b>.";
    }
    
    if(showFeeds && noun_type_feed.isLoaded()) {
      var feeds = noun_type_feed.getFeeds();
      if(feeds.length > 0) {
        pblock.innerHTML += "<br/><br/>Choose from: " + feeds.toString();
      }
    }
  }
};

var noun_type_feed = {
  _name : "feed name",
  _feeds : [],
  _loaded : false,
  
  getFeeds : function() {
      return noun_type_feed._feeds;
  },
  
  isLoaded : function () {
    return noun_type_feed._loaded;
  },
  
  addFeeds : function(feeds) {
    noun_type_feed._feeds = noun_type_feed._feeds.concat(feeds);
  },
  
  removeFeedName : function(feed) {
    noun_type_feed._feeds.splice(noun_type_feed._feeds.indexOf(feed), 1);
  },

  getSuggestions : function(text) {
    var suggestions = [];
    var feeds = noun_type_feed._feeds;
    var cnt = 0;
    
    for (var ind = 0; ind < feeds.length; ind++) {
      if (feeds[ind].match(text, "i")) {
        suggestions.push(CmdUtils.makeSugg(feeds[ind]));
      
        if (++cnt == 5) {
          break;
        }
      }
    }
  
    return suggestions;
  },

    suggest : function(text, html, callback) {
    //TODO: block another request while one is being performed
    if (!noun_type_feed._loaded) {
    feederService.callService( {
    'req' : feederService.REQ_FEEDLIST
    }, function(feeds) {
                noun_type_feed._loaded = true;
                noun_type_feed.addFeeds(feeds);
                callback(noun_type_feed.getSuggestions(text));
            });
        } else {
            return noun_type_feed.getSuggestions(text);
        }
          
        return [];
    }
};

/* This comand creates a feed. */
CmdUtils.CreateCommand( {
    name : "create-feed",
    icon : feederService.url + "/favicon.ico",
    homepage : feederService.url,
    author : {
        name : "Calin Avasilcai",
        email : "calin014@gmail.com"
    },
    license : "GPL",
    description : "Creates a feed.",
    help : "create-feed feed_name",
    takes : {
        "input" : /^\w+$/
    },

    preview : function(pblock, input) {     
      feederService.doCommandPreview(pblock, input, "Creates a feed with the name ", false);
   },

    execute : function(input) {
        if (!input.text)
            return;

        feederService.callService( {
            'req' : feederService.REQ_CREATEFEED,
            'feed' : input.text
        }, function() {
            noun_type_feed.addFeeds(input.text);
            displayMessage("Feed " + input.text + " was created succesfully.");
        });
    }
});

/* This comand deletes a feed. */
CmdUtils.CreateCommand( {
    name : "delete-feed",
    icon : feederService.url + "/favicon.ico",
    homepage : feederService.url,
    author : {
        name : "Calin Avasilcai",
        email : "calin014@gmail.com"
    },
    license : "GPL",
    description : "Deletes a feed.",
    help : "delete-feed feed_name",
    takes : {
        "input" : noun_type_feed
    },

    preview : function(pblock, input) {
      feederService.doCommandPreview(pblock, input, "Delete feed with name ", true);
    },

    execute : function(input) {
        if (!input.text)
            return;

        feederService.callService( {
            'req' : feederService.REQ_DELETEFEED,
            'feed' : input.text
        }, function() {
            noun_type_feed.removeFeedName(input.text);
            displayMessage("Feed " + input.text + " was deleted succesfully.");
        });
    }
});

/* This comand commits to a feed. */
CmdUtils.CreateCommand( {
    name : "commit-to-feed",
    icon : feederService.url + "/favicon.ico",
    homepage : feederService.url,
    author : {
        name : "Calin Avasilcai",
        email : "calin014@gmail.com"
    },
    license : "GPL",
    description : "Comits workspace content to an existent feed.",
    help : "commit-to-feed feed_name",
    takes : {
        "input" : noun_type_feed
    },

    preview : function(pblock, input) {
        feederService.doCommandPreview(pblock, input, "Commits workspace content to feed ", true);
    },

    execute : function(input) {
        if (!input.text)
            return;

        try {
            var content = feederService.getWorkspaceContent();
            feederService.callService( {
                "req" : feederService.REQ_SETFEED,
                "feed" : input.text,
                "cnt" : content
            }, function() {
                displayMessage("Content was successfully feed " + input.text
                        + ".");
            });
        } catch (e) {
            displayMessage(e.toString());
        }
    }
});

/* This comand updates from a feed. */
CmdUtils.CreateCommand( {
    name : "update-workspace",
    icon : feederService.url + "/favicon.ico",
    homepage : feederService.url,
    author : {
        name : "Calin Avasilcai",
        email : "calin014@gmail.com"
    },
    license : "GPL",
    description : "Updates the workspace with the contents of secified feed.",
    help : "update-workspace feed_name",
    takes : {
        "input" : noun_type_feed
    },

    preview : function(pblock, input) {
      feederService.doCommandPreview(pblock, input, "Updates workspace with the content of feed ", true);
    },

    execute : function(input) {
        if (!input.text)
            return;

        feederService.callService( {
            'req' : feederService.REQ_GETFEED,
            'feed' : input.text
        }, function(content) {
            try {
                feederService.setWorkspaceContent(content);
                displayMessage("Workspace was updated succesfully with feed "
                        + input.text);
            } catch (e) {
                displayMessage(e.toString());
            }
        });
    }
});
/* FEEDER COMMANDS */