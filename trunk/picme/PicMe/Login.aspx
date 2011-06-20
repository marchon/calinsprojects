<%@ Page Language="C#" AutoEventWireup="true"  CodeFile="Login.aspx.cs" Inherits="_Default" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>
        Share A Pic - Login
    </title>
    <link href="LookAndFeel/Login.css" rel="stylesheet" type="text/css" />
</head>
<body>
    <form id="LoginForm" runat="server">
        <div id="MessageDiv" runat="server">   
        </div>
        <asp:Login ID="MyLogin" runat="server" 
                                OnLoginError="MyLogin_LoginError" 
            FailureText="Please try again..." CssClass="login">
            <LayoutTemplate> 
                <table border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td colspan="2" class="caption">
                            Log in...
                        </td>
                    </tr>
                    <tr>
                        <td align="center" colspan="2" class="failure_text">
                            <asp:Literal ID="FailureText" runat="server" EnableViewState="False"></asp:Literal> 
                    </td>
                    </tr>
                    <tr>
                        <td align="right">
                            <asp:Label ID="UserNameLabel" runat="server" AssociatedControlID="UserName">
                                User Name:
                            </asp:Label>
                        </td>
                        <td>
                            <div class="text_box">
                                <asp:TextBox ID="UserName" runat="server"></asp:TextBox>
                            </div>
                            <asp:RequiredFieldValidator ID="UserNameRequired" runat="server" 
                                ControlToValidate="UserName" ErrorMessage="User Name is required." 
                                ToolTip="User Name is required." ValidationGroup="MyLogin" 
                                ForeColor="#BA3300">*</asp:RequiredFieldValidator>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            <asp:Label ID="PasswordLabel" runat="server" AssociatedControlID="Password">Password:</asp:Label>
                        </td>
                        <td>
                            <div class="text_box">
                                <asp:TextBox ID="Password" runat="server" TextMode="Password"></asp:TextBox>
                            </div>
                            <asp:RequiredFieldValidator ID="PasswordRequired" runat="server" 
                                ControlToValidate="Password" ErrorMessage="Password is required." 
                                ToolTip="Password is required." ValidationGroup="MyLogin" 
                                ForeColor="#BA3300">*</asp:RequiredFieldValidator>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="left">
                            <div class="dashed">
                                <asp:CheckBox ID="RememberMe" runat="server" Text="Remember me next time." />
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" colspan="2">
                            <asp:Button ID="LoginButton" runat="server" CommandName="Login" Text="Log In" 
                                ValidationGroup="MyLogin" CssClass="submit_button"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" colspan="2">
                        <div class="pass_rec">
                            <hr />
                                <a href="#" title="Password recovery.">Help! I forgot my password.</a>
                            <hr />
                        </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="left_corner">                        
                            </div>   
                        </td>
                        <td align="left"> 
                            <div class="right_corner">
                                <p>
                                    Not yet a member?
                                </p>
                                Signing up is easy!
                                <a href="CreateAccount.aspx" title="Create account.">
                                    Sign Up
                                </a>
                            </div>
                        </td>
                    </tr>
                </table>
            </LayoutTemplate>
        </asp:Login>
    </form>
</body>
</html>
