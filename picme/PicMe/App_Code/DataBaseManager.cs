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
using System.Data.SqlClient;

/// <summary>
/// Summary description for DataBaseManager
/// </summary>
public class DataBaseManager
{
    private static string connectionString = ConfigurationManager.ConnectionStrings["ASPNETDBConnectionString"].ConnectionString;

    public static void AddPicture(string userId, string id, string name)
    {
        SqlConnection dbConn = new SqlConnection(connectionString);

        SqlCommand sqlComm = new SqlCommand(
            @"INSERT INTO site_Picture
                (PicId, UserId, Name, UploadDate)
                VALUES  (@pic_id, @user_id, @pic_name, @upload_date)", dbConn);

        sqlComm.Parameters.AddWithValue("pic_id", id);
        sqlComm.Parameters.AddWithValue("user_id", userId);
        sqlComm.Parameters.AddWithValue("pic_name", name);
        sqlComm.Parameters.AddWithValue("upload_date", DateTime.Now);

        dbConn.Open();

        try
        {
            sqlComm.ExecuteNonQuery();
        }
        catch (Exception ex)
        {
            dbConn.Close();
            throw ex;
        }

        dbConn.Close();      
    }

	public static List<APicture> GetPicturesInSet(string setId)
    {
        List<APicture> picList = new List<APicture>();

        SqlConnection dbConn = new SqlConnection(connectionString);

        SqlCommand sqlComm = new SqlCommand(
            @"SELECT site_Picture.PicId, site_Picture.Name
              FROM site_Picture, site_Picture_in_Set
              WHERE (site_Picture_in_Set.SetId = @set_id) AND
                    (site_Picture_in_Set.PicId = site_Picture.PicId)", dbConn);

        sqlComm.Parameters.AddWithValue("set_id", setId);

        dbConn.Open();

        using (SqlDataReader sqlDr = sqlComm.ExecuteReader(CommandBehavior.CloseConnection))
        {
            while (sqlDr.Read())
            {
                picList.Add(new APicture(sqlDr.GetGuid(0).ToString(),
                                            sqlDr.GetString(1),
                                            null, null));
            }

            sqlDr.Close();
        }

        return picList;
    }
    public static List<APicture> GetUncotegorizedUserPictures(string userId)
    {
        List<APicture> picList = new List<APicture>();

        SqlConnection dbConn = new SqlConnection(connectionString);

        SqlCommand sqlComm = new SqlCommand(
            @"SELECT PicId, Name
              FROM site_Picture
              WHERE (site_Picture.UserId = @user_id) AND
                    (NOT EXISTS(
                        SELECT *
                        FROM site_Picture_in_Set
                        WHERE site_Picture.PicId = site_Picture_in_Set.PicId
                    ))", dbConn);

        sqlComm.Parameters.AddWithValue("user_id", userId);

        dbConn.Open();

        using (SqlDataReader sqlDr = sqlComm.ExecuteReader(CommandBehavior.CloseConnection))
        {
            while (sqlDr.Read())
            {
                picList.Add(new APicture(sqlDr.GetGuid(0).ToString(),
                                            sqlDr.GetString(1), 
                                            null, null));
            }

            sqlDr.Close();
        }

        return picList;
    }

    public static void SetPictureDetails(APicture picInfo)
    {
        SqlConnection dbConn = new SqlConnection(connectionString);

        SqlCommand sqlComm = new SqlCommand(
            @"UPDATE site_Picture
                SET Name = @name, Description = @description, Tags = @tags
                WHERE PicId = @pic_id", dbConn);

        sqlComm.Parameters.AddWithValue("pic_id", picInfo.Id);
        sqlComm.Parameters.AddWithValue("name", picInfo.Name);
        sqlComm.Parameters.AddWithValue("description", picInfo.Description);
        sqlComm.Parameters.AddWithValue("tags", picInfo.Tags);       

        dbConn.Open();

        sqlComm.ExecuteNonQuery();

        dbConn.Close();
    }

    public static APicture GetPictureDetails(string picId)
    {
        APicture pic = null;

        SqlConnection dbConn = new SqlConnection(connectionString);

        SqlCommand sqlComm = new SqlCommand(
            @"SELECT Description, Tags
              FROM site_Picture
              WHERE (PicId = @pic_id)", dbConn);

        sqlComm.Parameters.AddWithValue("pic_id", picId);

        dbConn.Open();

        using (SqlDataReader sqlDr = sqlComm.ExecuteReader(CommandBehavior.CloseConnection))
        {
            sqlDr.Read();

            try
            {
                pic = new APicture(null, null, sqlDr.GetString(0), sqlDr.GetString(1));
            }
            catch(Exception)
            {
                pic = new APicture(null, null, null, null);
            }

            sqlDr.Close();
        }

        return pic;
    }

    public static List<ASet> GetUserSets(string userId)
    {
        List<ASet> setList = new List<ASet>();

        SqlConnection dbConn = new SqlConnection(connectionString);

        SqlCommand sqlComm = new SqlCommand(
            @"SELECT SetId, Name, Description
              FROM site_Set
              WHERE (UserId = @user_id)", dbConn);

        sqlComm.Parameters.AddWithValue("user_id", userId);

        dbConn.Open();

        using (SqlDataReader sqlDr = sqlComm.ExecuteReader(CommandBehavior.CloseConnection))
        {
            while (sqlDr.Read())
            {
                setList.Add(new ASet(sqlDr.GetSqlGuid(0).ToString(),
                                            sqlDr.GetString(1),
                                            sqlDr.GetString(2)));
            }

            sqlDr.Close();
        }

        return setList;
    }

    public static void SetUserExtraInfo(string userId, UserExtraInfo userInfo)
    {
        SqlConnection dbConn = new SqlConnection(connectionString);

        SqlCommand sqlComm = new SqlCommand(
            @"UPDATE site_User_Extra_Info
                SET UserFullName = @UserFullName, UserGender = @UserGender,
                UserBirthDate = @UserBirthDate, UserCountry = @UserCountry, UserCity = @UserCity,                          UserDescription = @UserDescription
                WHERE UserId = @UserId", dbConn);

        sqlComm.Parameters.AddWithValue("UserFullName", userInfo.RealName);
        sqlComm.Parameters.AddWithValue("UserGender", userInfo.Gender); 
        sqlComm.Parameters.AddWithValue("UserBirthDate", userInfo.BirthDate);
        sqlComm.Parameters.AddWithValue("UserCountry", userInfo.Country);
        sqlComm.Parameters.AddWithValue("UserCity", userInfo.City);
        sqlComm.Parameters.AddWithValue("UserDescription", userInfo.Description);
        sqlComm.Parameters.AddWithValue("UserId", userId);

        dbConn.Open();

        sqlComm.ExecuteNonQuery();

        dbConn.Close();
    }

    public static UserExtraInfo GetUserExtraInfo(string userId)
    {
        SqlConnection dbConn = new SqlConnection(connectionString);

        SqlCommand sqlComm = new SqlCommand(
            @"SELECT UserFullName, UserGender, UserBirthDate, UserCountry, UserCity, UserDescription
              FROM site_User_Extra_Info
              WHERE (UserId = @user_id)", dbConn);

        sqlComm.Parameters.AddWithValue("user_id", userId);

        dbConn.Open();

        UserExtraInfo retVal = null;

        using (SqlDataReader sqlDr = sqlComm.ExecuteReader(CommandBehavior.CloseConnection))
        {
            sqlDr.Read();
            try
            {
                retVal = new UserExtraInfo(sqlDr.GetString(0),
                    sqlDr.GetBoolean(1), sqlDr.GetDateTime(2), sqlDr.GetString(3), sqlDr.GetString(4),
                    sqlDr.GetString(5));
            }
            catch (Exception ex)
            {
                retVal = null;
            }
            sqlDr.Close();
        }

        return retVal;
    }

    public static void SetUserAvatar(string userId, string avatarFileName)
    {
        SqlConnection dbConn = new SqlConnection(connectionString);

        SqlCommand sqlComm = new SqlCommand(
            @"UPDATE site_User_Extra_Info
                SET UserAvatar = @UserAvatar
                WHERE UserId = @UserId", dbConn);

        sqlComm.Parameters.AddWithValue("UserAvatar", avatarFileName);
        sqlComm.Parameters.AddWithValue("UserId", userId);

        dbConn.Open();

        sqlComm.ExecuteNonQuery();

        dbConn.Close();
    }

    public static string GetUserAvatar(string userId)
    {
        SqlConnection dbConn = new SqlConnection(connectionString);

        SqlCommand sqlComm = new SqlCommand(
            @"SELECT UserAvatar
              FROM site_User_Extra_Info
              WHERE (UserId = @user_id)", dbConn);

        sqlComm.Parameters.AddWithValue("user_id", userId);

        dbConn.Open();

        string retVal = null;


        using (SqlDataReader sqlDr = sqlComm.ExecuteReader(CommandBehavior.CloseConnection))
        {
            sqlDr.Read();
            try
            {
                retVal = sqlDr.GetString(0);
            }
            catch (Exception)
            {
            }
            sqlDr.Close();
        }

        return retVal;
    }

    public static SqlDataSource SearchPicturesByKeyWord(string query, int searchType)
    {

        string command = "SELECT " + ((searchType == 2) ? ("TOP " + query): "") +
                         @"site_Picture.PicId pic_id, site_Picture.Name pic_name,
                         site_Picture.Tags pic_tags,
                         site_Picture.UploadDate pic_upload_date,
                         site_User_Extra_Info.UserAvatar user_avatar, 
                         site_User_Extra_Info.UserFullName user_name
                         FROM site_Picture 
                         INNER JOIN site_User_Extra_Info 
                         ON site_Picture.UserId = site_User_Extra_Info.UserId    
                         " + ((searchType == 2) ? "ORDER BY site_Picture.UploadDate DESC" : 
                         "WHERE (site_Picture." + ((searchType == 0) ? "Name" : "Tags") + 
                         " LIKE '%" + query + "%')");

        return new SqlDataSource(connectionString,
            command);
    }

    public static SqlDataSource GetCommentsForPic(string picId)
    {
        string command = @"SELECT site_Comment.Comment user_comment,
                         site_Comment.CommentDate user_comment_date,    
                         site_User_Extra_Info.UserAvatar user_avatar,
                         site_User_Extra_Info.UserFullName user_name
                         FROM site_Comment
                         INNER JOIN site_User_Extra_Info
                         ON site_Comment.UserId = site_User_Extra_Info.UserId
                         WHERE (site_Comment.PicId = @pic_id)
                         ORDER BY site_Comment.CommentDate DESC";

        SqlDataSource ds =  new SqlDataSource(connectionString,
            command);
        ds.SelectParameters.Add("pic_id", picId);

        return ds;
    }
    public static bool InsertCommentForPic(string picId, string userId, string comment)
    {
        SqlConnection dbConn = new SqlConnection(connectionString);

        SqlCommand sqlComm = new SqlCommand(
            @"INSERT INTO site_Comment
                (PicId, UserId, Comment, CommentDate)
                VALUES  (@pic_id, @user_id, @comment, @comment_date)", dbConn);

        bool retVal = true;

        sqlComm.Parameters.AddWithValue("pic_id", picId);
        sqlComm.Parameters.AddWithValue("user_id", userId);
        sqlComm.Parameters.AddWithValue("comment", comment);
        sqlComm.Parameters.AddWithValue("comment_date", DateTime.Now);   

        dbConn.Open();

        try
        {
            sqlComm.ExecuteNonQuery();
        }
        catch(Exception)
        {
            retVal = false;
        }
        finally
        {
            dbConn.Close();
        }

        return retVal;
    }

    public static string CreateNewSet(string userId, string name, string description)
    {
        SqlConnection dbConn = new SqlConnection(connectionString);

        SqlCommand sqlComm = new SqlCommand(
            @"INSERT INTO site_Set
                (SetId, UserId, Name, Description)
                VALUES  (@set_id, @user_id, @name, @description)", dbConn);

        string retVal = Guid.NewGuid().ToString();

        sqlComm.Parameters.AddWithValue("set_id", retVal);
        sqlComm.Parameters.AddWithValue("user_id", userId);
        sqlComm.Parameters.AddWithValue("name", name);
        sqlComm.Parameters.AddWithValue("description", description);

        dbConn.Open();
     
        try
        {
            sqlComm.ExecuteNonQuery();
        }
        catch
        {
            retVal = null;
        }
        finally
        {
            dbConn.Close();
        }

        return retVal;
    }

    public static string GetSetDescription(string setId)
    {
        SqlConnection dbConn = new SqlConnection(connectionString);

        SqlCommand sqlComm = new SqlCommand(
            @"SELECT Description
              FROM site_Set
              WHERE (SetId = @set_id)", dbConn);

        sqlComm.Parameters.AddWithValue("set_id", setId);

        dbConn.Open();

        string retVal = null;

        using (SqlDataReader sqlDr = sqlComm.ExecuteReader(CommandBehavior.CloseConnection))
        {
            sqlDr.Read();
            try
            {
                retVal = sqlDr.GetString(0);
            }
            catch (Exception)
            {
            }
            sqlDr.Close();
        }

        return retVal;
    }

    public static bool UpdateSet(ASet setInfo)
    {
        SqlConnection dbConn = new SqlConnection(connectionString);

        SqlCommand sqlComm = new SqlCommand(
            @"UPDATE site_Set
                SET Name = @name, Description = @description
                WHERE SetId = @set_id", dbConn);

        sqlComm.Parameters.AddWithValue("set_id", setInfo.Id);
        sqlComm.Parameters.AddWithValue("name", setInfo.Name);
        sqlComm.Parameters.AddWithValue("description", setInfo.Description);

        bool retVal = true;

        dbConn.Open();

        try
        {
            sqlComm.ExecuteNonQuery();
        }
        catch
        {
            retVal = false;
        }
        finally
        {
            dbConn.Close();
        }

        return retVal;
    }

    //returns the pics that ramain uncategorized after set delete
    public static List<APicture> DeleteSet(string setId)
    {
        SqlConnection dbConn = new SqlConnection(connectionString);

        SqlCommand sqlComm = dbConn.CreateCommand();
        dbConn.Open();
        sqlComm.Parameters.AddWithValue("set_id", setId);

        List<APicture> retVal = new List<APicture>();
        sqlComm.CommandText =
                @"SELECT site_Picture_in_Set.PicId, site_Picture.Name
                    FROM site_Picture 
                    INNER JOIN site_Picture_in_Set
                    ON site_Picture_in_Set.PicId = site_Picture.PicId
                    WHERE site_Picture_in_Set.PicId IN
                        (
                            SELECT site_Picture_in_Set.PicId
                            FROM site_Picture_in_Set
                            WHERE site_Picture_in_Set.SetId = @set_id
                        )
                    GROUP BY site_Picture_in_Set.PicId, site_Picture.Name
                    HAVING COUNT(site_Picture_in_Set.PicId) = 1
                    ";
        try
        {
            using (SqlDataReader sqlDr = sqlComm.ExecuteReader())
            {
                while (sqlDr.Read())
                {
                    retVal.Add(new APicture(sqlDr.GetGuid(0).ToString(),
                                                sqlDr.GetString(1),
                                                null, null));
                }

                sqlDr.Close();
            }
        }
        catch (Exception ex)
        {
            dbConn.Close();
            return null;
        }

        sqlComm.CommandText = @"DELETE FROM site_Picture_in_Set
                WHERE SetId = @set_id";

        try
        {
            sqlComm.ExecuteNonQuery();
        }
        catch
        {
            dbConn.Close();
            return null;
        }

        sqlComm.CommandText = @"DELETE FROM site_Set
                WHERE SetId = @set_id";

        try
        {
            sqlComm.ExecuteNonQuery();
        }
        catch
        {
            retVal = null;
        }
        finally
        {
            dbConn.Close();
        }

        return retVal;
    }

    public static bool RemoveAllPicturesInSet(string setId)
    {
        SqlConnection dbConn = new SqlConnection(connectionString);

        SqlCommand sqlComm = new SqlCommand(
            @"DELETE FROM site_Picture_in_Set
                WHERE SetId = @set_id", dbConn);

        bool retVal = true;

        sqlComm.Parameters.AddWithValue("set_id", setId);

        dbConn.Open();

        try
        {
            sqlComm.ExecuteNonQuery();
        }
        catch
        {
            retVal = false;
        }
        finally
        {
            dbConn.Close();
        }

        return retVal;
    }

    public static bool DeletePic(string picId)
    {
        SqlConnection dbConn = new SqlConnection(connectionString);

        SqlCommand sqlComm = new SqlCommand(
            @"DELETE FROM site_Picture
                WHERE PicId = @pic_id", dbConn);

        bool retVal = true;

        sqlComm.Parameters.AddWithValue("pic_id", picId);

        dbConn.Open();

        try
        {
            sqlComm.ExecuteNonQuery();
        }
        catch
        {
            retVal = false;
        }
        finally
        {
            dbConn.Close();
        }

        return retVal;
    }

    public static bool AddPictureInSet(string picId, string setId)
    {
        SqlConnection dbConn = new SqlConnection(connectionString);

        SqlCommand sqlComm = new SqlCommand(
            @"INSERT INTO site_Picture_in_Set
                (SetId, PicId)
                VALUES  (@set_id, @pic_id)", dbConn);

        bool retVal = true;

        sqlComm.Parameters.AddWithValue("set_id", setId);
        sqlComm.Parameters.AddWithValue("pic_id", picId);

        dbConn.Open();

        try
        {
            sqlComm.ExecuteNonQuery();
        }
        catch
        {
            retVal = false;
        }
        finally
        {
            dbConn.Close();
        }

        return retVal;
    }

    public static bool RemovePictureFromSet(string picId, string setId)
    {
        SqlConnection dbConn = new SqlConnection(connectionString);

        SqlCommand sqlComm = new SqlCommand(
            @"DELETE FROM site_Picture_in_Set
                WHERE PicId = @pic_id AND SetId = @set_id", dbConn);

        bool retVal = true;

        sqlComm.Parameters.AddWithValue("set_id", setId);
        sqlComm.Parameters.AddWithValue("pic_id", picId);

        dbConn.Open();

        try
        {
            sqlComm.ExecuteNonQuery();
        }
        catch
        {
            retVal = false;
        }
        finally
        {
            dbConn.Close();
        }

        return retVal;
    }

    public static bool RemovePictureFromSet_1(string picId, string setId)
    {
        SqlConnection dbConn = new SqlConnection(connectionString);

        SqlCommand sqlComm = new SqlCommand(
            @"DELETE FROM site_Picture_in_Set
                WHERE PicId = @pic_id AND SetId = @set_id", dbConn);

        bool retVal = true;

        sqlComm.Parameters.AddWithValue("set_id", setId);
        sqlComm.Parameters.AddWithValue("pic_id", picId);

        dbConn.Open();

        try
        {
            sqlComm.ExecuteNonQuery();
        }
        catch
        {
        }

        sqlComm.CommandText = @"
                SELECT COUNT(*)
                FROM site_Picture_in_Set
                WHERE PicId = @pic_id";

        int nb = int.Parse(sqlComm.ExecuteScalar().ToString());

        if (nb > 0)
            retVal = false;

        dbConn.Close();

        return retVal;
    }
}
