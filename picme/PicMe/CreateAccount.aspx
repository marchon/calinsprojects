<%@ Page Language="C#" AutoEventWireup="true" CodeFile="CreateAccount.aspx.cs" Inherits="CreateAccount" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>
        Create Account
    </title>
    <link href="LookAndFeel/CreateAccount.css" rel="stylesheet" type="text/css" />
</head>
<body>
    <form id="AccountForm" runat="server">
    <div>
        <asp:CreateUserWizard ID="SignUp" runat="server" CssClass="signup" 
            ContinueDestinationPageUrl="~/Default.aspx">
            <CreateUserButtonStyle CssClass="submit_button" />
            <WizardSteps>
                <asp:CreateUserWizardStep ID="CreateUserWizardStep1" runat="server">
                    <ContentTemplate>
                        <table border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td colspan="2" class="caption">
                                    Sign Up...</td>
                            </tr>                            
                            <tr>
                                <td align="right">
                                    <asp:Label ID="UserNameLabel" runat="server" AssociatedControlID="UserName">User 
                                    Name:</asp:Label>
                                </td>
                                <td>
                                    <div class="text_box">
                                        <asp:TextBox ID="UserName" runat="server"></asp:TextBox>
                                    </div>
                                    <asp:RequiredFieldValidator ID="UserNameRequired" runat="server" 
                                        ControlToValidate="UserName" ErrorMessage="User Name is required." 
                                        ToolTip="User Name is required." ValidationGroup="SignUp" 
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
                                        ToolTip="Password is required." ValidationGroup="SignUp" 
                                        ForeColor="#BA3300">*</asp:RequiredFieldValidator>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">
                                    <asp:Label ID="ConfirmPasswordLabel" runat="server" 
                                        AssociatedControlID="ConfirmPassword">Confirm Password:</asp:Label>
                                </td>
                                <td>
                                    <div class="text_box">
                                        <asp:TextBox ID="ConfirmPassword" runat="server" TextMode="Password"></asp:TextBox>
                                    </div>
                                    <asp:RequiredFieldValidator ID="ConfirmPasswordRequired" runat="server" 
                                        ControlToValidate="ConfirmPassword" 
                                        ErrorMessage="Confirm Password is required." 
                                        ToolTip="Confirm Password is required." 
                                        ValidationGroup="SignUp" ForeColor="#BA3300">*</asp:RequiredFieldValidator>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">
                                    <asp:Label ID="EmailLabel" runat="server" AssociatedControlID="Email">E-mail:</asp:Label>
                                </td>
                                <td>
                                    <div class="text_box">
                                        <asp:TextBox ID="Email" runat="server"></asp:TextBox>
                                    </div>
                                    <asp:RegularExpressionValidator ID="EmailRequired" runat="server" 
                                        ControlToValidate="Email" ErrorMessage="E-mail is required and must be valid." 
                                        ToolTip="E-mail is required and must be valid." ValidationGroup="SignUp" 
                                        ForeColor="#BA3300" 
                                        ValidationExpression=".*@.*\..*">*</asp:RegularExpressionValidator>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">
                                    <asp:Label ID="QuestionLabel" runat="server" AssociatedControlID="Question">Security 
                                    Question:</asp:Label>
                                </td>
                                <td>
                                    <div class="text_box">
                                        <asp:TextBox ID="Question" runat="server"></asp:TextBox>
                                    </div>
                                    <asp:RequiredFieldValidator ID="QuestionRequired" runat="server" 
                                        ControlToValidate="Question" ErrorMessage="Security question is required." 
                                        ToolTip="Security question is required." 
                                        ValidationGroup="SignUp" ForeColor="#BA3300">*</asp:RequiredFieldValidator>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">
                                    <asp:Label ID="AnswerLabel" runat="server" AssociatedControlID="Answer">Security 
                                    Answer:</asp:Label>
                                </td>
                                <td>
                                    <div class="text_box">
                                        <asp:TextBox ID="Answer" runat="server"></asp:TextBox>
                                    </div>
                                    <asp:RequiredFieldValidator ID="AnswerRequired" runat="server" 
                                        ControlToValidate="Answer" ErrorMessage="Security answer is required." 
                                        ToolTip="Security answer is required." ValidationGroup="SignUp" 
                                        ForeColor="#BA3300">*</asp:RequiredFieldValidator>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" colspan="2">
                                    <asp:CompareValidator ID="PasswordCompare" runat="server" 
                                        ControlToCompare="Password" ControlToValidate="ConfirmPassword" 
                                        Display="Dynamic" 
                                        ErrorMessage="Password fields must match." 
                                        ValidationGroup="SignUp" ForeColor="#BA3300">*</asp:CompareValidator>
                                    <asp:ValidationSummary ID="ErrorSummary" runat="server" CssClass="error_list" 
                                        ForeColor="#BA3300" HeaderText="Please correct these errors before submitting:" 
                                        ValidationGroup="SignUp" />
                                </td>
                            </tr>
                            <tr>
                                <td align="center" colspan="2" class="failure_text">
                                    <asp:Literal ID="ErrorMessage" runat="server" EnableViewState="False"></asp:Literal>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" colspan="2" style="padding: 3px 20px;">
                                    <hr />
                                    <p style="font-weight: bold">
                                        Already a member?
                                    </p>
                                    <a href="Login.aspx"title="Log In.">
                                        Log In</a>
                                    <hr />
                                </td>
                            </tr>
                        </table>
                    </ContentTemplate>
                </asp:CreateUserWizardStep>
                <asp:CompleteWizardStep ID="CompleteWizardStep1" runat="server">
                    <ContentTemplate>
                        <table border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td align="center" class="caption">
                                    Complete...</td>
                            </tr>
                            <tr>
                                <td>
                                    Your account has been successfully created.</td>
                            </tr>
                            <tr>
                                <td align="right">
                                    <asp:Button ID="ContinueButton" runat="server" CausesValidation="False" 
                                        CommandName="Continue" Text="Continue" ValidationGroup="CreateUserWizard1" CssClass="submit_button" />
                                </td>
                            </tr>
                        </table>
                    </ContentTemplate>
                </asp:CompleteWizardStep>
            </WizardSteps>
        </asp:CreateUserWizard>
    </div>
    </form>
</body>
</html>
