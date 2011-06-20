using System;
using System.Collections;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Web.UI.HtmlControls;
using System.Xml.Linq;

public partial class MasterPage : System.Web.UI.MasterPage
{
    protected void Page_Load(object sender, EventArgs e)
    {
        if (!Page.IsPostBack)
        {
            if (Page.User.Identity.IsAuthenticated)
            {
                string avatar = DataBaseManager.GetUserAvatar(((Guid)(Membership.GetUser().ProviderUserKey)).ToString());

                if (avatar != null)
                    ((HtmlImage)(login_view.FindControl("img_avatar"))).Src = "~/Avatars/" + avatar + ".jpeg";
            }
        }
    }
    protected void ExecuteQuerySearch(object sender, EventArgs e)
    {
        //here redirect to browse with the secific query string from the query text box;
        Server.Transfer("Browse.aspx?query=" + Query.Text);
    }
}
