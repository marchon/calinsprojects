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
    public partial class ObjectPropertiesDialog : Form
    {
        
        public ArrayList framesPathList = new ArrayList();

        public UInt16 ObjectType
        {
            set
            {
                typeChooser.SelectedIndex = value;
            }

            get
            {
                return (UInt16)(typeChooser.SelectedIndex);
            }
        }

        public UInt16 ObjectNumberOfFrames
        {
            /*set
            {
                numberOfFramesLabel.Text = value.ToString();
            }*/

            get
            {
                return (UInt16)(framesPathList.Count);
            }
        }

        public UInt16 ObjectDelayBetweenFrames
        {
            set
            {
                delayBox.Text = value.ToString();
            }

            get
            {
                return UInt16.Parse(delayBox.Text);
            }
        }

        public UInt16 ObjectXDimension
        {
            set
            {
                dimXBox.Text = value.ToString();
            }

            get
            {
                return UInt16.Parse(dimXBox.Text);
            }
        }

        public UInt16 ObjectYDimension
        {
            set
            {
                dimYBox.Text = value.ToString();
            }

            get
            {
                return UInt16.Parse(dimYBox.Text);
            }
        }

        public UInt16 ObjectHeight
        {
            set
            {
                heightBox.Text = value.ToString();
            }

            get
            {
                return UInt16.Parse(heightBox.Text);
            }
        }

        public ObjectPropertiesDialog()
        {
            InitializeComponent();
            framesPathList.Clear();


            this.typeChooser.SelectedIndex = 0;

            this.delayBox.KeyPress += new KeyPressEventHandler(this.textBox_KeyPress);
            this.dimXBox.KeyPress += new KeyPressEventHandler(this.textBox_KeyPress);
            this.dimYBox.KeyPress += new KeyPressEventHandler(this.textBox_KeyPress);
            this.heightBox.KeyPress += new KeyPressEventHandler(this.textBox_KeyPress);

            this.delayBox.KeyUp += new KeyEventHandler(this.textBox_KeyUp);
            this.dimXBox.KeyUp += new KeyEventHandler(this.textBox_KeyUp);
            this.dimYBox.KeyUp += new KeyEventHandler(this.textBox_KeyUp);
            this.heightBox.KeyUp += new KeyEventHandler(this.textBox_KeyUp);
        }

        private void ObjectPropertiesDialog_Load(object sender, EventArgs e)
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

        private void frameAdder_Click(object sender, EventArgs e)
        {  
            OpenFileDialog openfile = new OpenFileDialog();  
          
            openfile.Title = "Select Object frames (At least one BMP file)...";
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
                        dimXBox.Text = firstFrame.Width.ToString();
                        dimYBox.Text = firstFrame.Height.ToString();

                        Utils.stretchImageToPictureBox(firstFrame, previewBox);
                    }
                }

                numberOfFramesLabel.Text = framesPathList.Count.ToString();
            }

            ceckIfAllInformationNeededIsProvided();

            openfile = null;
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
            if (framesPathList.Count > 0 &&
                delayBox.Text.Length > 0 &&
                dimXBox.Text.Length > 0 &&
                dimYBox.Text.Length > 0 &&
                heightBox.Text.Length > 0)
                okButton.Enabled = true;

            else
                okButton.Enabled = false;
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
            {
                previewBox.Image = null;
                okButton.Enabled = false;
            }
        }
    }
}