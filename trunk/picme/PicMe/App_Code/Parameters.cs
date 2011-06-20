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
/// Summary description for Parameters
/// </summary>
public class Parameters
{
    public static string DEFAULT_FORMAT = ".jpeg";

    public static string ORIGINAL_FOLDER = @"Pictures\Original";
    public static string SMALL_FOLDER = @"Pictures\Small";
    public static string BIG_FOLDER = @"Pictures\Big";
    public static string THUMBS_FOLDER = @"Pictures\Thumbs";

    public static int PIC_SMALL_MAX_WIDTH = 250;
    public static int PIC_SMALL_MAX_HEIGHT = 250;

    public static int PIC_BIG_MAX_WIDTH = 500;
    public static int PIC_BIG_MAX_HEIGHT = 500;

    public static int PIC_THUMB_WIDTH = 80;
    public static int PIC_THUMB_HEIGHT = 80;
}
