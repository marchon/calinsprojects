using System;
using System.Collections;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Text;
using System.Windows.Forms;
using System.IO;

namespace MapEditor2
{
    public delegate void MapSaveStateChangedHandler(bool isSaved);

    class ObjectType
    {
    public ObjectType(Image img, UInt16 h)
    {
        firstFrame = img;
        height = h;
    }

    public ObjectType()
    {
        firstFrame = null;
        height = 0;
    }

    public Image firstFrame;
    public UInt16 height;
    }  

    class ObjectInstance
    {
        public ObjectInstance(UInt16 id, UInt16 x, UInt16 y)
        {
            this.Id = id;
            this.X = x;
            this.Y = y;
        }

        public UInt16 Id;
        public UInt16 X;
        public UInt16 Y;
    }

    class Coord
    {
        public Coord(UInt16 x, UInt16 y)
        {
            this.X = x;
            this.Y = y;
        }

        public UInt16 X;
        public UInt16 Y;
    }

    class Map : ScrollableControl
    {
        /* PARAMETERS */
        private UInt16 tileWidth = 0;
        private UInt16 tileHeight = 0;
        private UInt16 mapWidth = 0;
        private UInt16 mapHeight = 0;

        private Image tileBitmap = null;
        private ArrayList objectTypes = null;

        private UInt16[] mapOffsets = null;
        private ArrayList mapObjects = new ArrayList();
        private ArrayList startCoordinates = new ArrayList();

        private TileContainer tileContainer = null;
        private ObjectContainer objectContainer = null;
        /* PARAMETERS */

        /* PARAMETER FLAG */
        private bool parametersSet = false;
        /* PARAMETER FLAG */

        /* INDICATORS FOR SELECTION IN CURRENT LAYER */
        /* ID OF CURRENT TILE */
        private int selectedTileId = 0;
        /* ID OF CURRENT TILE */

        /* COORDINATES FOR DRAWING AND INVALIDATEING */
        private int pointerHintX = 0;
        private int pointerHintY = 0;
        private int pointerHintWidth = 0;
        private int pointerHintHeight = 0;

        private const int coordinatesIndicatorDim = 60;
        /* COORDINATES FOR DRAWING AND INVALIDATEING */

        /* FLAG INDICATING INDICATOR VISIBILITY */
        private bool drawPointerHint = false;
        /* FLAG INDICATING INDICATOR VISIBILITY */
        /* INDICATORS FOR SELECTION IN CURRENT LAYER */

        /* FOR DIRTY RECTANGLE INVALIDATING PURPOSES */
        private Rectangle invalidateRect_1 = new Rectangle();
        private Rectangle invalidateRect_2 = new Rectangle();
        /* FOR DIRTY RECTANGLE INVALIDATING PURPOSES */

        /* FLAG INDICATING GRID VISIBILITY */
        private bool showGrid = false;
        /* FLAG INDICATING GRID VISIBILITY */

        /* FLAG INDICATING WETHER OBJECTS SNAP TO GRID */
        private bool snapToGrid = false;
        /* FLAG INDICATING WETHER OBJECTS SNAP TO GRID */

        /* FLAG INDICATING SAVE STATE */
        private bool isSaved = false;
        /* FLAG INDICATING SAVE STATE */

        /* CURRENT LAYER INDICATOR */
        private int currentLayer = 0;
        /* CURRENT LAYER INDICATOR */

        /* PICK UP MODE FOR OBJECTS */
        private bool pickUpMode = false;
        /* PICK UP MODE FOR OBJECTS */

        /* PICKED UP OBJECT IN PICK UP MODE */
        private Object pickedUpObject = null;
        private int pickedUpObjectIndex = -1;
        /* PICKED UP OBJECT IN PICK UP MODE */

        /* PROPERTIES FOR THE ABOVE FIELDS */
        public UInt16 TileWidth
        {
            get
            {
                return tileWidth;
            }
        }
        public UInt16 TileHeight
        {
            get
            {
                return tileHeight;
            }
        }
        public UInt16 MapWidth
        {
            get
            {
                return mapWidth;
            }
        }
        public UInt16 MapHeight
        {
            get
            {
                return mapHeight;
            }
        }

        public bool ShowGrid
        {
            set
            {
                showGrid = value;
                Invalidate();
            }
        }

        public bool SnapToGrid
        {
            set
            {
                snapToGrid = value;

                if (snapToGrid)
                {
                    pointerHintX = (pointerHintX / tileWidth) * tileWidth;
                    pointerHintY = (pointerHintY / tileHeight) * tileHeight;

                    InvalidateEraseDrawRegion(pointerHintX - pointerHintWidth / 2, pointerHintY - pointerHintHeight / 2, pointerHintWidth, pointerHintHeight);
                }
            }
        }

        public bool IsSaved
        {
            get
            {
                return isSaved;
            }
            set
            {
                isSaved = value;
                if (mapSaveStateChangedHandler != null)
                    mapSaveStateChangedHandler(isSaved);
            }
        }

        public int CurrentLayer
        {
            set
            {
                if (value >= 0 && value < 3)
                {
                    currentLayer = value;

                    if (pickedUpObject != null)
                        pickedUpObject = null;

                    Invalidate();
                }
            }
        }

        public bool PickUpMode
        {
            set
            {
                pickUpMode = value;

                if (pickedUpObject != null)
                    pickedUpObject = null;

                if (pickUpMode == false)
                {
                    if (objectTypes.Count > 0)
                    {
                        pointerHintWidth = ((ObjectType)objectTypes[objectContainer.SelectedObject]).firstFrame.Width;
                        pointerHintHeight = ((ObjectType)objectTypes[objectContainer.SelectedObject]).firstFrame.Height;
                    }
                }

                InvalidateEraseDrawRegion(pointerHintX - pointerHintWidth / 2, pointerHintY - pointerHintHeight / 2, pointerHintWidth, pointerHintHeight);
            }
        }
        /* PROPERTIES FOR THE ABOVE FIELDS */

        /* SAVE STATE CHANGE HANDLER */
        public MapSaveStateChangedHandler mapSaveStateChangedHandler = null;
        /* SAVE STATE CHANGE HANDLER */

        ProgressBar pb = new ProgressBar();

        /* CONSTRUCTOR */
        public Map()
        {
            this.SetStyle(
            ControlStyles.UserPaint |
            ControlStyles.AllPaintingInWmPaint |
            ControlStyles.OptimizedDoubleBuffer, true);

            this.Cursor = System.Windows.Forms.Cursors.Hand;

            this.Controls.Add(pb);
            pb.Width = 350;
            pb.Hide();
        }

        /* CONSTRUCTOR */

        protected override void OnSizeChanged(EventArgs e)
        {
            base.OnSizeChanged(e);

            pb.Location = new Point(this.Width / 2 - pb.Width / 2, this.Height / 2 - pb.Height / 2);
        }

        /* DISPOSING METHOD */
        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                if (mapOffsets != null)
                    mapOffsets = null;

                mapObjects = null;
                startCoordinates = null;
            }

            base.Dispose(disposing);
        }
        /* DISPOSING METHOD */

        /* HANDLERS */
        /* PAINT HANDLER */
        protected override void OnPaint(PaintEventArgs pe)
        {
            if (!parametersSet)
                return;

            /* TRABSLATE TO SCROLL POSITION */
            pe.Graphics.TranslateTransform(AutoScrollPosition.X, AutoScrollPosition.Y);
            /* TRABSLATE TO SCROLL POSITION */

            int sx = -AutoScrollPosition.X / tileWidth;
            int sy = -AutoScrollPosition.Y / tileHeight;
            int ex = sx + this.Width / tileWidth + 2;
            int ey = sy + this.Height / tileHeight + 2;

            if (ex > mapWidth)
                ex = mapWidth;
            if (ey > mapHeight)
                ey = mapHeight;

            Rectangle destRect = new Rectangle();

            for (int j = sy; j < ey; j++)
                for (int i = sx; i < ex; i++)
                {
                    if (j * mapWidth + i < mapOffsets.Length)
                    {
                        int a = (mapOffsets[i + j * mapWidth] * tileWidth) % tileBitmap.Width;
                        int b = ((mapOffsets[i + j * mapWidth] * tileWidth) / tileBitmap.Width) * tileHeight;

                        destRect.X = i * tileWidth;
                        destRect.Y = j * tileHeight;
                        destRect.Width = tileWidth;
                        destRect.Height = tileHeight;

                        pe.Graphics.DrawImage(tileBitmap,
                            destRect, a, b, tileWidth, tileHeight,
                            GraphicsUnit.Pixel);
                    }
                }

            if (showGrid)
            {
                Pen pen = new Pen(Color.Black, 2.0f);

                int x = sx * tileWidth;
                int _x = ex * tileWidth;
                int y = sy * tileHeight;
                int _y = ey * tileHeight;

                int nrCrtX = sx;
                int nrCrtY = sy;

                Font nrCrtFont = new Font(FontFamily.GenericSansSerif, 10);
                Brush nrCrtBrush = Brushes.Black;

                for (int line = x; line <= _x; line += tileWidth)
                {
                    pe.Graphics.DrawLine(pen, line, y, line, _y);

                    string nrCrt = "" + (nrCrtX++);
                    pe.Graphics.DrawString(nrCrt, nrCrtFont, nrCrtBrush, line - nrCrt.Length * 10, 0);

                }

                for (int line = y; line <= _y; line += tileHeight)
                {
                    pe.Graphics.DrawLine(pen, x, line, _x, line);

                    string nrCrt = "" + (nrCrtY++);
                    pe.Graphics.DrawString(nrCrt, nrCrtFont, nrCrtBrush, 0, line - nrCrtFont.Height);
                }
            }

            if (currentLayer > 0)
            {
                for (int i = 0; i < mapObjects.Count; i++)
                {
                    ObjectInstance obInst = (ObjectInstance)mapObjects[i];
                    Image obImag = ((ObjectType)objectTypes[obInst.Id]).firstFrame;

                    pe.Graphics.DrawImage(obImag,
                        new Rectangle(obInst.X - obImag.Width / 2, obInst.Y - obImag.Height / 2, obImag.Width, obImag.Height),
                        0, 0, obImag.Width, obImag.Height,
                        GraphicsUnit.Pixel, Utils.imageAttributes);
                }
            }

            if (currentLayer > 1)
            {
                for (int i = 0; i < startCoordinates.Count; i++)
                {
                    int x = ((Coord)startCoordinates[i]).X;
                    int y = ((Coord)startCoordinates[i]).Y;

                    pe.Graphics.DrawEllipse(new Pen(Color.Green, 2.0f), x - coordinatesIndicatorDim / 2,
                            y - coordinatesIndicatorDim / 2, coordinatesIndicatorDim, coordinatesIndicatorDim);
                    pe.Graphics.DrawLine(new Pen(Color.Green, 2.0f), x - coordinatesIndicatorDim / 2, y,
                        x + coordinatesIndicatorDim / 2, y);
                    pe.Graphics.DrawLine(new Pen(Color.Green, 2.0f), x, y - coordinatesIndicatorDim / 2,
                        x, y + coordinatesIndicatorDim / 2);
                }

            }

            if (drawPointerHint)
            {
                if (currentLayer == 0)
                {
                    pe.Graphics.DrawRectangle(new Pen(Color.Green, 2.0f),
                        pointerHintX, pointerHintY, tileWidth, tileHeight);
                }
                else if(currentLayer == 1 && objectTypes.Count > 0)
                {
                    if (!pickUpMode || pickedUpObject != null)
                    {
                        int id;

                        if (!pickUpMode)
                            id = objectContainer.SelectedObject;
                        else
                            id = ((ObjectInstance)pickedUpObject).Id;

                        pe.Graphics.DrawImage(((ObjectType)objectTypes[id]).firstFrame,
                            new Rectangle(pointerHintX - pointerHintWidth / 2, pointerHintY - pointerHintHeight / 2,
                            pointerHintWidth, pointerHintHeight),
                            0, 0, pointerHintWidth, pointerHintHeight,
                            GraphicsUnit.Pixel, Utils.imageAttributes);
                    }      
                }
                else if (currentLayer == 2)
                {
                    if (!pickUpMode || pickedUpObject != null)
                    {
                        pe.Graphics.DrawEllipse(new Pen(Color.Green, 2.0f), pointerHintX - coordinatesIndicatorDim / 2,
                            pointerHintY - coordinatesIndicatorDim / 2,
                            coordinatesIndicatorDim, coordinatesIndicatorDim);
                        pe.Graphics.DrawLine(new Pen(Color.Green, 2.0f), pointerHintX - coordinatesIndicatorDim / 2, pointerHintY,
                            pointerHintX + coordinatesIndicatorDim / 2, pointerHintY);
                        pe.Graphics.DrawLine(new Pen(Color.Green, 2.0f), pointerHintX, pointerHintY - coordinatesIndicatorDim / 2,
                            pointerHintX, pointerHintY + coordinatesIndicatorDim / 2);
                    }
                }
            }
        }
        /* PAINT HANDLER */

        /* BACKGROUND PAINT */
        protected override void OnPaintBackground(PaintEventArgs pevent)
        {
            //Don't allow the background to paint 
        }
        /* BACKGROUND PAINT */

        /* CLICK HANDLER */
        protected override void OnMouseClick(MouseEventArgs e)
        {
            if (parametersSet && e.Button == MouseButtons.Left)
            {
                if (currentLayer == 0)
                {
                    mapOffsets[selectedTileId] = (UInt16)tileContainer.SelectedTile;

                    InvalidateEraseDrawRegion(pointerHintX, pointerHintY, tileWidth, tileHeight);
                }
                else if (currentLayer == 1 && objectTypes.Count > 0)
                {
                    if (!pickUpMode)
                    {
                        if (mapObjects.Count < Utils.MAX_OBJECTS_ON_MAP)
                        {
                            int insertIndex = 0;

                            while (insertIndex < mapObjects.Count && ((ObjectType)objectTypes[(((ObjectInstance)mapObjects[insertIndex]).Id)]).height <=
                                ((ObjectType)objectTypes[objectContainer.SelectedObject]).height)
                                insertIndex++;

                            mapObjects.Insert(insertIndex, new ObjectInstance((UInt16)objectContainer.SelectedObject, (UInt16)pointerHintX, (UInt16)pointerHintY));
                        }
                        else
                            MessageBox.Show(Utils.MAX_OBJECTS_ERROR, Utils.APP_NAME, MessageBoxButtons.OK, MessageBoxIcon.Exclamation);
                    }
                    else
                    {
                        if (pickedUpObject == null)
                        {
                            Point mouseLocation = new Point();

                            mouseLocation.X = e.X - AutoScrollPosition.X;
                            mouseLocation.Y = e.Y - AutoScrollPosition.Y;

                            for (int i = mapObjects.Count - 1; i >= 0; i--)
                            {
                                ObjectInstance obi = (ObjectInstance)mapObjects[i];
                                pointerHintWidth = ((ObjectType)objectTypes[obi.Id]).firstFrame.Width;
                                pointerHintHeight = ((ObjectType)objectTypes[obi.Id]).firstFrame.Height;

                                if (obi.X - pointerHintWidth / 2 <= mouseLocation.X && mouseLocation.X <= obi.X + pointerHintWidth / 2 &&
                                    obi.Y - pointerHintHeight / 2 <= mouseLocation.Y && mouseLocation.Y <= obi.Y + pointerHintHeight / 2)
                                {
                                    pickedUpObject = obi;
                                    mapObjects.RemoveAt(i);

                                    pickedUpObjectIndex = i;

                                    Invalidate(new Rectangle(obi.X - pointerHintWidth / 2 + AutoScrollPosition.X,
                                        obi.Y - pointerHintHeight / 2 + AutoScrollPosition.Y,
                                        pointerHintWidth, pointerHintHeight));

                                    InvalidateEraseDrawRegion(pointerHintX - pointerHintWidth / 2, pointerHintY - pointerHintHeight / 2,
                                        pointerHintWidth, pointerHintHeight);

                                    break;
                                }
                            }

                        }
                        else
                        {
                            ((ObjectInstance)pickedUpObject).X = (UInt16)pointerHintX;
                            ((ObjectInstance)pickedUpObject).Y = (UInt16)pointerHintY;
                            mapObjects.Insert(pickedUpObjectIndex, pickedUpObject);
                            pickedUpObject = null;

                            InvalidateEraseDrawRegion(pointerHintX - pointerHintWidth / 2, pointerHintY - pointerHintHeight / 2,
                                        pointerHintWidth, pointerHintHeight);
                        }
                    }
                }
                else if (currentLayer == 2)
                {
                    if (!pickUpMode)
                    {
                        if (startCoordinates.Count < Utils.MAX_VEHICLES)
                            startCoordinates.Add(new Coord((UInt16)pointerHintX, (UInt16)pointerHintY));
                        else
                            MessageBox.Show(Utils.MAX_VEHICLES_ERROR, Utils.APP_NAME, MessageBoxButtons.OK, MessageBoxIcon.Exclamation);
                    }

                    else
                    {
                        if (pickedUpObject == null)
                        {
                            Point mouseLocation = new Point();

                            mouseLocation.X = e.X - AutoScrollPosition.X;
                            mouseLocation.Y = e.Y - AutoScrollPosition.Y;

                            for (int i = startCoordinates.Count - 1; i >= 0; i--)
                            {
                                Coord obi = (Coord)startCoordinates[i];
                                pointerHintWidth = pointerHintHeight = coordinatesIndicatorDim;

                                if (obi.X - pointerHintWidth / 2 <= mouseLocation.X && mouseLocation.X <= obi.X + pointerHintWidth / 2 &&
                                    obi.Y - pointerHintHeight / 2 <= mouseLocation.Y && mouseLocation.Y <= obi.Y + pointerHintHeight / 2)
                                {
                                    pickedUpObject = obi;
                                    startCoordinates.RemoveAt(i);

                                    Invalidate(new Rectangle(obi.X - pointerHintWidth / 2 + AutoScrollPosition.X - 2,
                                        obi.Y - pointerHintHeight / 2 + AutoScrollPosition.Y - 2,
                                        pointerHintWidth + 4, pointerHintHeight + 4));

                                    InvalidateEraseDrawRegion(pointerHintX - pointerHintWidth / 2, pointerHintY - pointerHintHeight / 2,
                                        pointerHintWidth, pointerHintHeight);

                                    break;
                                }
                            }
                        }
                        else
                        {
                            ((Coord)pickedUpObject).X = (UInt16)pointerHintX;
                            ((Coord)pickedUpObject).Y = (UInt16)pointerHintY;
                            startCoordinates.Add(pickedUpObject);
                            pickedUpObject = null;

                            InvalidateEraseDrawRegion(pointerHintX - pointerHintWidth / 2, pointerHintY - pointerHintHeight / 2,
                                        pointerHintWidth, pointerHintHeight);
                        }
                    }
                }   

                if (IsSaved == true)
                    IsSaved = false;
            }

            else if (parametersSet && e.Button == MouseButtons.Right && pickedUpObject != null)
            {
                pickedUpObject = null;
                InvalidateEraseDrawRegion(pointerHintX - pointerHintWidth / 2, pointerHintY - pointerHintHeight / 2,
                                        pointerHintWidth, pointerHintHeight);
            }
        }
        /* CLICK HANDLER */

        /* NOUSE ENTER HANDLER */
        protected override void OnMouseEnter(EventArgs e)
        {
            if (parametersSet)
            {
                drawPointerHint = true;
                InvalidateEraseDrawRegion();
            }
        }
        /* NOUSE ENTER HANDLER */

        /* MOUSE OUT HANDLER */
        protected override void OnMouseLeave(EventArgs e)
        {
            if (parametersSet)
            {
                drawPointerHint = false;
                InvalidateEraseDrawRegion();
            }
        }
        /* MOUSE OUT HANDLER */

        /* MOUSE MOVE HANDLER */
        protected override void OnMouseMove(MouseEventArgs e)
        {
            if (!parametersSet)
                return;

            /*if (!drawPointerHint)
                drawPointerHint = true;*/
            
            Point mouseLocation = new Point();

            mouseLocation.X = e.X - AutoScrollPosition.X;
            mouseLocation.Y = e.Y - AutoScrollPosition.Y;

            if (currentLayer == 0)
            {
                bool sectorChanged = false;

                if (mouseLocation.X - pointerHintX >= tileWidth || pointerHintX - mouseLocation.X >= 1)
                {
                    pointerHintX = (mouseLocation.X / tileWidth) * tileWidth;
                    sectorChanged = true;
                }

                if (mouseLocation.Y - pointerHintY >= tileHeight || pointerHintY - mouseLocation.Y >= 1)
                {
                    pointerHintY = (mouseLocation.Y / tileHeight) * tileHeight;
                    sectorChanged = true;
                }

                if (sectorChanged)
                {
                    if (e.Button == MouseButtons.Left)
                    {
                        mapOffsets[selectedTileId] = (UInt16)tileContainer.SelectedTile;
                    }

                    selectedTileId = (pointerHintY / tileHeight) * mapWidth + pointerHintX / tileWidth;

                    InvalidateEraseDrawRegion(pointerHintX, pointerHintY, tileWidth, tileHeight);
                }
            }
            else if ((currentLayer == 1 && objectTypes.Count > 0) || currentLayer == 2)
            {
                if (!snapToGrid || (mouseLocation.X - pointerHintX >= tileWidth / 2 || pointerHintX - mouseLocation.X >= tileWidth / 2 ||
                    mouseLocation.Y - pointerHintY >= tileHeight / 2 || pointerHintY - mouseLocation.Y >= tileHeight / 2))
                {
                    if (currentLayer == 1 && pickedUpObject == null)
                    {
                        pointerHintWidth = ((ObjectType)objectTypes[objectContainer.SelectedObject]).firstFrame.Width;
                        pointerHintHeight = ((ObjectType)objectTypes[objectContainer.SelectedObject]).firstFrame.Height;
                    }
                    else if (currentLayer == 2)
                        pointerHintWidth = pointerHintHeight = coordinatesIndicatorDim;

                    if (!snapToGrid)
                    {
                        pointerHintX = mouseLocation.X;
                        pointerHintY = mouseLocation.Y;
                    }
                    else
                    {
                        if (mouseLocation.X - pointerHintX >= tileWidth / 2)
                            pointerHintX = (mouseLocation.X / tileWidth) * tileWidth + tileWidth;                          
                        if (pointerHintX - mouseLocation.X >= tileWidth / 2)
                            pointerHintX = (mouseLocation.X / tileWidth) * tileWidth;
                            
                        if (mouseLocation.Y - pointerHintY >= tileHeight / 2)
                            pointerHintY = (mouseLocation.Y / tileHeight) * tileHeight + tileHeight;
                        if (pointerHintY - mouseLocation.Y >= tileHeight / 2)
                            pointerHintY = (mouseLocation.Y / tileHeight) * tileHeight;               
                    }

                    InvalidateEraseDrawRegion(pointerHintX - pointerHintWidth / 2, pointerHintY - pointerHintHeight / 2, 
                        pointerHintWidth, pointerHintHeight);
                }        
            }
        }
        /* MOUSE MOVE HANDLER */
        /* HANDLERS */

        /* PARAMETERS HANDELING */
        public void SetParameters(UInt16 tw, UInt16 th, UInt16 mw, UInt16 mh, UInt16 bt, Image tb, TileContainer tc, ArrayList mo, ObjectContainer oc)
        {

            if (tb == null || tc == null || parametersSet)
                throw (new Exception("Parameters error!"));

            if (tb.Width % tw != 0 || tb.Height % th != 0)
            {
                throw (new Exception("Parsing bitmap error!"));
            }

            tileWidth = tw;
            tileHeight = th;
            mapWidth = mw;
            mapHeight = mh;

            mapOffsets = new UInt16[mapWidth * mapHeight];

            for (int i = 0; i < mapOffsets.Length; i++)
                mapOffsets[i] = (UInt16)((((i / mapWidth) % bt) * bt) + ((i % mapWidth) % bt));

            mapObjects.Clear();

            this.AutoScrollMinSize = new Size(tileWidth * mapWidth, tileHeight * mapHeight);

            tileBitmap = tb;
            tileContainer = tc;

            objectTypes = mo;
            objectContainer = oc;

            parametersSet = true;

            selectedTileId = 0;
            pointerHintX = 0;
            pointerHintY = 0;

            IsSaved = false;

            this.Invalidate();

        }

        public void SetParametersFromFile(FileStream fileStream, Image tb, TileContainer tc, ArrayList mo, ObjectContainer oc)
        {
            if (fileStream == null || tb == null || tc == null || parametersSet)
                throw (new Exception("Parameters error!"));

            tileBitmap = tb;
            tileContainer = tc;

            objectTypes = mo;
            objectContainer = oc;

            try
            {
                UInt16 count = 0;

                try
                {
                    count = Utils.readUint16LittleEndian(fileStream);
                }
                catch (Exception)
                {
                    count = 0;
                }

                startCoordinates.Clear();

                for (int i = 0; i < count; i++)
                    startCoordinates.Add(new Coord(Utils.readUint16LittleEndian(fileStream), Utils.readUint16LittleEndian(fileStream)));

                tileWidth = Utils.readUint16LittleEndian(fileStream);
                tileHeight = Utils.readUint16LittleEndian(fileStream);
                mapWidth = Utils.readUint16LittleEndian(fileStream);
                mapHeight = Utils.readUint16LittleEndian(fileStream);

                mapOffsets = new UInt16[mapWidth * mapHeight];

                for (int i = 0; i < mapOffsets.Length; i++)
                    mapOffsets[i] = Utils.readUint16LittleEndian(fileStream);

                try
                {
                    count = Utils.readUint16LittleEndian(fileStream);
                }
                catch (Exception)
                {
                    count = 0;
                }

                mapObjects.Clear();
                for (int i = 0; i < count; i++)
                {
                    UInt16 id = Utils.readUint16LittleEndian(fileStream);

                    int insertIndex = 0;

                    while (insertIndex < mapObjects.Count && ((ObjectType)objectTypes[(((ObjectInstance)mapObjects[insertIndex]).Id)]).height <=
                        ((ObjectType)objectTypes[id]).height)
                        insertIndex++;

                    mapObjects.Insert(insertIndex, new ObjectInstance(id,
                        Utils.readUint16LittleEndian(fileStream),
                        Utils.readUint16LittleEndian(fileStream)));
                }
            }
            catch (Exception)
            {
                throw (new Exception("Error reading file!"));
            }

            this.AutoScrollMinSize = new Size(tileWidth * mapWidth, tileHeight * mapHeight);

            parametersSet = true;

            selectedTileId = 0;
            pointerHintX = 0;
            pointerHintY = 0;

            IsSaved = true;         
        }

        public void ResetParameters()
        {
            if (!parametersSet)
                return;

            tileWidth = 0;
            tileHeight = 0;
            mapWidth = 0;
            mapHeight = 0;

            mapOffsets = null;

            mapObjects.Clear();
            startCoordinates.Clear();

            tileBitmap = null;
            tileContainer = null;

            objectTypes = null;
            objectContainer = null;

            this.AutoScrollPosition = new Point(0, 0);

            parametersSet = false;
        }
        /* PARAMETERS HANDELING */

        /* MAP STRUCTURE SAVEING */
        public void SaveMap(FileStream fileStream)
        {
            if (fileStream == null)
                return;

            Utils.writeUint16LittleEndian(fileStream, (UInt16)(startCoordinates.Count));
            for (int i = 0; i < startCoordinates.Count; i++)
            {
                Utils.writeUint16LittleEndian(fileStream, ((Coord)startCoordinates[i]).X);
                Utils.writeUint16LittleEndian(fileStream, ((Coord)startCoordinates[i]).Y);
            }

            Utils.writeUint16LittleEndian(fileStream, tileWidth);
            Utils.writeUint16LittleEndian(fileStream, tileHeight);
            Utils.writeUint16LittleEndian(fileStream, mapWidth);
            Utils.writeUint16LittleEndian(fileStream, mapHeight);

            for (int i = 0; i < mapOffsets.Length; i++)
                Utils.writeUint16LittleEndian(fileStream, mapOffsets[i]);

            Utils.writeUint16LittleEndian(fileStream, (UInt16)(mapObjects.Count));
            for (int i = 0; i < mapObjects.Count; i++)
            {
                Utils.writeUint16LittleEndian(fileStream, ((ObjectInstance)mapObjects[i]).Id);
                Utils.writeUint16LittleEndian(fileStream, ((ObjectInstance)mapObjects[i]).X);
                Utils.writeUint16LittleEndian(fileStream, ((ObjectInstance)mapObjects[i]).Y);
            }

            IsSaved = true;
        }
        /* MAP STRUCTURE SAVEING */

        /* EXPORT MINIMAP */
        public void ExportMinimap(String path, int dimX, int dimY)
        {

            pb.Show();

            pb.Minimum = 1;
            pb.Maximum = mapHeight + mapObjects.Count + 2;
            pb.Value = 1;
            pb.Step = 1;

            

            Bitmap bmp = new Bitmap(tileWidth * mapWidth, tileHeight * mapHeight);
            Graphics gr = Graphics.FromImage(bmp);

            Rectangle destRect = new Rectangle();

            for (int j = 0; j < mapHeight; j++)
            {
                for (int i = 0; i < mapWidth; i++)
                {
                    if (j * mapWidth + i < mapOffsets.Length)
                    {
                        int a = (mapOffsets[i + j * mapWidth] * tileWidth) % tileBitmap.Width;
                        int b = ((mapOffsets[i + j * mapWidth] * tileWidth) / tileBitmap.Width) * tileHeight;

                        destRect.X = i * tileWidth;
                        destRect.Y = j * tileHeight;
                        destRect.Width = tileWidth;
                        destRect.Height = tileHeight;

                        gr.DrawImage(tileBitmap,
                            destRect, a, b, tileWidth, tileHeight,
                            GraphicsUnit.Pixel);
                    }                   
                }
                pb.PerformStep();
            }

            for (int i = 0; i < mapObjects.Count; i++)
            {
            ObjectInstance obInst = (ObjectInstance)mapObjects[i];
            Image obImag = ((ObjectType)objectTypes[obInst.Id]).firstFrame;

            gr.DrawImage(obImag,
            new Rectangle(obInst.X - obImag.Width / 2, obInst.Y - obImag.Height / 2, obImag.Width, obImag.Height),
            0, 0, obImag.Width, obImag.Height,
            GraphicsUnit.Pixel, Utils.imageAttributes);

            pb.PerformStep();
            }

            Bitmap minimap = new Bitmap(bmp, dimX, dimY);
            pb.PerformStep();
            minimap.Save(path);
            pb.PerformStep();

            minimap.Dispose();
            bmp.Dispose();
            minimap = null;
            bmp = null;

            System.GC.Collect();

            pb.Hide();
        }
        /* EXPORT MINIMAP */

        /* USEFULL METHODS FOR DIRTY RECTANGLE INVALIDATION */
        void InvalidateEraseDrawRegion(int x, int y, int w, int h)
        {
            invalidateRect_1 = invalidateRect_2;

            invalidateRect_2.X = x + AutoScrollPosition.X - 2;
            invalidateRect_2.Y = y + AutoScrollPosition.Y - 2;
            invalidateRect_2.Width = w + 4;
            invalidateRect_2.Height = h + 4;

            NormalizeRectangle(ref invalidateRect_2);

            Invalidate(invalidateRect_1);
            Invalidate(invalidateRect_2);
        }

        void InvalidateEraseDrawRegion()
        {
            Invalidate(invalidateRect_2);
        }

        void NormalizeRectangle(ref Rectangle rectangle)
        {           
            if (rectangle.X < 0)
                rectangle.X = 0;
            if (rectangle.X > this.Width)
            {
                rectangle.X = this.Width;
                rectangle.Width = 0;
            }
            if (rectangle.Width > this.Width - rectangle.X)
                rectangle.Width = this.Width - rectangle.X;

            if (rectangle.Y < 0)
                rectangle.Y = 0;
            if (rectangle.Y > this.Height)
            {
                rectangle.Y = this.Height;
                rectangle.Height = 0;
            }
            if (rectangle.Height > this.Height - rectangle.Y)
                rectangle.Height = this.Height - rectangle.Y;           
        }
        /* USEFULL METHODS FOR DIRTY RECTANGLE INVALIDATION */
    }
}
