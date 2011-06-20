<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="PicViewer.aspx.cs" Inherits="PicViewer" Title="PicMe - PicViewer" %>

<asp:Content ID="Content1" ContentPlaceHolderID="HeadContentPlaceHolder" Runat="Server">
    <link href="LookAndFeel/PicViewer.css" rel="stylesheet" type="text/css" />
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="BodyContentPlaceHolder" Runat="Server">
    <div id="PicView" class="PicView" runat="server">
        <div>
            <asp:Image ID="TheImage" 
                ImageUrl='<%# "~/Pictures/Big/" + Request["pic"] + ".jpeg" %>' runat="server" />
        </div>
        <asp:LoginView ID="CommentLoginView" runat="server">
            <AnonymousTemplate>
                Do you want to comment? <a href="Login.aspx?redirect=PicViewer.aspx?pic=<%# Request["pic"] %>">Log in!</a>
            </AnonymousTemplate>
            <LoggedInTemplate>
                <asp:TextBox ID="CommentTextBox" runat="server" 
                    TextMode="MultiLine" Width="400px" Height="100px">
                </asp:TextBox>
                <br />
                <asp:Button ID="Button1" runat="server" Text="Submit..." 
                    onclick="Button1_Click" />
            </LoggedInTemplate>
        </asp:LoginView>
        <asp:GridView ID="CommentsGridView" runat="server" AutoGenerateColumns="False" 
            BorderStyle="None" BorderWidth="0px">
            <Columns>
                <asp:TemplateField>
                    <ItemTemplate>
                        <div style="margin: 10px; padding: 5px; border: solid 1px #16191D; width: 400px;">
                            <div style="float:left; display: inline; margin-right: 15px;">
                                <asp:Image ID="Avatar" runat="server" 
                                                ImageUrl='<%# Util.GetAvatar(Eval("user_avatar")) %>' />
                            </div>
                            <div style="float:left; display: inline; margin-right: 15px;">
                                <p style="color: White;"><b><%# Eval("user_name")%></b> says:</p>
                                <p style="margin: 5px; border-left: solid 1px #16191D; padding-left: 5px;"><%# Eval("user_comment")%></p>
                                <p style="color: White;">
                                    <small>
                                        Posted on 
                                            <b>
                                                <%# Util.GetDate(Eval("user_comment_date"))%>
                                            </b>. 
                                    </small>
                                </p>
                            </div>
                        </div>
                    </ItemTemplate>
                </asp:TemplateField>
            </Columns>
        </asp:GridView>
    </div>
</asp:Content>

