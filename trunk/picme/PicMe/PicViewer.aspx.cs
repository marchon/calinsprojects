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

public partial class PicViewer : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        if (!IsPostBack)
        {
            ViewState.Add("PicId", Request["pic"]);
            BindCommentsGridView();
        }
    }
    protected void Button1_Click(object sender, EventArgs e)
    {
        TextBox commTextBox = ((TextBox)CommentLoginView.FindControl("CommentTextBox"));
        if (commTextBox.Text.CompareTo("") != 0)
        {
            DataBaseManager.InsertCommentForPic(ViewState["PicId"].ToString(),
                    ((Guid)(Membership.GetUser().ProviderUserKey)).ToString(),
                    commTextBox.Text);

            commTextBox.Text = "";
        }

        BindCommentsGridView();
    }

    private void BindCommentsGridView()
    {
        if (ViewState["PicId"] != null)
        {
            CommentsGridView.DataSource = DataBaseManager.GetCommentsForPic(ViewState["PicId"].ToString());
            DataBind();
        }
    }
}
