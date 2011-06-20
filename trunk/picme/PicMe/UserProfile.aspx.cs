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
using System.Globalization;
using System.Drawing;
using System.IO;

public partial class UserProfile : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        if (!User.Identity.IsAuthenticated)
            Server.Transfer("Login.aspx?redirect=UserProfile.aspx");
        else
        {
            ((HtmlGenericControl)Master.FindControl("profile")).Attributes.Add("class", "current_page_item");

            if (!IsPostBack)
            {
                /*populate birth date dropdownlists*/
                //day
                Day_DropDownList.Items.Add(new ListItem("--Day--", "0"));
                for (int day = 1; day <= 31; day++)
                    Day_DropDownList.Items.Add(new ListItem(day.ToString(), day.ToString()));

                //month
                DateTimeFormatInfo dtf = new DateTimeFormatInfo();
                Month_DropDownList.Items.Add(new ListItem("--Month--", "0"));
                for (int month = 1; month <= 12; month++)
                    Month_DropDownList.Items.Add(new ListItem(dtf.GetAbbreviatedMonthName(month).ToString(), month.ToString()));

                //year
                int currYear = DateTime.Now.Year;

                Year_DropDownList.Items.Add(new ListItem("--Year--", "0"));
                for (int year = 1; year <= 50; year++)
                    Year_DropDownList.Items.Add(new ListItem((currYear - year + 1).ToString(), year.ToString()));
                /*populate birth date dropdownlists*/

                /*populate with user details if any*/
                UserExtraInfo info = DataBaseManager.GetUserExtraInfo(((Guid)(Membership.GetUser().ProviderUserKey)).ToString());
                if (info != null)
                {
                    Name_TextBox.Text = info.RealName;
                    Gender_RadioButtonList.SelectedIndex = Convert.ToInt32(info.Gender);
                    if (currYear - 50 < info.BirthDate.Year && info.BirthDate.Year <= currYear)
                    {
                        Day_DropDownList.SelectedIndex = info.BirthDate.Day;
                        Month_DropDownList.SelectedIndex = info.BirthDate.Month;
                        Year_DropDownList.SelectedIndex = currYear - info.BirthDate.Year + 1;
                    }
                    Country_TextBox.Text = info.Country;
                    City_TextBox.Text = info.City;
                    Description_TextBox.Text = info.Description;
                }
                else
                    Gender_RadioButtonList.SelectedIndex = 0;
                /*populate with user details if any*/
            }
        }
    }
    protected void Upload_Button_Click(object sender, EventArgs e)
    {
        //avatar
        if (Avatar_FileUpload.HasFile)
        {
            Bitmap original = new Bitmap(Avatar_FileUpload.FileContent);

            Bitmap croped = Util.MiddleCropToDimensions(original, 40, 40);

            original.Dispose();

            string uniqueId = DataBaseManager.GetUserAvatar(((Guid)(Membership.GetUser().ProviderUserKey)).ToString());

            if (uniqueId == null)
            {

                uniqueId = Guid.NewGuid().ToString();
                DataBaseManager.SetUserAvatar(((Guid)(Membership.GetUser().ProviderUserKey)).ToString(), uniqueId);
            }

            string path = Path.Combine(Path.Combine(Request.PhysicalApplicationPath, "Avatars"), uniqueId + ".jpeg");
            
            //croped.Save(path, System.Drawing.Imaging.ImageFormat.Gif);
            Util.saveJpeg(path, croped);

            //need to rerender the page if we want to show the new uploaded avatar
            Server.Transfer("UserProfile.aspx");
        }
    }
    protected void Save_Button_Click(object sender, EventArgs e)
    {
        //details
        //make date
        DateTime dt;

        if(Year_DropDownList.SelectedIndex > 0 && 
                    Month_DropDownList.SelectedIndex > 0 && Day_DropDownList.SelectedIndex > 0)
            dt = new DateTime(DateTime.Now.Year - Year_DropDownList.SelectedIndex + 1, 
                    Month_DropDownList.SelectedIndex, Day_DropDownList.SelectedIndex);
        else dt = DateTime.MaxValue;

        DataBaseManager.SetUserExtraInfo(((Guid)(Membership.GetUser().ProviderUserKey)).ToString(),                         new UserExtraInfo(Name_TextBox.Text, 
                    Convert.ToBoolean(Gender_RadioButtonList.SelectedIndex),
                    dt, 
                    Country_TextBox.Text, City_TextBox.Text, Description_TextBox.Text));
    }
}
