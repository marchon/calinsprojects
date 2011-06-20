<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" 
    CodeFile="Uploader.aspx.cs" Title="PicMe - Uploader" Inherits="Manager" %>

<asp:Content ID="HeadContent" ContentPlaceHolderID="HeadContentPlaceHolder" Runat="Server">
    <link href="LookAndFeel/Uploader.css" rel="stylesheet" type="text/css" />
    <script src="ClientScript/Uploader_MultipleFiles.js" type="text/javascript"></script>
</asp:Content>
<asp:Content ID="BodyContent" ContentPlaceHolderID="BodyContentPlaceHolder" Runat="Server">
    <div id="Uploader">
        <div id="ErrMsgDiv" runat="server">
        </div>
        <h3>Upload images</h3>
        <div class="little_box">
            <small>
                Choose some images and then hit the upload button.
                <br />
                You can upload the following formats: <b>jpeg</b>, <b>bmp</b>, <b>gif</b>, <b>png</b>.
            </small>
            <div id="uploadContainer">
            </div>
            <div id="toUploadFilesListContainer">
            </div>
            <br />
            <asp:Button ID="UploadButton" runat="server" onclick="UploadButton_Click" Text="Upload..." 
                OnClientClick="return submitCeck()"/>
        </div>
    </div>
</asp:Content>

