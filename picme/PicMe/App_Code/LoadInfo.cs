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
using System.Collections.Generic;

/// <summary>
/// Summary description for LoadInfo
/// </summary>
public class LoadInfo
{

    List<ASet> setList;
    List<APicture> picList;

    public List<ASet> SetList
    {
        set { setList = value; }
        get { return setList; }
    }

    public List<APicture> PicList
    {
        set { picList = value; }
        get { return picList; }
    }

}
