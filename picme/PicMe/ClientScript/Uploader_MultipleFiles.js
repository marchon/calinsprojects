var uploadContainer = null;
var toUploadFilesListContainer = null;

var message = null;
var extensionArray = null;

var currentVisibleInputField = null;

var idCounter = 0;

function doUploadInit(uploadContainerId, toUploadFilesListContainerId,
    extesionmessage, validExtensionArray, idOfSubmitButton)
{
    uploadContainer = document.getElementById(uploadContainerId);
    toUploadFilesListContainer = document.getElementById(toUploadFilesListContainerId);
    
    message = document.createElement('small');
    message.innerHTML = extesionmessage;
    message.style.display = 'none';
    message.style.color = 'Red';
    uploadContainer.appendChild(message);
    
    extensionArray = validExtensionArray;
    for(var i = 0; i < extensionArray.length; i++)
        extensionArray[i] = extensionArray[i].toLowerCase();
        
    var newId = "input_" + idCounter;
    idCounter ++;    
    
    currentVisibleInputField = document.createElement('input');
    currentVisibleInputField.setAttribute('type', 'file');
    currentVisibleInputField.className = 'Browse';
    currentVisibleInputField.setAttribute('id', newId);
    currentVisibleInputField.setAttribute('name', newId + "_name");
    currentVisibleInputField.onchange = addNewUploadControl;
    currentVisibleInputField.style.display = 'inline';
    
    uploadContainer.insertBefore(currentVisibleInputField, message);
};

function addNewUploadControl()
{
    if(hasAcceptedExtension(this.value))
    {    
        this.style.display = 'none';
        
        var picName = this.value;

        var sind = picName.lastIndexOf('\\') + 1;
        var len = picName.lastIndexOf('.') - sind;
        picName = picName.substr(sind, len);
       
            
        var entry = document.createElement('p');
        entry.fileUpload = this;
        
        var name = document.createElement('span');
        name.innerHTML = picName + '   ';
        entry.appendChild(name);
        
        var deleteCommand = document.createElement('a');
        deleteCommand.innerHTML = '[Remove]';
        deleteCommand.className = 'Remove';
        deleteCommand.onclick = function()
                                {
                                    var entry = this.parentNode;
                                    uploadContainer.removeChild(entry.fileUpload);
                                    toUploadFilesListContainer.removeChild(entry);
                                };
                                
        entry.appendChild(deleteCommand);
        
        toUploadFilesListContainer.appendChild(entry);

        var newId = "input_" + idCounter;
        idCounter ++;    
        
        currentVisibleInputField = document.createElement('input');
        currentVisibleInputField.setAttribute('type', 'file');
        currentVisibleInputField.className = 'Browse';
        currentVisibleInputField.setAttribute('id', newId);
        currentVisibleInputField.setAttribute('name', newId + "_name");
        currentVisibleInputField.onchange = addNewUploadControl;
        currentVisibleInputField.style.display = 'inline';
        
        uploadContainer.insertBefore(currentVisibleInputField, message);
        
        message.style.display = 'none';
    }
    else if(this.value == "")
    {
    }
    else
    {
        message.style.display = 'inline';
    }
};

function hasAcceptedExtension(stringToCeck)
{
    if(extensionArray)
        for(var i = 0; i < extensionArray.length; i++)
            if(stringToCeck.toLocaleLowerCase().lastIndexOf("." + extensionArray[i]) != -1)
                return true;
   
    return false;
};

var submit = false;
function submitCeck()
{
    if(!submit)
    {
        uploadContainer.removeChild(currentVisibleInputField);
        message.innerHTML = "Uploading files... Please wait!"
        message.style.display = 'inline';
        submit = true;
        
        return true;
    }
    
    return false;
};