using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.IO;
using System.Collections;

namespace MapEditor2
{
    public partial class MainWindow : Form
    {

        private string mapName = null;
        private string bitmapPath = null;

        private UInt16 tileWidth = 0;
        private UInt16 tileHeight = 0;
        private UInt16 mapWidth = 0;
        private UInt16 mapHeight = 0;

        Image tileBitmap = null;
        ArrayList mapObjects = new ArrayList();

        private bool isMapLoaded = false;

        string mapDescription = null;

        public MainWindow()
        {
            InitializeComponent();
        }

        private void MainWindow_Load(object sender, EventArgs e)
        {
            Utils.CURRENT_FOLDER = Directory.GetCurrentDirectory();
            Utils.imageAttributes.SetColorKey(Color.Magenta, Color.Magenta);

            if (!Directory.Exists(Utils.MAP_FOLDER))
                Directory.CreateDirectory(Utils.MAP_FOLDER);

            if (!Directory.Exists(Utils.OBJECT_FOLDER))
                Directory.CreateDirectory(Utils.OBJECT_FOLDER);
    
            if (!Directory.Exists(Utils.VEHICLE_FOLDER))
                Directory.CreateDirectory(Utils.VEHICLE_FOLDER);


            map.mapSaveStateChangedHandler = new MapSaveStateChangedHandler(this.OnMapSaveStateChanged);
        }

        protected override void OnClosing(CancelEventArgs e)
        {
            if (!e.Cancel && !SaveBeforeClosing())
                e.Cancel = true;

            base.OnClosing(e);
        }

        private void OnMapSaveStateChanged(bool saved)
        {
            this.saveToolStripMenuItem.Enabled = !saved;
        }

        private void newToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (SaveBeforeClosing())
                NewMap();
        }

        private void openToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (SaveBeforeClosing())
                OpenMap();
        }

        private void saveToolStripMenuItem_Click(object sender, EventArgs e)
        {
            SaveMap();
        }

        private void closeToolStripMenuItem_Click(object sender, EventArgs e)
        {
            SaveBeforeClosing();
        }

        private void exitToolStripMenuItem_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void undoToolStripMenuItem_Click(object sender, EventArgs e)
        {

        }

        private void redoToolStripMenuItem_Click(object sender, EventArgs e)
        {

        }

        private void changePropertiesToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (MessageBox.Show(Utils.CHANGE_PROPERTIES_CAUTION, Utils.APP_NAME, 
                MessageBoxButtons.OKCancel, MessageBoxIcon.Warning) == DialogResult.OK)
                ChangeMapProperties();
        }  

        private void showGridToolStripMenuItem_Click(object sender, EventArgs e)
        {
            showGrid(showGridToolStripMenuItem.Checked);
        }

        private void terrainToolStripMenuItem_Click(object sender, EventArgs e)
        {
            SetCurrentLayer(Utils.TERRAIN_LAYER);
        }

        private void objectsToolStripMenuItem_Click(object sender, EventArgs e)
        {
            SetCurrentLayer(Utils.OBJECT_LAYER);
        }

        private void startingCoordinatesToolStripMenuItem_Click(object sender, EventArgs e)
        {
            SetCurrentLayer(Utils.START_COORDS_LAYER);
        }

        private void pickUpModeToolStripMenuItem_Click(object sender, EventArgs e)
        {
            pickUpMode(pickUpModeToolStripMenuItem.Checked);
        }  

        private void snapToGridToolStripMenuItem_Click(object sender, EventArgs e)
        {
            snapToGrid(snapToGridToolStripMenuItem.Checked);
        }

        private void exportMinimapToolStripMenuItem_Click(object sender, EventArgs e)
        {
            map.ExportMinimap(Utils.CURRENT_FOLDER + Utils.MAP_FOLDER + mapName + Utils.SEPARATOR + "Minimap.bmp", 250, 250);
        }

        private void objectEditorToolStripMenuItem_Click(object sender, EventArgs e)
        {
            (new ObjectEditor()).ShowDialog();
        }

        private void vehicleEditorToolStripMenuItem_Click(object sender, EventArgs e)
        {
            (new VehicleEditor()).ShowDialog();
        }

        private void hintsToolStripMenuItem_Click(object sender, EventArgs e)
        {
            MessageBox.Show(Utils.HINTS, Utils.APP_NAME, MessageBoxButtons.OK, MessageBoxIcon.Information);
        }

        private void aboutToolStripMenuItem_Click(object sender, EventArgs e)
        {
            MessageBox.Show(Utils.APP_NAME + ". " + Utils.COPYRIGHT + "\n" + Utils.APP_VERSION,
                Utils.APP_NAME, MessageBoxButtons.OK, MessageBoxIcon.Information);
        }

        private void descrLabel_Click(object sender, EventArgs e)
        {
            DescriptionForm descFrm = new DescriptionForm();

            if (mapDescription != null)
                descFrm.descriptionTextBox.Text = mapDescription;

            if (descFrm.ShowDialog() == DialogResult.OK)
            {
                mapDescription = descFrm.descriptionTextBox.Text;
                this.descrLabel.Text = mapDescription;

                this.saveToolStripMenuItem.Enabled = true;
            }
        }

        private void showGridCheckBox_CheckedChanged(object sender, EventArgs e)
        {
            showGrid(showGridCheckBox.Checked);
        }

        private void snapToGridCheckBox_CheckedChanged(object sender, EventArgs e)
        {
            snapToGrid(snapToGridCheckBox.Checked);
        }

        private void pickUpModeCheckBox_CheckedChanged(object sender, EventArgs e)
        {
            pickUpMode(pickUpModeCheckBox.Checked);
        }

        void layerSelectionComboBox_SelectedIndexChanged(object sender, System.EventArgs e)
        {
            SetCurrentLayer(layerSelectionComboBox.SelectedIndex);
        }

        private void showGrid(bool value)
        {
            map.ShowGrid = value;

            if (this.showGridToolStripMenuItem.Checked != value)
                this.showGridToolStripMenuItem.Checked = value;

            if (this.showGridCheckBox.Checked != value)
                this.showGridCheckBox.Checked = value;
        }

        private void snapToGrid(bool value)
        {
            map.SnapToGrid = value;

            if (this.snapToGridToolStripMenuItem.Checked != value)
                this.snapToGridToolStripMenuItem.Checked = value;

            if (this.snapToGridCheckBox.Checked != value)
                this.snapToGridCheckBox.Checked = value;
        }

        private void pickUpMode(bool value)
        {
            map.PickUpMode = value;

            if (this.pickUpModeToolStripMenuItem.Checked != value)
                this.pickUpModeToolStripMenuItem.Checked = value;

            if (this.pickUpModeCheckBox.Checked != value)
                this.pickUpModeCheckBox.Checked = value;
        }

        private void SetCurrentLayer(int layer)
        {
            if (!isMapLoaded)
                return;

            switch (layer)
            {
                case Utils.TERRAIN_LAYER:
                    tileContainer.Show();
                    tileContainer.Focus();

                    objectContainer.Hide();

                    currentLayerGroupBox.Text = Utils.TILES;

                    terrainToolStripMenuItem.Checked = true;
                    objectsToolStripMenuItem.Checked = false;
                    startingCoordinatesToolStripMenuItem.Checked = false;
                    break;
                case Utils.OBJECT_LAYER:
                    tileContainer.Hide();

                    objectContainer.Show();
                    objectContainer.Focus();

                    currentLayerGroupBox.Text = Utils.OBJECTS;

                    terrainToolStripMenuItem.Checked = false;
                    objectsToolStripMenuItem.Checked = true;
                    startingCoordinatesToolStripMenuItem.Checked = false;
                    break;
                case Utils.START_COORDS_LAYER:
                    tileContainer.Hide();
                    objectContainer.Hide();

                    currentLayerGroupBox.Text = "";

                    terrainToolStripMenuItem.Checked = false;
                    objectsToolStripMenuItem.Checked = false;
                    startingCoordinatesToolStripMenuItem.Checked = true;
                    break;
                default:
                    tileContainer.Hide();
                    objectContainer.Hide();

                    currentLayerGroupBox.Text = "";
                    break;
            }

            if (layer >= Utils.TERRAIN_LAYER && layer <= Utils.START_COORDS_LAYER)
            {
                map.CurrentLayer = layer;

                if (layerSelectionComboBox.SelectedIndex != layer)
                    layerSelectionComboBox.SelectedIndex = layer;
            }

            if (layer == Utils.OBJECT_LAYER || layer == Utils.START_COORDS_LAYER)
            {
                objectLayerOptionsToolStripMenuItem.Enabled = true;
                snapToGridToolStripMenuItem.Enabled = true;
                pickUpModeToolStripMenuItem.Enabled = true;

                snapToGridCheckBox.Enabled = true;
                pickUpModeCheckBox.Enabled = true;
            }
            else
            {
                objectLayerOptionsToolStripMenuItem.Enabled = false;
                snapToGridToolStripMenuItem.Enabled = false;
                pickUpModeToolStripMenuItem.Enabled = false;

                snapToGridCheckBox.Enabled = false;
                pickUpModeCheckBox.Enabled = false;
            }
        }

        private void NewMap()
        {
            MapPropertiesDialog mapDialog = new MapPropertiesDialog();

            if (mapDialog.ShowDialog() == DialogResult.OK)
            {
                mapName = mapDialog.MapName;
                bitmapPath = mapDialog.BitmapPath;

                tileWidth = mapDialog.TileWidth;
                tileHeight = mapDialog.TileHeight;
                mapWidth = mapDialog.MapWidth;
                mapHeight = mapDialog.MapHeight;

                twLabel.Text = tileWidth.ToString() + " px";
                thLabel.Text = tileHeight.ToString() + " px";
                mwLabel.Text = mapWidth.ToString() + " x " + tileWidth.ToString() + " px";
                mhLabel.Text = mapHeight.ToString() + " x " + tileHeight.ToString() + " px";
                mnLabel.Text = mapName;

                descrLabel.Text = Utils.MAP_DESCRIPTION_HINT;

                this.Text = mapName + " - " + Utils.APP_NAME;

                tileBitmap = Image.FromFile(bitmapPath);

                LoadMapObjects();

                tileContainer.SetParameters(tileBitmap, tileWidth, tileHeight);
                try
                {
                    map.SetParameters(tileWidth, tileHeight, mapWidth, mapHeight, mapDialog.BaseTiles, tileBitmap, tileContainer, mapObjects, objectContainer);
                }
                catch (Exception)
                {
                    MessageBox.Show(Utils.ERROR_CREATING_MAP, Utils.APP_NAME, MessageBoxButtons.OK, MessageBoxIcon.Error);
                    CloseMap();
                    return;
                }

                EnableAndShowControls();
            }

            mapDialog = null;
        }

        private void OpenMap()
        {
            string[] allDirectories = Directory.GetDirectories(Utils.CURRENT_FOLDER + Utils.MAP_FOLDER);

            if (allDirectories.Length <= 1)
            {
                MessageBox.Show(Utils.NO_EXISTING_MAP, Utils.APP_NAME, MessageBoxButtons.OK, MessageBoxIcon.Exclamation);
            }
            else
            {
                OpenMapDialog openMapDialog = new OpenMapDialog();

                string[] mapDirectories = new string[allDirectories.Length];

                int k = 0;
                for (int i = 0; i < allDirectories.Length; i++)
                {
                    string mapName = allDirectories[i].Substring(allDirectories[i].LastIndexOf(Utils.SEPARATOR) + 1);
                    if (!mapName.Equals(Utils.OBJECT_FOLDER_NAME))
                    {
                        mapDirectories[k++] = allDirectories[i];
                        openMapDialog.AddMapName(mapName);
                    }
                }

                allDirectories = null;

                if (openMapDialog.ShowDialog() == DialogResult.OK)
                {
                    mapName = openMapDialog.SelectedMapName;

                    bitmapPath = mapDirectories[openMapDialog.SelectedMapIndex] +
                        Utils.SEPARATOR + mapName + Utils.BITMAP_EXTENSION;

                    this.Text = mapName + " - " + Utils.APP_NAME;


                    FileStream fileStream = null;

                    try
                    {
                        tileBitmap = Image.FromFile(bitmapPath);

                        fileStream = new FileStream(mapDirectories[openMapDialog.SelectedMapIndex] +
                            Utils.SEPARATOR + mapName + Utils.BINARY_MAP_EXTENSION,
                            FileMode.Open, FileAccess.Read);

                        /* DESCRIPTION */
                        int len = Utils.readUint16LittleEndian(fileStream);
                        byte[] descr = new byte[len];
                        fileStream.Read(descr, 0, len);
                        mapDescription = Encoding.ASCII.GetString(descr);
                        descrLabel.Text = mapDescription;
                        /* DESCRIPTION */

                        LoadMapObjects();

                        map.SetParametersFromFile(fileStream, tileBitmap, tileContainer, mapObjects, objectContainer);

                        fileStream.Close();
                        fileStream = null;
                    }
                    catch (Exception)
                    {
                        MessageBox.Show(Utils.ERROR_READING_MAP, Utils.APP_NAME, MessageBoxButtons.OK, MessageBoxIcon.Error);

                        if (fileStream != null)
                            fileStream.Close();
                        CloseMap();

                        return;
                    }

                    tileWidth = map.TileWidth;
                    tileHeight = map.TileHeight;
                    mapWidth = map.MapWidth;
                    mapHeight = map.MapHeight;

                    twLabel.Text = tileWidth.ToString() + " px";
                    thLabel.Text = tileHeight.ToString() + " px";
                    mwLabel.Text = mapWidth.ToString() + " x " + tileWidth.ToString() + " px";
                    mhLabel.Text = mapHeight.ToString() + " x " + tileHeight.ToString() + " px";
                    mnLabel.Text = mapName;

                    tileContainer.SetParameters(tileBitmap, tileWidth, tileHeight);
                    EnableAndShowControls();
                }

                mapDirectories = null;
                openMapDialog = null;
            }
        }

        private void ChangeMapProperties()
        {
            MapPropertiesDialog mapDialog = new MapPropertiesDialog();

            mapDialog.MapName = mapName;
            mapDialog.BitmapPath = bitmapPath;
            mapDialog.TileWidth = tileWidth;
            mapDialog.TileHeight = tileHeight;
            mapDialog.MapWidth = mapWidth;
            mapDialog.MapHeight = mapHeight;

            if (mapDialog.ShowDialog() == DialogResult.OK)
            {
                tileWidth = mapDialog.TileWidth;
                tileHeight = mapDialog.TileHeight;
                mapWidth = mapDialog.MapWidth;
                mapHeight = mapDialog.MapHeight;

                twLabel.Text = tileWidth.ToString() + " px";
                thLabel.Text = tileHeight.ToString() + " px";
                mwLabel.Text = mapWidth.ToString() + " x " + tileWidth.ToString() + " px";
                mhLabel.Text = mapHeight.ToString() + " x " + tileHeight.ToString() + " px";

                tileContainer.ResetParameters();
                map.ResetParameters();

                tileContainer.SetParameters(tileBitmap, tileWidth, tileHeight);
                try
                {
                    map.SetParameters(tileWidth, tileHeight, mapWidth, mapHeight, mapDialog.BaseTiles, tileBitmap, tileContainer, mapObjects, objectContainer);
                }
                catch (Exception)
                {
                    MessageBox.Show(Utils.ERROR_CREATING_MAP, Utils.APP_NAME, MessageBoxButtons.OK, MessageBoxIcon.Error);
                    CloseMap();
                    return;
                }
            }

            mapDialog = null;
        }

        private void CloseMap()
        {
            DisableAndHideControls();
            map.ResetParameters();
            tileContainer.ResetParameters();
            UnloadMapObjects();

            tileBitmap = null;

            this.Text = Utils.APP_NAME;

            twLabel.Text = null;
            thLabel.Text = null;
            mwLabel.Text = null;
            mhLabel.Text = null;
            mnLabel.Text = null;
            descrLabel.Text = null;

            tileWidth = 0;
            tileHeight = 0;
            mapWidth = 0;
            mapHeight = 0;

            mapName = null;
            bitmapPath = null;
        }

        private void SaveMap()
        {
            string mapDirectory = Utils.CURRENT_FOLDER + Utils.MAP_FOLDER + mapName;

            if (!Directory.Exists(mapDirectory))
            {
                Directory.CreateDirectory(mapDirectory);
                File.Copy(bitmapPath, mapDirectory + Utils.SEPARATOR + mapName + Utils.BITMAP_EXTENSION);
            }

            FileStream stream = new FileStream(mapDirectory + Utils.SEPARATOR + mapName + Utils.BINARY_MAP_EXTENSION, 
                FileMode.Create, FileAccess.Write);

            if (mapDescription != null)
            {
                Utils.writeUint16LittleEndian(stream, (UInt16)mapDescription.Length);
                //stream.Write(Convert.FromBase64String(mapDescription), 0, mapDescription.Length);
                byte[] descr = Encoding.ASCII.GetBytes(mapDescription);
                stream.Write(descr, 0, descr.Length);
            }
            else
                Utils.writeUint16LittleEndian(stream, (UInt16)0);
            
            map.SaveMap(stream);

            stream.Close();

            stream = null;
        }

        private bool SaveBeforeClosing()
        {
            if (isMapLoaded)
            {
                if (!map.IsSaved)
                {
                    switch (MessageBox.Show(Utils.getSavePrompt(mapName), Utils.APP_NAME,
                        MessageBoxButtons.YesNoCancel, MessageBoxIcon.Exclamation))
                    {
                        case DialogResult.Yes:
                            SaveMap();
                            CloseMap();
                            break;
                        case DialogResult.No:
                            CloseMap();
                            break;
                        case DialogResult.Cancel:
                            return false;
                    }
                }
                else
                    CloseMap();
            }

            return true;
        }

        void LoadMapObjects()
        {
            int objectId = 0;
            string pathToObject = Utils.OBJECT_FOLDER + objectId;

            while (Directory.Exists(pathToObject))
            {
                FileStream stream = new FileStream(pathToObject + Utils.OBJECT_INFO_PATH, FileMode.Open, FileAccess.Read);

                /* HEIGHT IS THE LAST ATTRIBUTE */
                stream.Seek(-1 * sizeof(UInt16), SeekOrigin.End);
                /* HEIGHT IS THE LAST ATTRIBUTE */

                Image frame = Image.FromFile(pathToObject + Utils.FRAMES_FOLDER + Utils.FIRST_FRAME);
                mapObjects.Add(new ObjectType(frame, Utils.readUint16LittleEndian(stream)));
                objectContainer.AddObject(frame);

                stream.Close();

                objectId++;
                pathToObject = Utils.OBJECT_FOLDER + objectId;
            }
        }

        void UnloadMapObjects()
        {
            mapObjects.Clear();
            objectContainer.Clear();
        }

        private void EnableAndShowControls()
        {
            isMapLoaded = true;

            this.closeToolStripMenuItem.Enabled = true;
            this.undoToolStripMenuItem.Enabled = true;
            this.redoToolStripMenuItem.Enabled = true;
            this.changePropertiesToolStripMenuItem.Enabled = true;
            this.showGridToolStripMenuItem.Enabled = true;
            this.layerToolStripMenuItem.Enabled = true;
            this.exportMinimapToolStripMenuItem.Enabled = true;

            this.objectEditorToolStripMenuItem.Enabled = false;
            this.vehicleEditorToolStripMenuItem.Enabled = false;

            this.layerSelectionComboBox.Enabled = true;
            this.layerSelectionComboBox.SelectedIndex = 0;

            this.showGridCheckBox.Enabled = true;

            this.descrLabel.Enabled = true;

            this.map.Show();
        }

        private void DisableAndHideControls()
        {
            this.saveToolStripMenuItem.Enabled = false;
            this.closeToolStripMenuItem.Enabled = false;
            this.undoToolStripMenuItem.Enabled = false;
            this.redoToolStripMenuItem.Enabled = false;
            this.changePropertiesToolStripMenuItem.Enabled = false;
            this.showGridToolStripMenuItem.Enabled = false;
            this.layerToolStripMenuItem.Enabled = false;
            this.exportMinimapToolStripMenuItem.Enabled = false;

            this.objectEditorToolStripMenuItem.Enabled = true;
            this.vehicleEditorToolStripMenuItem.Enabled = true;
          
            this.layerSelectionComboBox.SelectedIndex = -1;
            this.layerSelectionComboBox.Enabled = false;

            this.showGridCheckBox.Enabled = false;

            this.descrLabel.Enabled = false;

            this.map.Hide();

            isMapLoaded = false;
        }
    }
}