using System;
using System.Data;
using System.Configuration;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Xml.Linq;

/// <summary>
/// Summary description for UserExtraInfo
/// </summary>
public class UserExtraInfo
{
    private string realName;
    private bool gender;
    private DateTime birthDate;
    private string country;
    private string city;
    private string description;

	public UserExtraInfo()
	{
        realName = null;
        birthDate = DateTime.MinValue;
        country = null;
        city = null;
        description = null;
	}

    public UserExtraInfo(string name, bool gen, DateTime date, string country, string city, string descr)
    {
        realName = name;
        gender = gen;
        birthDate = date;
        this.country = country;
        this.city = city;
        description = descr;
    }

    public string RealName
    {
        set { realName = value; }
        get { return realName; }
    }
    public bool Gender
    {
        set { gender = value; }
        get { return gender; }
    }
    public DateTime BirthDate
    {
        set { birthDate = value; }
        get { return birthDate; }
    }
    public string Country
    {
        set { country = value; }
        get { return country; }
    }
    public string City
    {
        set { city = value; }
        get { return city; }
    }
    public string Description
    {
        set { description = value; }
        get { return description; }
    }
}
