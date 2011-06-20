using System;
using System.Collections;
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
using System.IO;
using System.Collections.Generic;

public partial class Manager : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        if (!User.Identity.IsAuthenticated)
            Server.Transfer("Login.aspx?redirect=Manager.aspx");
        else
        {
            ((HtmlGenericControl)Master.FindControl("manager")).Attributes.Add("class", "current_page_item");

            ((HtmlGenericControl)Master.FindControl("BodyControl")).Attributes.Add("onload",
                "doWorkSpaceInit('WorkSpace', 'TaskBar', 'PictureStream', 'ScrollUp', 'ScrollDown', 10)");
        }        
    }
}
