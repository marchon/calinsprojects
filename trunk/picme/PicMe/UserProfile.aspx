<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" 
    CodeFile="UserProfile.aspx.cs" Inherits="UserProfile" Title="PicMe - Profile" %>

<asp:Content ID="HeadContent" ContentPlaceHolderID="HeadContentPlaceHolder" Runat="Server">
    <link href="LookAndFeel/Profile.css" rel="stylesheet" type="text/css" />
</asp:Content>
<asp:Content ID="BodyContent" ContentPlaceHolderID="BodyContentPlaceHolder" Runat="Server">
    <div class="box">
        <h3> Your Avatar: </h3>
        <div class="little_box">
        <asp:Label ID="Avatar_Label" runat="server" Text="Choose Avatar:    "></asp:Label>
        <asp:FileUpload ID="Avatar_FileUpload" runat="server" />
        <br />
        <br />
        <asp:Button
            ID="Upload_Button" runat="server" Text="Upload avatar..." 
        onclick="Upload_Button_Click" />
        </div>
    </div>
    <div class="box">
        <h3> About you: </h3>
        <div class="little_box">
        <asp:Label ID="Name_Label" runat="server" Text="Your real name:    "></asp:Label>
        <asp:TextBox ID="Name_TextBox" runat="server"></asp:TextBox>
        <br />
        <asp:Label ID="Gender_Label" runat="server" Text="Gender:    "></asp:Label>
        <asp:RadioButtonList ID="Gender_RadioButtonList" runat="server" AppendDataBoundItems="True" 
                            RepeatDirection="Horizontal" RepeatLayout="Flow">
            <asp:listitem id="Gender_Male" runat="server" value="Male" />
            <asp:listitem id="Gender_Female" runat="server" value="Female" />
        </asp:RadioButtonList>
        <br />
        <asp:Label ID="Birth_Lable" runat="server" Text="Date of birth:    "></asp:Label>   
        <asp:DropDownList ID="Day_DropDownList" runat="server">
        </asp:DropDownList>
        <asp:DropDownList ID="Month_DropDownList" runat="server">
        </asp:DropDownList>
        <asp:DropDownList ID="Year_DropDownList" runat="server">
        </asp:DropDownList>
        <br />
        <asp:Label ID="Country_Label" runat="server" Text="Country:    "></asp:Label>
        <asp:TextBox ID="Country_TextBox" runat="server" ></asp:TextBox>
        <br />
        <asp:Label ID="City_Label" runat="server" Text="City:    "></asp:Label>
        <asp:TextBox ID="City_TextBox" runat="server" ></asp:TextBox>
        <br />
        <asp:Label ID="Description_Label" runat="server" Text="About you:    ">
        </asp:Label>
        <asp:TextBox ID="Description_TextBox" runat="server" Height="100px" TextMode="MultiLine" 
            Width="280px"></asp:TextBox>
        <br />
        <br />
        <asp:Button ID="Save_Button" runat="server" Text="Save..." onclick="Save_Button_Click" />
        </div>
    </div>
</asp:Content>

