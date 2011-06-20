using System;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Xml.Linq;

public partial class _Default : System.Web.UI.Page 
{
    protected void Page_Load(object sender, EventArgs e)
    {
        if (Request.QueryString["redirect"] != null)
        {
            MessageDiv.InnerHtml = "You have to register before accesing this link! Please take a moment and do so.";
            MyLogin.DestinationPageUrl = Request.QueryString["redirect"];
        }
    }

    protected void MyLogin_LoginError(object sender, EventArgs e) 
    {
        MyLogin.FailureText = "<hr /><b>";
        try
        {
            MembershipUser userInfo = Membership.GetUser(MyLogin.UserName);

            if (userInfo == null)
                MyLogin.FailureText += "There is no such user in our database! ";          
            else
                if (!userInfo.IsApproved)
                    MyLogin.FailureText += "Your account has not yet been approven!";
                else
                    if (userInfo.IsLockedOut)
                        MyLogin.FailureText += "Your account has been blocked due to the reach of maximum number of incorect login attempts. Please contact the site adminitrator to solve this issue.";
                    else
                        MyLogin.FailureText += "The password was incorect!";
        }
        catch (Exception)
        {
            MyLogin.FailureText = "Your login atempt was not succesful!";
        }

        MyLogin.FailureText += "</b><br />Please try again...<hr />";
    }
}
