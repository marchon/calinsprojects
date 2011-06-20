using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.IO;

namespace MapEditor2
{
    public partial class MapPropertiesDialog : Form
    {

        public string MapName
        {
            get
            {
                return nameTextBox.Text;
            }
            set
            {
                nameTextBox.Text = value;
            }
        }

        public string BitmapPath
        {
            get
            {
                return pathTextBox.Text;
            }
            set
            {
                pathTextBox.Text = value;
            }
        }

        public UInt16 TileWidth
        {
            get
            {
                return UInt16.Parse(tileWidthTextBox.Text);
            }
            set
            {
                tileWidthTextBox.Text = value.ToString();
            }
        }

        public UInt16 TileHeight
        {
            get
            {
                return UInt16.Parse(tileHeightTextBox.Text);
            }
            set
            {
                tileHeightTextBox.Text = value.ToString();
            }
        }

        public UInt16 MapWidth
        {
            get
            {
                return UInt16.Parse(mapWidthTextBox.Text);
            }
            set
            {
                mapWidthTextBox.Text = value.ToString();
            }
        }

        public UInt16 MapHeight
        {
            get
            {
                return UInt16.Parse(mapHeightTextBox.Text);
            }
            set
            {
                mapHeightTextBox.Text = value.ToString();
            }
        }

        public UInt16 BaseTiles
        {
            get
            {
                return UInt16.Parse(baseTilesTextBox.Text);
            }
        }
        
        public MapPropertiesDialog()
        {
            InitializeComponent();

            this.tileWidthTextBox.KeyPress += new KeyPressEventHandler(this.textBox_KeyPress);
            this.tileHeightTextBox.KeyPress += new KeyPressEventHandler(this.textBox_KeyPress);
            this.mapWidthTextBox.KeyPress += new KeyPressEventHandler(this.textBox_KeyPress);
            this.mapHeightTextBox.KeyPress += new KeyPressEventHandler(this.textBox_KeyPress);
            this.baseTilesTextBox.KeyPress += new KeyPressEventHandler(this.textBox_KeyPress);

            this.nameTextBox.TextChanged += new EventHandler(this.textBox_TextChanged);
            this.tileWidthTextBox.TextChanged += new EventHandler(this.textBox_TextChanged);
            this.tileHeightTextBox.TextChanged += new EventHandler(this.textBox_TextChanged);
            this.mapWidthTextBox.TextChanged += new EventHandler(this.textBox_TextChanged);
            this.mapHeightTextBox.TextChanged += new EventHandler(this.textBox_TextChanged);
            this.baseTilesTextBox.TextChanged += new EventHandler(this.textBox_TextChanged);
        }

        protected override void  OnLoad(EventArgs e)
        {
            if (pathTextBox.Text.Length > 0)
            {
                try
                {
                    loadPicture(Image.FromFile(pathTextBox.Text));
                }
                catch (Exception)
                {
                    pathTextBox.Text = "";
                }

                if (nameTextBox.Text.Length > 0)
                {
                    nameTextBox.Enabled = false;
                    browseButton.Enabled = false;
                    ceckIfAllInformationNeededIsProvided();
                }
            }

 	        base.OnLoad(e);
        }

        protected override void OnClosing(CancelEventArgs e)
        {
            if (!e.Cancel && (this.DialogResult == DialogResult.OK))
            {
                string mapDirectory = Utils.CURRENT_FOLDER + Utils.MAP_FOLDER + nameTextBox.Text;

                if (nameTextBox.Enabled && Directory.Exists(mapDirectory))
                {
                    e.Cancel = true;
                    MessageBox.Show(Utils.MAP_EXISTS_ERROR, Utils.ERROR_CAPTION, MessageBoxButtons.OK, MessageBoxIcon.Error);
                    nameTextBox.Text = "";
                    nameTextBox.Focus();
                }
                else
                {
                    UInt16 tileWidth = UInt16.Parse(tileWidthTextBox.Text);
                    UInt16 tileHeight = UInt16.Parse(tileHeightTextBox.Text);
                    UInt16 mapWidth = UInt16.Parse(mapWidthTextBox.Text);
                    UInt16 mapHeight = UInt16.Parse(mapHeightTextBox.Text);
                    UInt16 baseTiles = UInt16.Parse(baseTilesTextBox.Text);

                    if (tileWidth > Utils.MAX_TILE_DIM || tileWidth < Utils.MIN_TILE_DIM)
                    {
                        e.Cancel = true;
                        MessageBox.Show(Utils.TILE_WIDTH_ERROR, Utils.ERROR_CAPTION, MessageBoxButtons.OK, MessageBoxIcon.Error);
                        tileWidthTextBox.Text = "";
                        tileWidthTextBox.Focus();
                    }
                    else if (tileHeight > Utils.MAX_TILE_DIM || tileHeight < Utils.MIN_TILE_DIM)
                    {
                        e.Cancel = true;
                        MessageBox.Show(Utils.TILE_HEIGHT_ERROR, Utils.ERROR_CAPTION, MessageBoxButtons.OK, MessageBoxIcon.Error);
                        tileHeightTextBox.Text = "";
                        tileHeightTextBox.Focus();
                    }
                    else if (mapWidth > Utils.MAX_MAP_DIM || mapWidth < Utils.MIN_MAP_DIM)
                    {
                        e.Cancel = true;
                        MessageBox.Show(Utils.MAP_WIDTH_ERROR, Utils.ERROR_CAPTION, MessageBoxButtons.OK, MessageBoxIcon.Error);
                        mapWidthTextBox.Text = "";
                        mapWidthTextBox.Focus();
                    }
                    else if (mapHeight > Utils.MAX_MAP_DIM || mapHeight < Utils.MIN_MAP_DIM)
                    {
                        e.Cancel = true;
                        MessageBox.Show(Utils.MAP_HEIGHT_ERROR, Utils.ERROR_CAPTION, MessageBoxButtons.OK, MessageBoxIcon.Error);
                        mapHeightTextBox.Text = "";
                        mapHeightTextBox.Focus();
                    }
                    else if (baseTiles == 0 || Math.Pow(baseTiles, 2) > 
                        (UInt16.Parse(widthValueLabel.Text) / tileWidth) *
                        (UInt16.Parse(heightValueLabel.Text) / tileHeight))
                    {
                        e.Cancel = true;
                        MessageBox.Show(Utils.BASE_TILES_ERROR, Utils.ERROR_CAPTION, MessageBoxButtons.OK, MessageBoxIcon.Error);
                        baseTilesTextBox.Text = "";
                        baseTilesTextBox.Focus();
                    }
                    else if (UInt16.Parse(widthValueLabel.Text) % tileWidth != 0)
                    {
                        e.Cancel = true;
                        MessageBox.Show(Utils.TILE_WIDTH_DIVISION_ERROR, Utils.ERROR_CAPTION, MessageBoxButtons.OK, MessageBoxIcon.Error);
                        tileWidthTextBox.Text = "";
                        tileWidthTextBox.Focus();
                    }
                    else if (UInt16.Parse(heightValueLabel.Text) % tileHeight != 0)
                    {
                        e.Cancel = true;
                        MessageBox.Show(Utils.TILE_HEIGHT_DIVISION_ERROR, Utils.ERROR_CAPTION, MessageBoxButtons.OK, MessageBoxIcon.Error);
                        tileHeightTextBox.Text = "";
                        tileHeightTextBox.Focus();
                    }
                }
            }
            base.OnClosing(e);
        }

        private void textBox_KeyPress(object sender, System.Windows.Forms.KeyPressEventArgs e)
        {
            if (e.KeyChar != (char)8)  //backspace
            {
                try
                {
                    UInt16.Parse(((TextBox)sender).Text + e.KeyChar);
                }
                catch (Exception)
                {
                    e.Handled = true;
                }
            }
        }

        private void textBox_TextChanged(object sender, EventArgs e)
        {
            ceckIfAllInformationNeededIsProvided();
        }

        private void ceckIfAllInformationNeededIsProvided()
        {
            if (nameTextBox.Text.Length > 0 &&
                tileWidthTextBox.Text.Length > 0 &&
                tileHeightTextBox.Text.Length > 0 &&
                mapWidthTextBox.Text.Length > 0 &&
                mapHeightTextBox.Text.Length > 0 &&
                baseTilesTextBox.Text.Length > 0 &&
                pathTextBox.Text.Length > 0)
                okButton.Enabled = true;
            else
                okButton.Enabled = false;
        }

        private void browseButton_Click(object sender, EventArgs e)
        {
            OpenFileDialog openFileDialog = new OpenFileDialog();

            openFileDialog.Title = "Select bitmap with tiles(One BMP file)...";
            openFileDialog.Filter = "Windows Bitmap|*.bmp";

            if (openFileDialog.ShowDialog() != DialogResult.Cancel)
            {
                pathTextBox.Text = openFileDialog.FileName;

                loadPicture(Image.FromFile(openFileDialog.FileName));          
            }

            ceckIfAllInformationNeededIsProvided();

            openFileDialog = null;
        }

        private void loadPicture(Image previewImage)
        {
            if (previewImage.Width >= previewImage.Height)
            {
                this.bitmapPreviewPictureBox.Image = new Bitmap(previewImage,
                           bitmapPreviewPictureBox.Width,
                           previewImage.Height * bitmapPreviewPictureBox.Width / previewImage.Width);
            }
            else
            {
                this.bitmapPreviewPictureBox.Image = new Bitmap(previewImage,
                           previewImage.Width * bitmapPreviewPictureBox.Height / previewImage.Height,
                           bitmapPreviewPictureBox.Height);
            }

            this.bitmapPreviewPictureBox.SizeMode = PictureBoxSizeMode.CenterImage;

            this.widthValueLabel.Text = previewImage.Width.ToString();
            this.heightValueLabel.Text = previewImage.Height.ToString();
        }
    }
}