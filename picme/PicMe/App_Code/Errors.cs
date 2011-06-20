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
/// Summary description for Errors
/// </summary>
public class Errors
{
    public static string UPLOAD_ERROR_PREFIX = "Sorry. An error ocured: ";
    public static string UPLOAD_ERROR_FORMAT = "Picture format error!";
    public static string UPLOAD_ERROR_SERVER = "Server internal error. Please try again...";
}
