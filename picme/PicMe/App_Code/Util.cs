using System;
using System.Data;
using System.Configuration;
using System.IO;
using System.Drawing;
using System.Drawing.Imaging;
using System.Drawing.Drawing2D;
using System.Collections.Generic;

/// <summary>
/// Summary description for Util
/// </summary>
/// 

public class Util
{
    public static void FitPictureInDimensions(ref Bitmap bitmap, int width, int height)
    {
        if (bitmap.Size.Width > width || bitmap.Size.Height > height)
        {
            Bitmap newBitmap;

            if (bitmap.Size.Width - width > bitmap.Size.Height - height)
            {
                newBitmap = resizeImage(bitmap, new Size(width, (width * bitmap.Size.Height) / bitmap.Size.Width));
            }
            else
            {
                newBitmap = resizeImage(bitmap, new Size((height * bitmap.Size.Width) / bitmap.Size.Height, height));
            }

            bitmap.Dispose();
            bitmap = newBitmap;
        }
    }

    public static Bitmap MiddleCropToDimensions(Bitmap bitmap, int width, int height)
    {
        if (bitmap.Size.Width > width || bitmap.Size.Height > height)
        {
            Bitmap newBitmap;

            if (bitmap.Size.Width - width > bitmap.Size.Height - height)
            {
                bitmap = resizeImage(bitmap, new Size((height * bitmap.Size.Width) / bitmap.Size.Height, height));
                newBitmap = cropImage(bitmap, new Rectangle((bitmap.Size.Width - width) / 2, 0, width, height));
            }
            else
            {
                bitmap = resizeImage(bitmap, new Size(width, (width * bitmap.Size.Height) / bitmap.Size.Width));

                newBitmap = cropImage(bitmap, new Rectangle(0, (bitmap.Size.Height - height) / 2, width, height));
            }

            bitmap.Dispose();
            bitmap = newBitmap;
        }

        return bitmap;
    }

    private static Bitmap resizeImage(Bitmap imgToResize, Size size)
    {
        Bitmap b = new Bitmap(size.Width, size.Height);
        Graphics g = Graphics.FromImage((Image)b);
        g.InterpolationMode = InterpolationMode.HighQualityBicubic;

        g.DrawImage(imgToResize, 0, 0, size.Width, size.Height);
        g.Dispose();

        return b;
    }

    private static Bitmap cropImage(Bitmap img, Rectangle cropArea)
    {
        Bitmap bmpCrop = img.Clone(cropArea,
        img.PixelFormat);
        return bmpCrop;
    }

    public static void saveJpeg(string path, Bitmap img)
    {
        // Encoder parameter for image quality
        EncoderParameter qualityParam =
           new EncoderParameter(Encoder.Quality, 85L);

        // Jpeg image codec
        ImageCodecInfo jpegCodec = getEncoderInfo("image/jpeg");
        if (jpegCodec == null)
            return;

        EncoderParameters encoderParams = new EncoderParameters(1);
        encoderParams.Param[0] = qualityParam;

        img.Save(path, jpegCodec, encoderParams);
    }

    private static ImageCodecInfo getEncoderInfo(string mimeType)
    {
        // Get image codecs for all image formats
        ImageCodecInfo[] codecs = ImageCodecInfo.GetImageEncoders();

        // Find the correct image codec
        for (int i = 0; i < codecs.Length; i++)
            if (codecs[i].MimeType == mimeType)
                return codecs[i];
        return null;
    }



    /* string work */
    public static string GetAvatar(object name)
    {
        string avatarName = name as string;
        if (avatarName == null)
            return "~/Avatars/default.gif";

        return "~/Avatars/" + avatarName + ".jpeg";
    }
    public static string GetDate(object date)
    {
        DateTime dt = (DateTime)date;

        return (dt.Day.ToString() + " " + dt.ToString("MMMM") + " " + dt.Year.ToString());
    }

    public static string GetBrowseTagLinks(object tags)
    {
        string theTags = tags as string;

        if (theTags == null)
            return "There are no tags for this photo.";

        List<string> tagList = new List<string>();
        int index = 0;
        while (index < theTags.Length)
        {
            int newIndex = theTags.IndexOf(' ', index); ///todo:vezi aici
            int len = ((newIndex == -1) ? theTags.Length : newIndex) - index;
            tagList.Add(theTags.Substring(index, len));

            if (newIndex == -1)
                break;

            index = (newIndex + 1);
        }

        string result = "<a href=\"Browse.aspx?tag=" + tagList[0] + "\">" + tagList[0] + "</a>"; ;

        for (int i = 1; i < tagList.Count; i++)
            result += ", <a href=\"Browse.aspx?tag=" + tagList[i] + "\">" + tagList[i] + "</a>";

        return result;
    }
}
