<%@ Page Language="C#" MasterPageFile="~/MasterPage.master"
     AutoEventWireup="true" CodeFile="Manager.aspx.cs" 
     Inherits="Manager" Title="PicMe - Manager" %>

<asp:Content ID="HeadContent" ContentPlaceHolderID="HeadContentPlaceHolder" Runat="Server">
    <link href="LookAndFeel/Manager.css" rel="stylesheet" type="text/css" />
</asp:Content>
<asp:Content ID="BodyContent" ContentPlaceHolderID="BodyContentPlaceHolder" Runat="Server">
    <asp:ScriptManager ID="ScriptManager1" runat="server">
        <Scripts>
            <asp:ScriptReference Path="~/ClientScript/Manager_WindowManagement.js" />
        </Scripts>
        <Services>
            <asp:ServiceReference Path="~/Ajax_WebService.asmx" />
        </Services>
    </asp:ScriptManager>
    
    <div class="PictureStreamContainer">
        <div id="ScrollUp" class="ScrollUp">
        </div>
        <div id="PictureStream" class="PictureStream">
        </div>
        <div id="ScrollDown" class="ScrollDown">
        </div>
    </div>
    <div id="TaskBar" class="TaskBar">
        <div class="ValidButton">
            New Set 
        </div>
        <div>
            Edit Set
        </div>
        <div>
            Delete Set 
        </div>
        <div>
            Delete Pic
        </div>
    </div>
    <div id="WorkSpace" class="WorkSpace">
    </div>
</asp:Content>

