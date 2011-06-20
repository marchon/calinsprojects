using System;
using System.Collections;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Web.Services.Protocols;
using System.Xml.Linq;
using System.Collections.Generic;
using System.Web.Security;
using System.IO;

public class DeleteSetsResponse
{
    private List<string> undeletedSets = null;
    private List<APicture> refreshedUncategorizedPictures = null;

    public List<string> UndeletedSets
    {
        set { undeletedSets = value; }
        get { return undeletedSets; }
    }
    public List<APicture> RefreshedUncategorizedPictures
    {
        set { refreshedUncategorizedPictures = value; }
        get { return refreshedUncategorizedPictures; }
    }
}

/// <summary>
/// Summary description for Ajax_WebService
/// </summary>
[WebService(Namespace = "http://tempuri.org/")]
[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
// To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
[System.Web.Script.Services.ScriptService]
public class Ajax_WebService : System.Web.Services.WebService {

    public Ajax_WebService () {

        //Uncomment the following line if using designed components 
        //InitializeComponent(); 
    }

    [WebMethod]
    public List<APicture> GetPicturesInSet(string setId)
    {
        System.Threading.Thread.Sleep(1000); //simuleaza un server real

        List<APicture> picList;
        try
        {
            picList = DataBaseManager.GetPicturesInSet(setId);
        }
        catch (Exception ex)
        {
            picList = new List<APicture>();
        }

        return picList;
    }

    [WebMethod]
    public APicture GetPictureDetails(string picId)
    {
        System.Threading.Thread.Sleep(1000); //simuleaza un server real

        return DataBaseManager.GetPictureDetails(picId);
    }

    [WebMethod]
    public bool SetPictureDetails(string picId, string name, string description, string tags)
    {
        System.Threading.Thread.Sleep(1000); //simuleaza un server real

        try
        {
            DataBaseManager.SetPictureDetails(new APicture(picId, name, description, tags));
        }
        catch (Exception)
        {
            return false;
        }

        return true;
    }

    [WebMethod]
    public LoadInfo GetLoadInfo()
    {
        System.Threading.Thread.Sleep(1000); //simuleaza un server real
        LoadInfo result = new LoadInfo();

        result.SetList = DataBaseManager.GetUserSets(((Guid)(Membership.GetUser().ProviderUserKey)).ToString());
        result.PicList = DataBaseManager.GetUncotegorizedUserPictures(((Guid)(Membership.GetUser().ProviderUserKey)).ToString());

        return result;
    }

    [WebMethod]
    public string CreateNewSet(string newSetName, string newSetDescription)
    {
        System.Threading.Thread.Sleep(1000); //simuleaza un server real

        return DataBaseManager.CreateNewSet(
            ((Guid)Membership.GetUser().ProviderUserKey).ToString(),
            newSetName, newSetDescription);
    }

    [WebMethod]
    public string GetSetDescription(string setId)
    {
        System.Threading.Thread.Sleep(1000); //simuleaza un server real

        return DataBaseManager.GetSetDescription(setId);
    }
    [WebMethod]
    public bool UpdateSet(string setId, string name, string description)
    {
        System.Threading.Thread.Sleep(1000); //simuleaza un server real

        return DataBaseManager.UpdateSet(new ASet(setId, name, description));
    }

    [WebMethod]
    public DeleteSetsResponse DeleteSets(List<string> setIds)
    {
        System.Threading.Thread.Sleep(1000); //simuleaza un server real

        DeleteSetsResponse result = new DeleteSetsResponse();

        result.UndeletedSets = new List<string>();
        result.RefreshedUncategorizedPictures = new List<APicture>();
        for(int i = 0; i < setIds.Count; i++)
        {
            List<APicture> picLst = DataBaseManager.DeleteSet(setIds[i]);
            if (picLst == null)
                result.UndeletedSets.Add(setIds[i]);
            else
                result.RefreshedUncategorizedPictures.AddRange(picLst);
        }

        return result;
    }

    [WebMethod]
    public List<string> PermanentlyDeletePics(List<string> picIds)
    {
        System.Threading.Thread.Sleep(1000); //simuleaza un server real

        List<string> result = new List<string>();

        for (int i = 0; i < picIds.Count; i++)
        {
            if (!DataBaseManager.DeletePic(picIds[i]))
                result.Add(picIds[i]);
            else
            {
                string appPath = HttpContext.Current.Request.PhysicalApplicationPath;
                string file = picIds[i] + ".jpeg";
                File.Delete(Path.Combine(Path.Combine(appPath, Parameters.ORIGINAL_FOLDER), file));
                File.Delete(Path.Combine(Path.Combine(appPath, Parameters.BIG_FOLDER), file));
                File.Delete(Path.Combine(Path.Combine(appPath, Parameters.SMALL_FOLDER), file));
                File.Delete(Path.Combine(Path.Combine(appPath, Parameters.THUMBS_FOLDER), file));
            }
        }

        return result;
    }

    [WebMethod]
    public List<string> CopyPictureInSet(List<string> picIds, string destSetId)
    {
        System.Threading.Thread.Sleep(1000); //simuleaza un server real

        List<string> response = new List<string>(); 

        for (int i = 0; i < picIds.Count; i++)
            if(!DataBaseManager.AddPictureInSet(picIds[i], destSetId))
                response.Add(picIds[i]);

        return response;
    }
    [WebMethod]
    public List<string> MovePictureInSet(List<string> picIds, string srcSetId, string destSetId)
    {
        System.Threading.Thread.Sleep(1000); //simuleaza un server real

        List<string> response = new List<string>(); 

        for (int i = 0; i < picIds.Count; i++)
        {
            if (!DataBaseManager.RemovePictureFromSet(picIds[i], srcSetId))
            {
                response.Add(picIds[i]);
                continue;
            }

            if (!DataBaseManager.AddPictureInSet(picIds[i], destSetId))
            {
                response.Add(picIds[i]);
                DataBaseManager.AddPictureInSet(picIds[i], srcSetId); //re-add to source set
            }
        }

        return response;
    }

    [WebMethod]
    public List<string> DeletePicsFromSet(List<string> picIds, string setId)
    {
        List<string> uncategorizedAfterDelete = new List<string>();
        for (int i = 0; i < picIds.Count; i++)
        {
            if (DataBaseManager.RemovePictureFromSet_1(picIds[i], setId))
                uncategorizedAfterDelete.Add(picIds[i]);
        }

        return uncategorizedAfterDelete;
    }
}