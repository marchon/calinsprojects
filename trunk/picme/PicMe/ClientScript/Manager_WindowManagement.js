/* Miscellaneous */

/* opened sets tracking */
Array.prototype.exists = function (x)
{
    for (var i = 0; i < this.length; i++) 
        if (this[i] == x) return true;
        
    return false;
}
Array.prototype.remove = function(s)
{
    for(var i = 0; i < this.length; i++)
        if(s == this[i])
        {
            this.splice(i, 1);
            break;
        }
}
Array.prototype.removeAll = function()
{
    this.splice(0, this.length);
}
/* opened sets tracking */

/* random numbers betweeb min and max */
function generateRandomNumber(min, max)
{
    if(min > max)
    {
        a = min;
        min = max;
        max = a;
    }
    
    return (min + Math.floor(Math.random() * (max - min))); //min<=rand<max
};
/* random numbers betweeb min and max */

/* cancel event bubbleing */
function cancelEventBubbleing(e)
{
    e.cancelBubble = true;
	if (e.stopPropagation) e.stopPropagation();
};
/* cancel event bubbleing */

/* mouse position */
function mouseCoords(ev)
{   
	if(ev.pageX || ev.pageY)
	{
		return {x:ev.pageX, y:ev.pageY};
	}

	return {
	    x: (ev.clientX + document.documentElement.scrollLeft - document.body.clientLeft), 
	    y: (ev.clientY + document.documentElement.scrollTop - document.body.clientTop)
	};
};
/* mouse position */

/* get control bounding box */
function getTopLeft(e)
{
	var left = 0;
	var top  = 0;

	while (e)
	{
	    left += e.offsetLeft;
	    top  += e.offsetTop;
	    e     = e.offsetParent;
	}
	
	return {x:left, y:top};
};

function getBottomRight(e, topLeft)
{    
    if(!topLeft)
        topLeft = getTopLeft(e);
        
    return {x:e.offsetWidth + topLeft.x, y:e.offsetHeight + topLeft.y};
};
/* get control bounding box */

/* set opacity */
function setOpacity(obj, opacity, doNotTryIeFix) 
{ 
  // IE/Win
  obj.style.filter = "alpha(opacity:"+opacity+")";
  if(obj.parentNode && obj.parentNode.style && (doNotTryIeFix == null) )
    obj.parentNode.style.filter = "alpha(opacity:100)"; //asta e chiar aiurea:|
  
  // Safari<1.2, Konqueror
  obj.style.KHTMLOpacity = opacity/100;
  
  // Older Mozilla and Firefox
  obj.style.MozOpacity = opacity/100;
  
  // Safari 1.2, newer Firefox and Mozilla, CSS3
  obj.style.opacity = opacity/100;
};
/* set opacity */

/* fade effect */
/* fade in effect */
function fadeIn(objId, opacity, timeBetweenChanges, handlerAfterFade) 
{
    if (document.getElementById) 
    {
        obj = document.getElementById(objId);
        if (obj) 
        {
            if(opacity <= 100)
            {
                setOpacity(obj, opacity);
                opacity += 10;
                window.setTimeout("fadeIn('" + objId + "'," + opacity + ", " + 
                    timeBetweenChanges + ", " + handlerAfterFade + ")", timeBetweenChanges);
            }
            else if(handlerAfterFade != null)
            {
                handlerAfterFade(objId);
            }
        }
    }
};
/* fade in effect */

/* fade out effect */
function fadeOut(objId, opacity, timeBetweenChanges, handlerAfterFade)
{
    if (document.getElementById) 
    {
        obj = document.getElementById(objId);
        if(obj)
        {
            if (opacity >= 0) 
            {
                  setOpacity(obj, opacity);
                  
                  opacity -= 10;
                  
                  window.setTimeout("fadeOut('" + objId + "'," + opacity + ", " + 
                    timeBetweenChanges + ", " + handlerAfterFade + ")", timeBetweenChanges);
            }
            else if(handlerAfterFade != null)
            {
                handlerAfterFade(objId);
            }
        }
    }
};
/* fade out effect */
/* fade effect */

/* text selection disableing */
function disableTextSelection(control)
{
    if (typeof control.onselectstart != "undefined") //IE route
        control.onselectstart = function(){return false;}
    else if (typeof control.style.MozUserSelect != "undefined") //Firefox route
        control.style.MozUserSelect = "none";
    else //All other route (ie: Opera)
        control.onmousedown = function(){return false;}
    control.style.cursor = "default";
};
/* text selection disableing */

/* get inner text */
function getTextContent(el)
{ // by Kravvitz of DynamicSiteSolutions.com
  var text='',i=0,kids,n; // DOM1+ and IE4
  
  if(!arguments[1]) text=el.textContent||el.innerText||
    ((!(n=el.nodeName)||!/^body|object$/i.test(n))?(el.text||el.data||''):'');
    
  if((!text || /^\s*$/.test(text)) && (kids=el.childNodes))
    while(el=kids[i++])
      switch(el.nodeType)
      {
        case 1:text=text+getTextContent(el,true);break;
        case 8:break;
        default:text=text+el.nodeValue;break;
      }
      
  return text;
}
/* get inner text */

/* get elements by class name */
function getElementsByClassName(oElm, strTagName, strClassName)
{
	var arrElements = (strTagName == "*" && oElm.all)? oElm.all : oElm.getElementsByTagName(strTagName);
	var arrReturnElements = new Array();
	strClassName = strClassName.replace(/\-/g, "\\-");
	var oRegExp = new RegExp("(^|\\s)" + strClassName + "(\\s|$)");
	var oElement;
	for(var i=0; i<arrElements.length; i++)
	{
		oElement = arrElements[i];
		if(oRegExp.test(oElement.className))
		{
			arrReturnElements.push(oElement);
		}
	}
	return (arrReturnElements)
}
/* get elements by class name */

/* Miscellaneous */

/* Utils */
/* message box */
var messageBox = null;

function MessageBox(caption, message, html, buttonList, handlerList)
{
    if(messageBox || !buttonList || !handlerList ||
        buttonList.length != handlerList.length)
        return;
    
    BlurControl(pageControl);
    
    messageBox = document.createElement('div');
    messageBox.className = 'MessageBox';
    
    messageBox.style.position = 'absolute';
    
    messageBox.style.zIndex = "10";
    
    var buttonsString = new String();
    
    messageBox.handlers = handlerList;
    
    for(var i = buttonList.length - 1; i >= 0; i--)
        buttonsString += "<div class=\"button\" onclick=\"CloseMessageBox(" + i + 
            ");\">" + buttonList[i] + "</div>"
    
    messageBox.innerHTML = "<div class=\"Caption\">" + ((caption == null) ? "MessageBox" : caption) +
                     "</div>" + ((message == null) ? "" : ("<p>" + message + "</p>")) + 
                     ((html == null) ? "" : (html)) + 
                     "<div>" + buttonsString + "</div>";
                
    pageControl.appendChild(messageBox); 
    var topLeft = getTopLeft(pageControl);
    
    messageBox.style.left = (topLeft.x + pageControl.offsetWidth / 2 - messageBox.offsetWidth / 2) + 'px';
    messageBox.style.top = (topLeft.y + pageControl.offsetHeight / 2 - messageBox.offsetHeight / 2) + 'px';
};

function CloseMessageBox(hwnd)
{
    var handler = null;
    var args = new Array();
    
    if(messageBox.handlers != null)
        if(messageBox.handlers[hwnd] != null && messageBox.handlers[hwnd].length > 0)
        {
            handler = messageBox.handlers[hwnd][0];
            
            for(var i = 1; i < messageBox.handlers[hwnd].length; i++)
                args.push(eval(messageBox.handlers[hwnd][i]));
        }

    UnblurControl(pageControl);

    pageControl.removeChild(messageBox);
    messageBox = null;
        
    if(handler != null)  
        handler(args);
           
};
/* message box */

/* loading message */
function ShowLoading(control, blurControl)
{
    if(blurControl)
        BlurControl(control);
        
    var loadingMessage = document.createElement('p');
    loadingMessage.className = 'LoadingMessage';

    if(blurControl)
    {
        var topLeft = getTopLeft(control);
        
        loadingMessage.style.position = 'absolute';
        
        loadingMessage.style.zIndex = "10";
    }
    
    control.loadingMsg = loadingMessage;
    control.appendChild(loadingMessage);
    
    if(blurControl)
    {
        loadingMessage.style.left = (topLeft.x + control.offsetWidth / 2 - loadingMessage.offsetWidth / 2) + 'px';
        loadingMessage.style.top = (topLeft.y + control.offsetHeight / 2 - loadingMessage.offsetHeight / 2) + 'px';
    }
    else
    {
        loadingMessage.style.marginTop = (control.offsetHeight / 2 - loadingMessage.offsetHeight / 2) + 'px';
    }
}
function HideLoading(control)
{
    if(control.loadingMsg)
    {
        UnblurControl(control);
        
        control.removeChild(control.loadingMsg);
        
        control.loadingMsg = null;
    }
}
/* loading message */

/* blur control */
function BlurControl(control)
{
    if(typeof control.blurControlRef == "undefined" || !control.blurControlRef)
    {
        blurControl = document.createElement('div');
        blurControl.style.position = 'absolute';
        
        var topLeft = getTopLeft(control);
        
        blurControl.style.left = topLeft.x + 'px';
        blurControl.style.top = topLeft.y + 'px';
        blurControl.style.width = control.offsetWidth + 'px';
        blurControl.style.height = control.offsetHeight + 'px';
        
        blurControl.style.backgroundColor = 'Black';
        setOpacity(blurControl, 70);
        
        blurControl.style.zIndex = "5";
        
        control.blurControlRef = blurControl;
        control.appendChild(blurControl);
    }
};
function UnblurControl(control)
{
    if(typeof control.blurControlRef != "undefined" && control.blurControlRef)
    {
        control.removeChild(control.blurControlRef);       
        control.blurControlRef = null;
    }
};
/* blur control */

/* server errors */
function ServerCommunicationError(response)
{
    HideLoading(pageControl);
    
    MessageBox("Comunication error...", 
                "Could not establish connection with server! Please ceck your internet connection.",
                null, new Array('Ok'), new Array(null));
}

function InternalServerError(response)
{
    HideLoading(pageControl);
    
    MessageBox("Internal error...", 
                "There were some errors on the server while performing the required action. Sorry for the inconvenience. Please try again later.",
                null, new Array('Ok'), new Array(null));
}
/* server errors */

/* add pics to control */
function addPicToPictureStreamControl(id, name)
{
    var pic = document.createElement('div');
    pic.picId = id;
    pic.picName = name;
    pic.ondblclick = function(){setPicDetails(this);};
    pic.onmousedown = function(ev){selectItem(ev, this); preparePicMoveing(this);}
    var img = document.createElement('div');
    img.style.backgroundImage = 'url(Pictures/Thumbs/' + pic.picId + '.jpeg)';
    pic.appendChild(img);
                    
    pictureStreamControl.appendChild(pic);
    
    return pic;
};

function addPicToWindowContent(content, id, name)
{
    var imgId = content.getAttribute('id') + '_' + id;
    
    var pic = document.createElement('div');
    pic.picId = id;
    pic.picName = name;
    pic.ondblclick = function(){setPicDetails(this);};
    pic.onmousedown = function(ev){selectItem(ev, this); preparePicMoveing(this); Focus(this.parentNode.parentNode);};
    
    var img = document.createElement('div');
    img.setAttribute('id', imgId);
    img.style.backgroundImage = 'url(Pictures/Thumbs/' + pic.picId + '.jpeg)';
    pic.appendChild(img);
    
    var nameParagraph = document.createElement('p');
    nameParagraph.innerHTML = pic.picName;
    pic.appendChild(nameParagraph);
    
    content.appendChild(pic);
    
    return pic;
};
/* add pics to control */

/* pic drag and drop for moving */
var img2move = null;
var destinationWindow = -1;
var moveTimer = null;

function preparePicMoveing(pic)
{
    var clearEvents = function()
                      {
                        pic.onmousemove = null; 
                        pic.onmouseup = null;
                        pic.onmouseout = null;
                      }
                      
    var counter = 0;
                      
    pic.onmousemove = function(ev)
                    {
                        counter ++;
                        if(counter == 3)
                        {
                            ev  = ev || window.event;
                            var pos = mouseCoords(ev);
 
                            var img = this;
                            
                            setOpacity(img, 40);
    
                            img2move = document.createElement('div');
                            img2move.srcObj = img;
                            img2move.style.backgroundImage = 'url(Pictures/Thumbs/' + img.picId + '.jpeg)';
                            img2move.style.width = '80px';
                            img2move.style.height = '80px';
                            img2move.style.position = 'absolute';
                            img2move.style.zIndex = "100";

                            img2move.setAttribute('id', 'img2move');
                            setOpacity(img2move, 40);
                            
                            workSpaceControl.appendChild(img2move);
                            
                            img2move.style.top = pos.y - img2move.offsetHeight / 2 + 'px';
                            img2move.style.left = pos.x - img2move.offsetWidth / 2 + 'px';
                            
                            beginDrag(img2move.getAttribute('id'), ev);
                            
                            moveTimer = setInterval('picMoveingHandler();', 200); 
                            
                            clearEvents();
                            deselectAllItems(img.parentNode);
                        }
                    }
    pic.onmouseup = clearEvents;
    pic.onmouseout = clearEvents;
};

function stopPicMoveing()
{
    if(img2move == null)
        return;
        
    if(destinationWindow >= 0 && 
            openedWindows[destinationWindow].childNodes[1] != img2move.srcObj.parentNode)
    {     
    
        var temp_img2move = img2move;
        var temp_destinationWindow = destinationWindow;
        
        var pic = addPicToWindowContent(openedWindows[temp_destinationWindow].childNodes[1], 
                                        temp_img2move.srcObj.picId, temp_img2move.srcObj.picName);
        var disp = temp_img2move.srcObj.style.display;
        temp_img2move.srcObj.style.display = 'none';
    
        var copyHandler = function(response)
                            {
                                if(response.length > 0)
                                {
                                    MessageBox('Error...', 'This item could not be moved. Please note that you can\'t have the same pic more than once in the same set.',
                                        null, new Array('Yes'), new Array(null));
                                        
                                    temp_img2move.srcObj.style.display = disp;
                                    setOpacity(temp_img2move.srcObj, 100);
                                     
                                    pic.parentNode.removeChild(pic);
                                }
                                else
                                {
                                    temp_img2move.srcObj.parentNode.removeChild(temp_img2move.srcObj);
                                    fadeThumb(pic.firstChild.getAttribute('id'));   
                                }
                                
                                temp_img2move = null; ///just to be sure:D
                            };
        var serverError = function(response)
                            {
                                ServerCommunicationError();
                                
                                temp_img2move.srcObj.style.display = disp;
                                setOpacity(temp_img2move.srcObj, 100);
                                 
                                pic.parentNode.removeChild(pic);
                            }
                             
        if(temp_img2move.srcObj.parentNode == pictureStreamControl)                        
            Ajax_WebService.CopyPictureInSet(
                new Array(img2move.srcObj.picId),
                openedWindows[destinationWindow].getAttribute('id'),
                copyHandler,
                serverError, serverError);
        else
        {
            Ajax_WebService.MovePictureInSet(
                new Array(img2move.srcObj.picId),
                temp_img2move.srcObj.parentNode.parentNode.getAttribute('id'),
                openedWindows[destinationWindow].getAttribute('id'),
                copyHandler,
                serverError, serverError);
        }
        
        destinationWindow = -1;
    }
    else
        setOpacity(img2move.srcObj, 100);
        
    workSpaceControl.removeChild(img2move);
    
    clearInterval(moveTimer);
    
    moveTimer = null;
    img2move = null;
};

function picMoveingHandler()
{
    if(img2move == null)
    {
        clearInterval(moveTimer);
        moveTimer = null;
        return;
    }
    
    
    var x = parseInt(img2move.style.left) + img2move.offsetWidth / 2;
    var y = parseInt(img2move.style.top) + img2move.offsetHeight / 2;
    
    for(var i = openedWindows.length - 1; i >= 0; i--)
    {
        var winX = parseInt(openedWindows[i].style.left);
        var winY = parseInt(openedWindows[i].style.top);
        var winW = openedWindows[i].offsetWidth;
        var winH = openedWindows[i].offsetHeight;
        
        if(x >= winX && x <= winX + winW &&
            y >= winY && y <= winY + winH)
                break;
    }
    
    if(i < 0)
    {
        if(destinationWindow >= 0)
        {
            destinationWindow = -1;
            setOpacity(img2move, 40);
        }
    }
    else
        if(destinationWindow != i)
        {
            destinationWindow = i;
            setOpacity(img2move, 100);
            Focus(openedWindows[destinationWindow]);
        }     
};
/* pic drag and drop for moving */
/* Utils */


/* Page Control */
var pageControl = null;
/* Page Control */

/* taskbar */
var taskbarControl = null;
var buttonList = null;

function prepareTaskbarButtons()
{
    if(taskbarControl == null)
        return;
        
    buttonList = taskbarControl.getElementsByTagName('div');
    
    buttonList[0].onmousedown = function(){pressButton(this);};
    buttonList[0].onmouseout = releaseButton;
    buttonList[0].onmouseup = function(){ if(this.className != ''){ releaseButton(); newSetCommand(); } };
    
    buttonList[1].onmousedown = function(){pressButton(this);};
    buttonList[1].onmouseout = releaseButton;
    buttonList[1].onmouseup = function(){ if(this.className != ''){ releaseButton(); editSetCommand(); } };
    
    buttonList[2].onmousedown = function(){pressButton(this);};
    buttonList[2].onmouseout = releaseButton;
    buttonList[2].onmouseup = function(){ if(this.className != ''){ releaseButton(); deleteSetCommand(); } };    
    
    buttonList[3].onmousedown = function(){pressButton(this);};
    buttonList[3].onmouseout = releaseButton;
    buttonList[3].onmouseup = function(){ if(this.className != ''){ releaseButton(); deletePicCommand(); } };
};

function workspaceSelectionChangedHandler(workspace)
{
    var nbSelItms = getSelectedItemList(workspace).length;
    
    switch(nbSelItms)
    {
        case 0:
            buttonList[1].className = '';
            buttonList[2].className = '';
        break;
        case 1:
            buttonList[1].className = 'ValidButton';
            buttonList[2].className = 'ValidButton';
        break;
        default:
            buttonList[1].className = '';
    };
};

function pictureStreamSelectionChangedHandler(pictureStream)
{
    var nbSelItms = getSelectedItemList(pictureStream).length;
    
    if(nbSelItms > 0) 
        buttonList[3].className = 'ValidButton';
    else
        buttonList[3].className = '';
}

function addTaskbarEntry(window)
{
    var entry = document.createElement('p');
    entry.setAttribute('id', (window.getAttribute('id') + '_task'));
    
    entry.className = taskbarControl.selectedClass;
    entry.innerHTML = window.windowName;
    entry.windowReference = window;
    window.entryReference = entry;
    
    entry.onmousedown = function(){Focus(this.windowReference);};
    
    taskbarControl.appendChild(entry);
};

function removeTaskbarEntry(window)
{
    entry = document.getElementById((window.getAttribute('id') + '_task'));
    entry.windowReference = null;
    window.entryReference = null;
    
    taskbarControl.removeChild(entry);
};

/* taskbar commands */

/* new set command */
function newSetCommand()
{
    MessageBox("Create set...", "Please choose a name and a description for the new set.",
        "<p>Name: <input name=\"set_name\" type=\"text\" id=\"set_name_input_id\" /></p>" +
        "<p>Description: <textarea name=\"set_descr\" rows=\"2\" cols=\"20\" type=\"text\" id=\"set_descr_input_id\"></textarea></p>",
        new Array('Create...', 'Cancel'), 
        new Array(new Array(
            function(args)
            {
                ShowLoading(pageControl, true);
                
                Ajax_WebService.CreateNewSet(args[0], args[1], 
                    function(response)
                    {
                        HideLoading(pageControl);
                        if(!response)
                            InternalServerError();
                        else
                        {
                            AddSetToWorkspace(response, args[0]);
                        }
                    }, ServerCommunicationError, ServerCommunicationError);  
            }, 'document.getElementById(\'set_name_input_id\').value',
            'document.getElementById(\'set_descr_input_id\').value'), null));
};
/* new set command */

/* edit set command */
function editSetCommand()
{
    var selectedSets = getSelectedItemList(workSpaceControl);
    
   
    ShowLoading(pageControl, true);

    Ajax_WebService.GetSetDescription(selectedSets[0].setId,
        function(description)
        {
            HideLoading(pageControl);
            if(description == null)
                InternalServerError()
            else
                MessageBox("Edit set...", "Modify the fields and then hit save.",
                    "<p>Name: <input name=\"set_name\" type=\"text\" id=\"set_name_input_id\" value=\"" + selectedSets[0].setName + "\"/></p>" +
                    "<p>Description: <textarea name=\"set_descr\" rows=\"2\" cols=\"20\" type=\"text\" id=\"set_descr_input_id\">" + description + "</textarea></p>",
                    new Array('Save...', 'Cancel'), 
                    new Array(new Array(
                    function(args)
                    {
                        ShowLoading(pageControl, true);
                        
                        Ajax_WebService.UpdateSet(selectedSets[0].setId, args[0], args[1],
                            function(response)
                            {
                                HideLoading(pageControl);
                                
                                if(!response)
                                    InternalServerError();
                                else
                                {
                                    selectedSets[0].setName = args[0];
                                    selectedSets[0].firstChild.innerHTML = args[0]; 
                                }
                            }, ServerCommunicationError, ServerCommunicationError); 
                    }, 'document.getElementById(\'set_name_input_id\').value',
                    'document.getElementById(\'set_descr_input_id\').value'), null));                      
        }, ServerCommunicationError, ServerCommunicationError);
        
};
/* edit set command */

/* delete set command */
function deleteSetCommand()
{
    var selectedSetsForDelete = getSelectedItemList(workSpaceControl);
    
    if(selectedSetsForDelete != null && selectedSetsForDelete.length > 0)
    {  
        MessageBox('Confirm delete...', 'Are you sure you want to delete ' + 
                    ((selectedSetsForDelete.length == 1)? ('<b>' + selectedSetsForDelete[0].setName +
                    '</b>'):('these ' + selectedSetsForDelete.length + ' items')) + '?', null,  
                    new Array('Yes', 'No'), 
                    new Array(new Array(
                        function()
                        {
                            var params = new Array();
    
                            for(var i = 0; i < selectedSetsForDelete.length; i++)
                                params.push(selectedSetsForDelete[i].setId);
                                
                            ShowLoading(pageControl, true);
                            Ajax_WebService.DeleteSets(params,
                                function(response)
                                {
                                    HideLoading(pageControl);
                                    
                                    if(response.UndeletedSets.length > 0)
                                        InternalServerError();
                                    
                                    for(var i = 0; i < selectedSetsForDelete.length; i++)
                                        if(!response.UndeletedSets.exists(selectedSetsForDelete[i].setId))
                                            workSpaceControl.removeChild(selectedSetsForDelete[i]);
                                            
                                    deselectAllItems(workSpaceControl);
                                    
                                    /* add new uncategorized pictures to picture stream */
                                    if(response.RefreshedUncategorizedPictures != null)
                                        for(var i = 0; i < response.RefreshedUncategorizedPictures.length; i++)
                                            addPicToPictureStreamControl(response.RefreshedUncategorizedPictures[i].Id,
                                                                        response.RefreshedUncategorizedPictures[i].Name);
                                    /* add new uncategorized pictures to picture stream */
                                }, 
                                ServerCommunicationError, ServerCommunicationError);
                        }
                    ), null));
    }
};
/* delete set command */

/* delete pic command */
function deletePicCommand()
{
    selectedPicsForDelete = getSelectedItemList(pictureStreamControl);
    
    if(selectedPicsForDelete != null && selectedPicsForDelete.length > 0)
    {
        MessageBox('Confirm delete...', 'Are you sure you want to delete ' + 
                    ((selectedPicsForDelete.length == 1)? ('<b>' + selectedPicsForDelete[0].picName +
                     '</b>'):('these ' + selectedPicsForDelete.length + ' items')) + '?' + 
                     '<br /><b>Warning:</b> This will permanently delete the selected pic(s).', null, 
                     new Array('Yes', 'No'), 
                     new Array(new Array(
                        function()
                        {
                            ShowLoading(pageControl, true);
                        
                            var args = new Array();
                            for(var i = 0; i < selectedPicsForDelete.length; i++)
                                args.push(selectedPicsForDelete[i].picId);
                            
                            Ajax_WebService.PermanentlyDeletePics(args,
                                function(response)
                                {
                                    HideLoading(pageControl);
                                    
                                    if(response.length > 0)
                                        InternalServerError();
                                    
                                    for(var i = 0; i < selectedPicsForDelete.length; i++)
                                        if(!response.exists(selectedPicsForDelete[i].picId))
                                            pictureStreamControl.removeChild(selectedPicsForDelete[i]);
                                            
                                    deselectAllItems(pictureStreamControl);
                                },
                                ServerCommunicationError, ServerCommunicationError);
                        }
                     ), null));
    }
};
/* delete pic command */

/* taskbar commands */

/* taskbar */

/* Picture stream */
var pictureStreamControl = null;

var Timer;

function prepareScroll(scrollUpButtonId, scrollDownButtonId)
{
    var scrollUpButton = document.getElementById(scrollUpButtonId);
    var scrollDownButton = document.getElementById(scrollDownButtonId);
    
    scrollUpButton.onmousedown = function()
                                {
                                    this.className = 'ScrollUpPressed';
                                    Timer = setInterval("adjScroll(-4)", 15); 
                                };
    scrollUpButton.onmouseup = function()
                            {
                                this.className = 'ScrollUp';
                                clearInterval(Timer);
                            };
    scrollUpButton.onmouseout = function()
                            {
                                this.className = 'ScrollUp';
                                clearInterval(Timer);
                            };
                            
    scrollDownButton.onmousedown = function()
                                {
                                    this.className = 'ScrollDownPressed';
                                    Timer = setInterval("adjScroll(4)", 15); 
                                };
    scrollDownButton.onmouseup = function()
                            {
                                this.className = 'ScrollDown';
                                clearInterval(Timer);
                            };
    scrollDownButton.onmouseout = function()
                            {
                                this.className = 'ScrollDown';
                                clearInterval(Timer);
                            };            
};

function adjScroll(val)
{
    pictureStreamControl.scrollTop += val;
};
/* Picture stream */

/* set stacking */
var openedWindows = new Array();
var maxOpenedWindows = null;

function registerWindow(handle)
{    
    openedWindows.push(handle);
    handle.onmousedown = function(ev){ev = ev || window.event;
                    cancelEventBubbleing(ev);Focus(this);};
                    
    addTaskbarEntry(handle);
    Focus(handle); 
};

function unregisterWindow(handle)
{
    removeTaskbarEntry(handle);
    openedWindows.remove(handle);
    
    if(openedWindows.length > 0)
        Focus(openedWindows[openedWindows.length - 1]);
};

var currentFocus = null;
function Focus(window)
{
    if(currentFocus == window)
        return;
    
    currentFocus = window;
    
    var i;
    for(i = 0; i < openedWindows.length; i++)
        if(openedWindows[i] == window) break;
        
    if(i == openedWindows.length)
        return;
    
    var ref = openedWindows[i++];
    
    for(; i < openedWindows.length; i++)
    {
        openedWindows[i-1] = openedWindows[i];
        openedWindows[i-1].style.zIndex = ('' + i); 
    }
    
    openedWindows[i-1] = ref;
    openedWindows[i-1].style.zIndex = ('' + i);
    
    if(i - 2 >= 0)
    {
        openedWindows[i-2].entryReference.className = taskbarControl.unselectedClass;
        setOpacity(openedWindows[i-2], 70);
    }
    
    openedWindows[i-1].entryReference.className = taskbarControl.selectedClass;
    setOpacity(openedWindows[i-1], 100);
}
/* set stacking */

/* workspace */

/* reference to workspace */
var workSpaceControl = null;
/* reference to workspace */

/* workspace boundingbox */
var boundingBoxtopLeft = null;
var boundingBoxbottomRight = null;
/* workspace boundingbox */

/* workspace initialize */
function doWorkSpaceInit(WorkSpaceId, TaskBarId, PictureStreamId, ScrollUpButtonId, ScrollDownButtonId, maximumWindows)
{
    document.body.onmouseup = function(){ stopDrag(); stopPicMoveing(); };
    document.onkeydown = keyDown;
    document.onkeyup = keyUp;
        
    workSpaceControl = document.getElementById(WorkSpaceId);
    pageControl = workSpaceControl.parentNode;
    taskbarControl = document.getElementById(TaskBarId);
    pictureStreamControl = document.getElementById(PictureStreamId);
    
    ShowLoading(pageControl, true);
    
    prepareScroll(ScrollUpButtonId, ScrollDownButtonId);
    prepareTaskbarButtons();
    
    taskbarControl.unselectedClass = "TaskbarEntry";
    taskbarControl.selectedClass = "TaskbarEntrySelected";
    
    maxOpenedWindows = maximumWindows;
    
    disableTextSelection(pageControl);
    
    registerContentForItemSelect(workSpaceControl, "SetFolder_1", "SetFolder_2", "SetFolder_3", workspaceSelectionChangedHandler);
    registerContentForItemSelect(pictureStreamControl, "", "selectedImage", null, pictureStreamSelectionChangedHandler);
    
    boundingBoxtopLeft = getTopLeft(workSpaceControl);
    boundingBoxbottomRight = getBottomRight(workSpaceControl, boundingBoxtopLeft);
    
     Ajax_WebService.GetLoadInfo(
        function(response)
        {
            HideLoading(pageControl);
            for(var i = 0; i < response.SetList.length; i++)
                AddSetToWorkspace(response.SetList[i].Id, response.SetList[i].Name);
              
            var id = PictureStreamId + '_';
            for(var i = 0; i < response.PicList.length; i++)
                addPicToPictureStreamControl(response.PicList[i].Id, response.PicList[i].Name);
        }, 
        ServerCommunicationError, ServerCommunicationError);
};
/* workspace initialize */

/* add set to workspace */
function AddSetToWorkspace(id, name)
{
    var newSet = document.createElement('div');
    
    newSet.setAttribute('id', 'set_' + id);
    newSet.setId = id;
    newSet.setName = name;
    newSet.className = 'SetFolder_1';
    newSet.ondblclick = function(){openSet(id, name); deselectAllItems(workSpaceControl);};
    newSet.onmousedown = function(ev){selectItem(ev, this);};
    newSet.innerHTML = "<p>" + name + "</p>";
    
    workSpaceControl.appendChild(newSet);
}
/* add set to workspace */

/* workspace */


/* Set opening/closing */

/* open set */
function openSet(setId, setName)
{
    var control = document.getElementById(setId);
    if(control != null)
    {
        Focus(control);
    }
    else if(openedWindows.length < maxOpenedWindows)
    {
        setOpenItemLook(document.getElementById(("set_" + setId)));
        
        var newWindow = document.createElement('div');
        
        newWindow.setAttribute("id", setId);
        newWindow.className = "PopUpSet"; //se pare ca asta merge la toate
        
        newWindow.innerHTML = "<div class=\"PopUpCaption\" onmousedown=\"beginDrag('" + setId +
                    "', event)\"><div><img src=\"LookAndFeel/Images/win_close.gif\" alt=\"Close\"" +
                    " onclick=\"closeSet('" + setId + "')\" onmousedown=\"blockDrag()\" /></div>" + 
                    setName + "</div><div id=\"" + setId + "_content\" class=\"PopUpContent\"></div>"+
                    "<div class=\"PopUpToolBar\">" + 
                    "<div onmousedown=\"pressButton(this);\" onmouseout=\"releaseButton();\" onmouseup=\"doAction(this, 'cut');\">Cut</div>" +
                    "<div onmousedown=\"pressButton(this);\" onmouseout=\"releaseButton();\" onmouseup=\"doAction(this, 'copy');\">Copy</div>" +
                    "<div class=\"" + ((picList == null)? '':'ValidButton' )+ 
                    "\" onmousedown=\"pressButton(this);\" onmouseout=\"releaseButton();\" onmouseup=\"doAction(this, 'paste');\">Paste</div>" +
                    "<div onmousedown=\"pressButton(this);\" onmouseout=\"releaseButton();\" onmouseup=\"doAction(this, 'delete');\">Delete</div>" +
                    "<div onmousedown=\"pressButton(this);\" onmouseout=\"releaseButton();\" onmouseup=\"doAction(this, 'edit');\">Edit</div></div>";   
                
        workSpaceControl.appendChild(newWindow);
        
        newWindow.style.left = generateRandomNumber(boundingBoxtopLeft.x, 
            boundingBoxbottomRight.x - newWindow.offsetWidth) + "px";
        newWindow.style.top = generateRandomNumber(boundingBoxtopLeft.y, 
            boundingBoxbottomRight.y - newWindow.offsetHeight) + "px";
            
        newWindow.windowName = setName;
        registerWindow(newWindow);
        newWindow.windowName = null;

        ShowLoading(document.getElementById((setId + "_content")), false);

        Ajax_WebService.GetPicturesInSet(setId, 
            function(response){responseArrivedHandler(response, (setId + "_content"));}, //complete
            function(response){closeSet(newWindow.getAttribute("id"));},  //timeout
            function(response){closeSet(newWindow.getAttribute("id"));}); //error
         
        newWindow.style.visibility = 'visible';                   
        fadeIn(setId, 0, 50);
    }
    else
    {
        MessageBox("Info...", "You can't open more then " + maxOpenedWindows + " at a time!", 
            null, new Array('Ok'), new Array(null) );
    }
};
/* open set */

/* window buttons */
var pressedButton = null;
var pressed = false;
var timeOutId = null;
function pressButton(obj)
{
    if(obj.className == '')
        return;
        
    if(pressedButton != null)
    {
        pressedButton.className = 'ValidButton';
        pressedButton = null;
        clearTimeout(timeOutId);
    }
    
    obj.className = 'PressedButton';
    
    pressedButton = obj;
    pressed = true;
};

function releaseButton()
{
    if(pressed)
    {
        pressed = false;
        timeOutId = setTimeout("if(pressedButton != null) {pressedButton.className = 'ValidButton'; pressedButton = null;}", 200);
    }
}

function selectionChangedHandler(window)
{
    var toolbar = window.nextSibling;
    
    var nbSelItms = getSelectedItemList(window).length;
    
    switch(nbSelItms)
    {
        case 0:
            toolbar.childNodes[0].className = '';
            toolbar.childNodes[1].className = '';
            toolbar.childNodes[3].className = '';
            toolbar.childNodes[4].className = '';
        break;
        case 1:
            toolbar.childNodes[0].className = 'ValidButton';
            toolbar.childNodes[1].className = 'ValidButton';
            toolbar.childNodes[3].className = 'ValidButton';
            toolbar.childNodes[4].className = 'ValidButton';
        break;
        default:
            toolbar.childNodes[4].className = '';
    };
};

function validateInvalidatePasteButtons(validate)
{
    var toolbars = getElementsByClassName(workSpaceControl, 'div', 'PopUpToolBar');
    for(var i = 0; i < toolbars.length; i++)
        if(validate == true)
            toolbars[i].childNodes[2].className = 'ValidButton';
        else
        {
            if(pressedButton != null)
            {
                pressedButton = null;
                clearTimeout(timeOutId);
            }
            toolbars[i].childNodes[2].className = '';
        }
};

var sourceSetContent = null;
var picList = null;
var cut = false;

function doAction(obj, action)
{   
    if(obj.className == '')
        return;
        
    releaseButton();
    
    var windowContent = obj.parentNode.previousSibling;
    
    switch(action)
    {      
        case 'cut':
        case 'copy':
            if(cut)
            {
                for(var i = 0; i < picList.length; i++)
                    setOpacity(picList[i], 100);
                    
                cut = false;
            } 
            
            if(action == 'cut')
                cut = true;
            
            sourceSetContent = windowContent;
            var list = getSelectedItemList(windowContent);
            
            picList = new Array();
            for(var i = 0; i < list.length; i++)
            {
                picList.push(list[i]);
                if(cut) setOpacity(list[i], 60);
            }
            
            validateInvalidatePasteButtons(true);             
        break;
        case 'paste':   //todo - aici e jmekeria
        {
            if(sourceSetContent == null)
                return;
                
            var id = windowContent.getAttribute('id') + "_";
            var nbOrd = windowContent.childNodes.length;
            
            if(cut)     ///cut
            {
            
                var args = new Array();
                for(var i = 0; i < picList.length; i++)    
                    args.push(picList[i].picId);
                    
                ShowLoading(pageControl, true);
                
                Ajax_WebService.MovePictureInSet(args, 
                    sourceSetContent.parentNode.getAttribute('id'),
                    windowContent.parentNode.getAttribute('id'),
                    function(response)
                    {
                        HideLoading(pageControl);
                        if(response.length > 0)
                            MessageBox('Error...', 'Some items could not be moved. Please note that you can\'t have the same pic more than once in the same set.',
                                null, new Array('Yes'), new Array(null));
                                
                        for(var i = 0; i < picList.length; i++)
                            if(!response.exists(picList[i].picId))
                            {
                                sourceSetContent.removeChild(picList[i]);
                                pic = addPicToWindowContent(windowContent, picList[i].picId, picList[i].picName);
                                setTimeout("fadeThumb('" + pic.firstChild.getAttribute('id') + "')", (500 + 100 * i));
                            }
                            else
                                setOpacity(picList[i], 100);
                            
                        deselectAllItems(sourceSetContent);
                        selectionChangedHandler(sourceSetContent);
                        
                        sourceSetContent = null;
                        picList = null;
                        cut = false;
                        validateInvalidatePasteButtons(false); 
                    }, ServerCommunicationError, ServerCommunicationError);
            }
            else        ///copy
            {
                var args = new Array();
                for(var i = 0; i < picList.length; i++)    
                    args.push(picList[i].picId);
                
                ShowLoading(pageControl, true);
                Ajax_WebService.CopyPictureInSet(args, windowContent.parentNode.getAttribute('id'),
                    function(response)
                    {
                        HideLoading(pageControl);
                        if(response.length > 0)
                            MessageBox('Error...', 'Some items could not be copyed. Please note that you can\'t have the same pic more than once in the same set.',
                                null, new Array('Yes'), new Array(null));
                                
                        for(var i = 0; i < picList.length; i++)
                            if(!response.exists(picList[i].picId))
                            {
                                pic = addPicToWindowContent(windowContent, picList[i].picId, picList[i].picName);
                                setTimeout("fadeThumb('" + pic.firstChild.getAttribute('id') + "')", (500 + 100 * i));
                            }
                    }, ServerCommunicationError, ServerCommunicationError);
            }  
        }  
        break;
        case 'delete':
            {
                var pictures = getSelectedItemList(windowContent);
                
                MessageBox('Confirm delete...', 'Are you sure you want to delete ' + 
                    ((pictures.length == 1)? ('<b>' + pictures[0].picName + '</b>'):('these ' + pictures.length + ' items')) +
                    '?<br />This will only remove the pic from this set. To permanently delete the pic remove it from all sets and then delete it from the picture stream.', 
                    null, new Array('Yes', 'No'), 
                    new Array(
                    new Array(
                    function()
                    {
                        ShowLoading(pageControl, true);
                        
                        var args = new Array();
                        for(var i = 0; i < pictures.length; i++)
                            args.push(pictures[i].picId);
                        
                        Ajax_WebService.DeletePicsFromSet(args, 
                            windowContent.parentNode.getAttribute('id'),                           
                            function(response)
                            {
                                HideLoading(pageControl);
                                
                                for(var i = 0; i < pictures.length; i++)
                                {
                                    windowContent.removeChild(pictures[i]);
                                    if(response.exists(pictures[i].picId))
                                    {
                                        addPicToPictureStreamControl(pictures[i].picId, pictures[i].picName);
                                    }
                                }
                                        
                                deselectAllItems(windowContent);
                                selectionChangedHandler(windowContent);
                            },
                            ServerCommunicationError, ServerCommunicationError);
                    }
                    ), null));
            }
        break;
        case 'edit':
            { 
                var pictures = getSelectedItemList(windowContent);
                setPicDetails(pictures[0]);     
            }
        break;
    }
};
/* window buttons */

/* close set */
function closeSet(setId)
{
    if(openedWindows.exists(document.getElementById(setId)))
    {
        fadeOut(setId, 100, 50, removeSetWindow); //todo: fix bug: daca se apasa de 2 ori pe close
        unregisterWindow(document.getElementById(setId));
    }
};
/* close set */

/* remove set from work area */
function removeSetWindow(setId)
{
    var control = document.getElementById(setId);
    if(control != null)
    {
        workSpaceControl.removeChild(control);
        setClosedItemLook(document.getElementById(("set_" + setId)));
    } 
};
/* remove set from work area */

/* request picture url from server */
function responseArrivedHandler(APictureList, contentId)
{
    var content = document.getElementById(contentId);
    
    if(content)
    {      
        HideLoading(content);
        
        registerContentForItemSelect(content, "", "selectedImage", null, selectionChangedHandler);

        for(var i = 0; i < APictureList.length; i++)
        {
            var pic = addPicToWindowContent(content, APictureList[i].Id, APictureList[i].Name); 
            setTimeout("fadeThumb('" + pic.firstChild.getAttribute('id') + "')", (500 + 100 * i));
        }
    }
};
/* fade thumb */
function fadeThumb(picId)
{
    var pic = document.getElementById(picId);
    if(pic)
    {
        fadeIn(picId, 0, 150);       
        pic.style.visibility = "visible";
    }
};
/* fade thumb */

/* set details */
function setPicDetails(pic)
{
    var smallImageCache = new Image();
    smallImageCache.src = "Pictures/Small/" + pic.picId + ".jpeg";
    
    ShowLoading(pageControl, true);
    
    Ajax_WebService.GetPictureDetails(pic.picId,
        function(APicture)
        {   
            HideLoading(pageControl);
            
            if(APicture == null)
                InternalServerError();
            else
            {
                var desc = ((APicture.Description == null) ? "Enter a description..." : APicture.Description);
                var tags = ((APicture.Tags == null) ? "Enter tags..." : APicture.Tags);
                
                MessageBox("Edit pic...", "Please choose a name, a description and tags for this pic.",
                    "<p style=\"text-align: center;\"><img src=\"" + smallImageCache.src + "\" /></p>" +
                    "<p>Name: <input name=\"pic_name\" type=\"text\" id=\"pic_name_input_id\" value=\"" + pic.picName + "\" /></p>" +
                    "<p>Description: <textarea name=\"pic_descr\" rows=\"2\" cols=\"20\" type=\"text\" id=\"pic_descr_input_id\">" + desc + "</textarea></p>" +
                    "<p>Tags: <textarea name=\"pic_tags\" rows=\"2\" cols=\"20\" type=\"text\" id=\"pic_tags_input_id\">" + tags + "</textarea></p>",
                    new Array('Save...', 'Cancel'), 
                    new Array(new Array(
                    function(args)
                    {
                        ShowLoading(pageControl, true);
                        Ajax_WebService.SetPictureDetails(pic.picId, args[0], args[1], args[2],
                            function(response)
                            {
                                HideLoading(pageControl);
                                
                                if(response == false)
                                    InternalServerError();
                                else
                                {
                                    pic.picName = args[0];
                                    if(pic.lastChild && pic.lastChild.nodeName.toLocaleLowerCase() == 'p')
                                        pic.lastChild.innerHTML = args[0];
                                }
                            }, ServerCommunicationError, ServerCommunicationError);
                    }, 'document.getElementById(\'pic_name_input_id\').value',
                    'document.getElementById(\'pic_descr_input_id\').value', 'document.getElementById(\'pic_tags_input_id\').value'), null));
            }
        }, ServerCommunicationError, ServerCommunicationError);    
};
/* set details */

/* request picture url from server */

/* Set opening/closing */

/* set selecting */
var multiSelect = false;

function isCtrl(ev)
{
    var key = (ev.charCode)?ev.charCode:
			((ev.keyCode)?ev.keyCode:((ev.which)?ev.which:0));
			
    return key == 16;  
}

function keyDown(ev)
{
    ev  = ev || window.event;
    
    if(isCtrl(ev))
       multiSelect = true;
};
function keyUp(ev)
{
    ev  = ev || window.event;
    
     if(isCtrl(ev))
        multiSelect = false;
};

function registerContentForItemSelect(contentHandle, itemUnselectedClass, itemSelectedClass, itemOpenedClass, selChangedHandler)
{
    contentHandle.selectedItems = new Array();
    contentHandle.unselClass = itemUnselectedClass;
    contentHandle.selClass = itemSelectedClass;
    contentHandle.openClass = itemOpenedClass;
    
    contentHandle.canSelect = true;
    
    if(selChangedHandler)
        contentHandle.selChangedHandler = selChangedHandler;
    else
        contentHandle.selChangedHandler = null;
    
    //contentHandle.onmousedown = function(){ deselectAllItems(this); }
};

function getSelectedItemList(container)
{
    if(container.canSelect)
        return container.selectedItems;
    
    else 
        return null;
};

var intern = false;

function deselectAllItems(container)
{   
    for(var i = 0; i < container.selectedItems.length; i++)
        if(container.selectedItems[i].className != container.openClass)
            container.selectedItems[i].className = container.unselClass;

    container.selectedItems.removeAll();
    
    if(!intern && container.selChangedHandler != null)
        container.selChangedHandler(container);
}

function selectItem(ev, object)
{
    ev = ev || window.event;
    cancelEventBubbleing(ev);

    var container = object.parentNode;
    
    if(!container.canSelect)
        return;
    
    if(!multiSelect)
    {  
        intern = true;
        deselectAllItems(container);
        intern = false;
        
        if(object.className != container.openClass)
        {
            object.className = container.selClass;
            container.selectedItems.push(object);
        }
    }
    else
    {
        if(object.className != container.openClass)
        {
            if(container.selectedItems.exists(object))
            {
                object.className = container.unselClass;
                container.selectedItems.remove(object);
            }
            else
            {
                object.className = container.selClass;
                container.selectedItems.push(object);              
            }
        }
    }
    
    if(container.selChangedHandler != null)
        container.selChangedHandler(container);
};

function setOpenItemLook(object)
{
    var container = object.parentNode;
    
    if(!container.canSelect)
        return;
        
    object.className = container.openClass;
    container.selectedItems.remove(object);
};
function setClosedItemLook(object)
{
    var container = object.parentNode;
    
    if(!container.canSelect)
        return;
        
    object.className = container.unselClass;
};
/* set selecting */

/* drag and drop */

/* drag variables */

/* drag flag */
var canDrag = true;
/* drag flag */

/* current dragged object */
var currentDraggedObject = null;
/* current dragged object */

/* mouse offset from object top, left */
var mouseOffset = null;
/* mouse offset from object top, left */

/* mouse move handler */
var mouseMoveHandler = null;
/* mouse move handler */

/* drag variables */

/* drag functions */

/* mouse offsets */
function getMouseOffset(target, ev)
{
	ev = ev || window.event;

	var docPos    = getTopLeft(target);
	var mousePos  = mouseCoords(ev);
	return {x:mousePos.x - docPos.x, y:mousePos.y - docPos.y};
}
/* mouse offsets */

/* begin dragging */
function beginDrag(setId, ev, handler)
{
    if(canDrag == false)
        return;
               
    currentDraggedObject = document.getElementById(setId);   
    mouseOffset = getMouseOffset(document.getElementById(setId), ev);
    
    document.body.onmousemove = doDrag;
    
    if(handler)
        mouseMoveHandler = handler;
};
/* begin dragging */

/* do dragging */
function doDrag(ev)
{
    if(currentDraggedObject != null)
    {  
        ev  = ev || window.event;
        
        var mousePos = mouseCoords(ev);
        
        //currentDraggedObject.style.position = 'absolute';
        if(mousePos.x - mouseOffset.x > boundingBoxtopLeft.x && 
            mousePos.x - mouseOffset.x + currentDraggedObject.offsetWidth < boundingBoxbottomRight.x)
            currentDraggedObject.style.left = (mousePos.x - mouseOffset.x) + "px";
            
        if(mousePos.y - mouseOffset.y > boundingBoxtopLeft.y &&
            mousePos.y - mouseOffset.y + currentDraggedObject.offsetHeight < boundingBoxbottomRight.y)
            currentDraggedObject.style.top = (mousePos.y - mouseOffset.y) + "px";
        
        if(mouseMoveHandler)
            mouseMoveHandler(mousePos);
            
        return false;
    }
};
/* do dragging */

/* stop dragging */
function stopDrag()
{
    currentDraggedObject = null;
    document.body.onmousemove = null;
    
    canDrag = true;
    mouseMoveHandler = null;
};
/* stop dragging */

/* block dragging */
function blockDrag()
{
    currentDraggedObject = null;
    canDrag = false;
};
/* block dragging */

/* drag functions */

/* drag and drop */
