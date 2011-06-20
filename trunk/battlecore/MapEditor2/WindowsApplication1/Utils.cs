using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Drawing.Imaging;
using System.Drawing;
using System.Windows.Forms;

namespace MapEditor2
{
    class Utils
    {
        /* EXTENSIONS */
        public const string BITMAP_EXTENSION = ".bmp";
        public const string BINARY_MAP_EXTENSION = ".map";
        /* EXTENSIONS */

        /* PATHS */
        public static string CURRENT_FOLDER = null;
        public const string SEPARATOR = @"\";        
        public const string MAP_FOLDER = @"\Maps\";
        public const string VEHICLE_FOLDER = @"Vehicles\";
        public const string VEHICLE_PREVIEW = @"\Preview.bmp";
        public const string OBJECT_FOLDER = @"Maps\MapObjects\";
        public const string FRAMES_FOLDER = @"\Frames\";
        public const string OBJECT_INFO_PATH = @"\object.info";
        public const string VEHICLE_INFO_PATH = @"\vehicle.info";
        public const string FIRST_FRAME = @"0.bmp";

        public const string OBJECT_FOLDER_NAME = "MapObjects";

        public const string TEMP_FOLDER = @"\Temp\";
        /* PATHS */

        /* LIMITS */
        public const UInt16 MAX_TILE_DIM = 128;
        public const UInt16 MIN_TILE_DIM = 32;

        public const UInt16 MAX_MAP_DIM = 500;
        public const UInt16 MIN_MAP_DIM = 10;

        public const UInt16 OBJECT_ATTR_NB = 6;
        public const UInt16 MAX_OBJECTS_ON_MAP = 512;
        public const UInt16 MAX_VEHICLES = 64;

        public const UInt16 VEHICLE_DIRECTIONS = 16;
        /* LIMITS */

        /* TEXTS */
        public const string APP_NAME = "MapEditor II";
        public const string APP_VERSION = "Version: 1.0.0";
        public const string COPYRIGHT = "Copyright 2007 Calin";

        public const string HINTS = "You are using Map Editor II!\n" + 
            "This softwere is especialy designed to help manage resources\nfor the game BattleCore.\n\n" + 
            "Map Editor comes with three different capabilities:\n" + 
            "1: It can create or edit map objects.\n" + 
            "2: It can create or edit vehicles.\n" + 
            "3: It can create or edit maps.\n\n" + 
            "The Object Editor and the Vehicle Editor can be found under Tools section in the aplication's menu.\n" + 
            "In order to be able to place objects on the map you first have to create them with the Object Editor.\n\n" + 
            "For aditional information, please do not hesitate to contect me.\n" + 
            "Cheers,\nCalin";

        /* ERRORS / WARNINGS */
        public const string ERROR_CAPTION = "Error...";
        public const string MAP_EXISTS_ERROR = "A map with this name already exists!\nPlease change the map's name.";
        public static string TILE_WIDTH_ERROR = "Tile width must be between " + MIN_TILE_DIM + " and " + MAX_TILE_DIM + "!";
        public static string TILE_HEIGHT_ERROR = "Tile height must be between " + MIN_TILE_DIM + " and " + MAX_TILE_DIM + "!";
        public static string MAP_WIDTH_ERROR = "Map width must be between " + MIN_MAP_DIM + " and " + MAX_MAP_DIM + "!";
        public static string MAP_HEIGHT_ERROR = "Map height must be between " + MIN_MAP_DIM + " and " + MAX_MAP_DIM + "!";
        public const string BASE_TILES_ERROR = "Base tiles number must be non-zero and\nmust be less then the total number of tiles!";

        public const string TILE_WIDTH_DIVISION_ERROR = "The bitmap width must be a multiple of tile width!";
        public const string TILE_HEIGHT_DIVISION_ERROR = "The bitmap height must be a multiple of tile height!";

        public const string NO_EXISTING_MAP = "There is no existing map.\nPlease create a new map.";

        public static string MAX_OBJECTS_ERROR = "You reached the maximum number of objects: " + MAX_OBJECTS_ON_MAP + ".";
        public static string MAX_VEHICLES_ERROR = "You reached the maximum number of vehicles: " + MAX_VEHICLES + ".";

        public const string ERROR_CREATING_MAP = "Error creating map!";
        public const string ERROR_READING_MAP = "Error reading map!";

        public const string CHANGE_PROPERTIES_CAUTION = "Caution! Changing map properties will result in work loss!";

        public const string ERROR_RENAMING = "Error renaming folder!";
        /* ERRORS / WARNINGS */

        public const string MAP_DESCRIPTION_HINT = "Click to write description...";

        private const string SAVE_PROMPT_1 = "Save changes to\n";
        private const string SAVE_PROMPT_2 = " before closing?";
        public static string getSavePrompt(string name)
        {
            return (SAVE_PROMPT_1 + "\"" + name + "\"" + SAVE_PROMPT_2);
        }
        /* TEXTS */

        /* LAYERS */
        public const int TERRAIN_LAYER = 0;
        public const int OBJECT_LAYER = 1;
        public const int START_COORDS_LAYER = 2;

        public const string TILES = "Tiles";
        public const string OBJECTS = "Objects";
        /* LAYERS */

        /* FILE IO */
        public static void writeUint16LittleEndian(FileStream fileStream, UInt16 value)
        {
            fileStream.WriteByte((byte)value);
            value >>= 8;
            fileStream.WriteByte((byte)value);
        }

        public static void writeUint32LittleEndian(FileStream fileStream, UInt32 value)
        {
            fileStream.WriteByte((byte)value);
            value >>= 8;
            fileStream.WriteByte((byte)value);
            value >>= 8;
            fileStream.WriteByte((byte)value);
            value >>= 8;
            fileStream.WriteByte((byte)value);
        }

        public static UInt16 readUint16LittleEndian(FileStream fileStream)
        {
            return (UInt16)((UInt16)fileStream.ReadByte() | (((UInt16)fileStream.ReadByte()) << 8));
        }

        /* FILE IO */

        /* FOR TRANSPARENCY */
        public static ImageAttributes imageAttributes = new ImageAttributes();
        /* FOR TRANSPARENCY */

        public static void stretchImageToPictureBox(Image image, PictureBox pictureBox)
        {
            if (image.Width * pictureBox.Height >= image.Height * pictureBox.Width)
            {
                pictureBox.Image = new Bitmap(image,
                   pictureBox.Width,
                   (image.Height * pictureBox.Width) / image.Width);
            }
            else
            {
                pictureBox.Image = new Bitmap(image,
                   (image.Width * pictureBox.Height) / image.Height,
                   pictureBox.Height);
            }

            pictureBox.SizeMode = PictureBoxSizeMode.CenterImage;
        }
    }
}
