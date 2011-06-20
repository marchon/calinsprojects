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
/// Summary description for APicture
/// </summary>
public class APicture
{
    private string id;
    private string name;
    private string description;
    private string tags;

    public APicture()
    {
        this.id = null;
        this.name = null;
        this.description = null;
        this.tags = null;
    }

	public APicture(string id, string name, string description, string tags)
	{
        this.id = id;
        this.name = name;
        this.description = description;
        this.tags = tags;
	}

    public string Id
    {
        set { id = value; }
        get { return id; }
    }
    public string Name
    {
        set { name = value; }
        get { return name; }
    }
    public string Description
    {
        set { description = value; }
        get { return description; }
    }

    public string Tags
    {
        set { tags = value; }
        get { return tags; }
    }
}
