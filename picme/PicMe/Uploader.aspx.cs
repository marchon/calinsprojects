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
using System.IO;
using System.Drawing;
using System.Drawing.Imaging;

public partial class Manager : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        if (!User.Identity.IsAuthenticated)
            Server.Transfer("Login.aspx?redirect=Uploader.aspx");
        else
        {
            ((HtmlGenericControl)Master.FindControl("uploader")).Attributes.Add("class", "current_page_item");

            ((HtmlGenericControl)Master.FindControl("BodyControl")).Attributes.Add("onload",
                "doUploadInit('uploadContainer', 'toUploadFilesListContainer', 'Not a valid format!', " +
                "new Array('jpeg', 'jpg', 'gif', 'png', 'bmp'), '" + UploadButton.ClientID + "');");
        }
    }
    protected void UploadButton_Click(object sender, EventArgs e)
    {
        HttpFileCollection uploadedFiles = Request.Files;

        for (int i = 0; i < uploadedFiles.Count; i++)
        {
            HttpPostedFile postedFile = uploadedFiles[i];

            if (postedFile.ContentLength > 0)
            {
                try
                {
                    string picName = "Untitled";

                    try
                    {
                        string fullName = postedFile.FileName;
                        int sind = fullName.LastIndexOf('\\') + 1;
                        int len = fullName.LastIndexOf('.') - sind;
                        picName = fullName.Substring(sind, len);
                    }
                    catch (Exception)
                    {
                    }

                    SaveAndRegisterNewUploadedPicture(postedFile.InputStream, picName);
                }
                catch (Exception ex)
                {
                        ErrMsgDiv.InnerHtml = Errors.UPLOAD_ERROR_PREFIX + ex.Message + "<br />";
                }
            }
        }
    }

    protected void SaveAndRegisterNewUploadedPicture(Stream pictureStream, string pictureName) 
    {
        //generate picture name
        string uniqueId = Guid.NewGuid().ToString();
   
        //paths
        string originalFolder = Path.Combine(Request.PhysicalApplicationPath, Parameters.ORIGINAL_FOLDER);
        string bigFolder = Path.Combine(Request.PhysicalApplicationPath, Parameters.BIG_FOLDER);
        string smallFolder = Path.Combine(Request.PhysicalApplicationPath, Parameters.SMALL_FOLDER);
        string thumbFolder = Path.Combine(Request.PhysicalApplicationPath, Parameters.THUMBS_FOLDER);

        //paths + identifiers
        string fullOriginalPath = Path.Combine(originalFolder, uniqueId + Parameters.DEFAULT_FORMAT);
        string fullBigPath = Path.Combine(bigFolder, uniqueId + Parameters.DEFAULT_FORMAT);
        string fullSmallPath = Path.Combine(smallFolder, uniqueId + Parameters.DEFAULT_FORMAT);
        string fullThumbPath = Path.Combine(thumbFolder, uniqueId + Parameters.DEFAULT_FORMAT);

        Bitmap uploadedPhoto;

        try
        {
            //make bitmap object
            uploadedPhoto = new Bitmap(pictureStream);
            Util.saveJpeg(fullOriginalPath, uploadedPhoto);
        }
        catch (Exception)
        {
            throw new Exception(Errors.UPLOAD_ERROR_FORMAT);
        }

        try
        {

            /* big picture */
            //if the picture is bigger then the limits adjust it
            Util.FitPictureInDimensions(ref uploadedPhoto,
                Parameters.PIC_BIG_MAX_WIDTH, Parameters.PIC_BIG_MAX_HEIGHT);
            //save the picture
            Util.saveJpeg(fullBigPath, uploadedPhoto);
            /* big picture */

            /* small picture */
            //if the picture is bigger then the limits adjust it
            Util.FitPictureInDimensions(ref uploadedPhoto,
                Parameters.PIC_SMALL_MAX_WIDTH, Parameters.PIC_SMALL_MAX_HEIGHT);
            //save the picture
            Util.saveJpeg(fullSmallPath, uploadedPhoto);
            /* small picture */
            
            Bitmap thumbnailPhoto;

            thumbnailPhoto = Util.MiddleCropToDimensions(uploadedPhoto,
                Parameters.PIC_THUMB_WIDTH, Parameters.PIC_THUMB_HEIGHT);

            Util.saveJpeg(fullThumbPath, thumbnailPhoto);

            thumbnailPhoto.Dispose();

            //dispose bitmap
            uploadedPhoto.Dispose();

            //register in database
            DataBaseManager.AddPicture(((Guid)(Membership.GetUser().ProviderUserKey)).ToString(),
                uniqueId, pictureName);
        }
        catch (Exception)
        {
            if (File.Exists(fullOriginalPath))
                File.Delete(fullOriginalPath);

            if (File.Exists(fullBigPath))
                File.Delete(fullBigPath);

            if (File.Exists(fullSmallPath))
                File.Delete(fullSmallPath);

            if (File.Exists(fullThumbPath))
                File.Delete(fullThumbPath);

            throw new Exception(Errors.UPLOAD_ERROR_SERVER);
        }
    }
}
