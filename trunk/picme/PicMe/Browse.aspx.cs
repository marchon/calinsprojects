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


public partial class Browse : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        ((HtmlGenericControl)Master.FindControl("browse")).Attributes.Add("class", "current_page_item");

        if (!IsPostBack)
        {
            if (ViewState["CurrentQuery"] == null)
                ViewState.Add("CurrentQuery", "");

            if (ViewState["CurrentSearchBy"] == null)
                ViewState.Add("CurrentSearchBy", 0);

            if (Request["query"] != null)
            {
                ViewState["CurrentQuery"] = Request["query"];
                ViewState["CurrentSearchBy"] = 0;

                DoGridViewDataBind(Request["query"], 0, 0);

                SearchTextBox.Text = Request["query"];
                SearchByRadioButtonList.SelectedIndex = 0;
            }
            else if (Request["tag"] != null)
            {
                ViewState["CurrentQuery"] = Request["tag"];
                ViewState["CurrentSearchBy"] = 1;

                DoGridViewDataBind(Request["tag"], 0, 1);

                SearchTextBox.Text = Request["tag"];
                SearchByRadioButtonList.SelectedIndex = 1; 
            }
            else
            {
                ViewState["CurrentQuery"] = "100";
                ViewState["CurrentSearchBy"] = 2;

                DoGridViewDataBind("100", 0, 2);
            }
        }
    }

    protected void SearchButton_Click(object sender, EventArgs e)
    {
        ViewState["CurrentQuery"] = SearchTextBox.Text;
        ViewState["CurrentSearchBy"] = SearchByRadioButtonList.SelectedIndex;
        DoGridViewDataBind(SearchTextBox.Text, 0, SearchByRadioButtonList.SelectedIndex);
    }

    protected void SearchResultGridView_SelectedIndexChanging(object sender, GridViewPageEventArgs e)
    {
        ///ciudat, inainte nu trebuia asta...
        DoGridViewDataBind((string)ViewState["CurrentQuery"], e.NewPageIndex, (int)(ViewState["CurrentSearchBy"]));
    }

    protected void DoGridViewDataBind(string query, int pageIndex, int searchType)
    {
        SqlDataSource ds = DataBaseManager.SearchPicturesByKeyWord(query, searchType);
        ds.Selected += new SqlDataSourceStatusEventHandler(GetTotalRecords);
        SearchResultGridView.DataSource = ds;
        SearchResultGridView.PageIndex = pageIndex;
        SearchResultGridView.DataBind();
    }

    void GetTotalRecords(object sender, SqlDataSourceStatusEventArgs e)
    {
        switch((int)ViewState["CurrentSearchBy"])
        {
            case 0:
                SearchResultGridView.Caption = "<b>" + e.AffectedRows.ToString() +
                    "</b> pics were found whose name matches with <b>" +
                    (string)ViewState["CurrentQuery"] + "</b>.";
                break;
            case 1:
                SearchResultGridView.Caption = "<b>" + e.AffectedRows.ToString() +
                    "</b> pics were found tagged with <b>" +
                    (string)ViewState["CurrentQuery"] + "</b>.";
                break;
            default:
                SearchResultGridView.Caption = "These are the last <b>" +
                    e.AffectedRows.ToString() +
                    "</b> pics uploaded on picMe. ";
                break;
        }
    }

    
}
