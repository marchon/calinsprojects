<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="Browse.aspx.cs" Inherits="Browse" Title="PicMe - Browse" %>

<asp:Content ID="HeadContent" ContentPlaceHolderID="HeadContentPlaceHolder" Runat="Server">
    <link href="LookAndFeel/Browse.css" rel="stylesheet" type="text/css" />
</asp:Content>
<asp:Content ID="BodyContent" ContentPlaceHolderID="BodyContentPlaceHolder" Runat="Server">
    <div id="Search">
        <h3>Search:</h3>
        <div>
            <asp:TextBox ID="SearchTextBox" runat="server" CssClass="textbox"></asp:TextBox>
            <asp:RequiredFieldValidator
                ID="RequiredFieldValidator1" runat="server" ErrorMessage="*" ControlToValidate="SearchTextBox"></asp:RequiredFieldValidator>
            <asp:Button ID="SearchButton" runat="server" Text="Search..." 
                onclick="SearchButton_Click" CssClass="button" Width="81px"/>
            <asp:RadioButtonList ID="SearchByRadioButtonList" runat="server" 
                RepeatDirection="Horizontal" CssClass="radiobuttonlist">
                <asp:ListItem Selected="True" Value="0">By Name</asp:ListItem>
                <asp:ListItem Value="1">By Tags</asp:ListItem>
            </asp:RadioButtonList>
        </div>
    </div>
    <div id="GridView">
        <asp:GridView ID="SearchResultGridView" runat="server"
            AllowPaging="True" AutoGenerateColumns="False" PageSize="15" 
            BorderWidth="0px" OnPageIndexChanging="SearchResultGridView_SelectedIndexChanging" BorderStyle="None">
            <PagerSettings Position="Bottom" />
            <Columns>
                <asp:TemplateField>
                    <ItemTemplate>
                        <div class="Record">
                            <div class="Info">
                                <h3><%# Eval("pic_name")%></h3>
                                <p class="UploadDate">Uploaded on <b><%# Util.GetDate(Eval("pic_upload_date")) %></b></p>
                                <div class="User">
                                    <div class="Avatar">
                                        <asp:Image ID="Avatar" runat="server" 
                                            ImageUrl='<%# Util.GetAvatar(Eval("user_avatar")) %>' />
                                    </div>
                                    <p>
                                        Uploaded by <a href="#"><%# Eval("user_name")%></a>.
                                        <br /> 
                                        See more <a href="#">pics</a> or visit <a href="#">profile</a>.
                                    </p>
                                </div>    
                                <div class="Tags">
                                    Tags: <%# Util.GetBrowseTagLinks(Eval("pic_tags"))%>
                                </div>
                            </div>
                            <div class="Image">
                                <a href="PicViewer.aspx?pic=<%# Eval("pic_id") %>">
                                    <asp:Image ID="Pic" runat="server" ImageUrl='<%# "~/Pictures/Small/" + Eval("pic_id") + ".jpeg" %>' />
                                </a>
                            </div>
                        </div>
                    </ItemTemplate>
                </asp:TemplateField>
            </Columns>
        </asp:GridView>
    </div>
</asp:Content>

