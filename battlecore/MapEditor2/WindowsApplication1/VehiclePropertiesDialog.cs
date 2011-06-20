using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Collections;

namespace MapEditor2
{
    public partial class VehiclePropertiesDialog : Form
    {
        public ArrayList framesPathList = new ArrayList();
        public string vehiclePreviewPath = null;

        public string VehicleName
        {
            set
            {
                nameTextBox.Text = value;
            }

            get
            {
                return nameTextBox.Text;
            }
        }

        public UInt16 VehicleAcceleration
        {
            set
            {
                accelerationTextBox.Text = value.ToString();
            }

            get
            {
                return UInt16.Parse(accelerationTextBox.Text);
            }
        }

        public UInt16 VehicleMaxSpeed
        {
            set
            {
                maxSpeedTextBox.Text = value.ToString();
            }

            get
            {
                return UInt16.Parse(maxSpeedTextBox.Text);
            }
        }

        public UInt16 VehicleManevrability
        {
            set
            {
                manevrabilityTextBox.Text = value.ToString();
            }

            get
            {
                return UInt16.Parse(manevrabilityTextBox.Text);
            }
        }

        public UInt16 VehicleArmor
        {
            set
            {
                armorTextBox.Text = value.ToString();
            }

            get
            {
                return UInt16.Parse(armorTextBox.Text);
            }
        }

        public UInt16 VehicleHitPoints
        {
            set
            {
                hitPointsTextBox.Text = value.ToString();
            }

            get
            {
                return UInt16.Parse(hitPointsTextBox.Text);
            }
        }

        public VehiclePropertiesDialog()
        {
            InitializeComponent();

            this.accelerationTextBox.KeyPress += new KeyPressEventHandler(this.textBox_KeyPress);
            this.maxSpeedTextBox.KeyPress += new KeyPressEventHandler(this.textBox_KeyPress);
            this.manevrabilityTextBox.KeyPress += new KeyPressEventHandler(this.textBox_KeyPress);
            this.armorTextBox.KeyPress += new KeyPressEventHandler(this.textBox_KeyPress);
            this.hitPointsTextBox.KeyPress += new KeyPressEventHandler(this.textBox_KeyPress);

            this.accelerationTextBox.KeyUp += new KeyEventHandler(this.textBox_KeyUp);
            this.maxSpeedTextBox.KeyUp += new KeyEventHandler(this.textBox_KeyUp);
            this.manevrabilityTextBox.KeyUp += new KeyEventHandler(this.textBox_KeyUp);
            this.armorTextBox.KeyUp += new KeyEventHandler(this.textBox_KeyUp);
            this.hitPointsTextBox.KeyUp += new KeyEventHandler(this.textBox_KeyUp);
        }

        private void VehiclePropertiesDialog_Load(object sender, EventArgs e)
        {
            if (framesPathList.Count > 0)
            {
                Image firstFrame = Image.FromFile(framesPathList[0].ToString());
                Utils.stretchImageToPictureBox(firstFrame, previewBox);
                firstFrame.Dispose();
            }

            numberOfFramesLabel.Text = framesPathList.Count.ToString();

            ceckIfAllInformationNeededIsProvided();
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

        private void textBox_KeyUp(object sender, System.Windows.Forms.KeyEventArgs e)
        {
            ceckIfAllInformationNeededIsProvided();
        }

        private void ceckIfAllInformationNeededIsProvided()
        {
            if (framesPathList.Count == Utils.VEHICLE_DIRECTIONS &&
                vehiclePreviewPath != null &&
                nameTextBox.Text.Length > 0 &&
                accelerationTextBox.Text.Length > 0 &&
                maxSpeedTextBox.Text.Length > 0 &&
                manevrabilityTextBox.Text.Length > 0 &&
                armorTextBox.Text.Length > 0 &&
                hitPointsTextBox.Text.Length > 0)
                okButton.Enabled = true;

            else
                okButton.Enabled = false;
        }

        private void addFramesButton_Click(object sender, EventArgs e)
        {
            OpenFileDialog openfile = new OpenFileDialog();

            openfile.Title = "Select Vehicle frames...";
            openfile.Filter = "Windows Bitmap|*.bmp";
            openfile.Multiselect = true;

            if (openfile.ShowDialog() != DialogResult.Cancel)
            {
                foreach (string framePath in openfile.FileNames)
                {
                    framesPathList.Add(framePath);

                    if (framesPathList.Count == 1)
                    {
                        Image firstFrame = Image.FromFile(framePath);
                        Utils.stretchImageToPictureBox(firstFrame, previewBox);
                    }
                }

                numberOfFramesLabel.Text = framesPathList.Count.ToString();
            }

            ceckIfAllInformationNeededIsProvided();

            openfile = null;
        }

        private void removeFramesButton_Click(object sender, EventArgs e)
        {
            framesPathList.Clear();
            previewBox.Image = null;

            numberOfFramesLabel.Text = ((Int32)(0)).ToString();

            okButton.Enabled = false;
        }

        private void removeLastFrameButton_Click(object sender, EventArgs e)
        {
            if (framesPathList.Count == 0)
                return;

            framesPathList.RemoveAt(framesPathList.Count - 1);

            numberOfFramesLabel.Text = framesPathList.Count.ToString();

            if (framesPathList.Count == 0)
                previewBox.Image = null;

            if(framesPathList.Count != Utils.VEHICLE_DIRECTIONS)
                okButton.Enabled = false;
            else
                okButton.Enabled = true;
        }

        private void setPreviewBitmapButton_Click(object sender, EventArgs e)
        {
            OpenFileDialog openfile = new OpenFileDialog();

            openfile.Title = "Select Vehicle Preview (One BMP file)...";
            openfile.Filter = "Windows Bitmap|*.bmp";
            openfile.Multiselect = false;

            if (openfile.ShowDialog() != DialogResult.Cancel)
            {
                vehiclePreviewPath = openfile.FileName;    
            }

            ceckIfAllInformationNeededIsProvided();

            openfile = null;
        }
    }
}