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
    public partial class ObjectEditor : Form
    {
        public ObjectEditor()
        {
            InitializeComponent();
        }

        private void addObjectButton_Click(object sender, EventArgs e)
        {
            ObjectPropertiesDialog objPropDlg = new ObjectPropertiesDialog();

            if (objPropDlg.ShowDialog() == DialogResult.OK)
            {
                string newObjectPath = null;

                Directory.SetCurrentDirectory(Utils.CURRENT_FOLDER);

                int newObjectID = 0;

                do
                {
                    newObjectPath = null;

                    newObjectPath += Utils.OBJECT_FOLDER;
                    newObjectPath += newObjectID;

                    newObjectID++;
                } while (Directory.Exists(newObjectPath));

                string newObjectFramesPath = newObjectPath + Utils.FRAMES_FOLDER;

                Directory.CreateDirectory(newObjectPath);
                Directory.CreateDirectory(newObjectFramesPath);

                int currentFrameID = 0;

                foreach (string framePath in objPropDlg.framesPathList)
                {
                    File.Copy(framePath, newObjectFramesPath + currentFrameID + Utils.BITMAP_EXTENSION);
                    currentFrameID++;
                }

                FileStream stream = new FileStream(newObjectPath + Utils.OBJECT_INFO_PATH, FileMode.Create, FileAccess.Write);

                /*
                 * The order is:
                 * type
                 * frames
                 * delay
                 * dimx
                 * dimy
                 * height
                */

                Utils.writeUint16LittleEndian(stream, objPropDlg.ObjectType);
                Utils.writeUint16LittleEndian(stream, objPropDlg.ObjectNumberOfFrames);
                Utils.writeUint16LittleEndian(stream, objPropDlg.ObjectDelayBetweenFrames);
                Utils.writeUint16LittleEndian(stream, objPropDlg.ObjectXDimension);
                Utils.writeUint16LittleEndian(stream, objPropDlg.ObjectYDimension);
                Utils.writeUint16LittleEndian(stream, objPropDlg.ObjectHeight);

                stream.Close();
                stream = null;

                objectListBox.Items.Add((newObjectID - 1).ToString());

                if (objectListBox.Items.Count == 1)
                    objectListBox.SelectedIndex = 0;
            }

        }

        private void ObjectEditor_Load(object sender, EventArgs e)
        {
            Directory.SetCurrentDirectory(Utils.CURRENT_FOLDER);

            int objectId = 0;

            while (Directory.Exists(Utils.OBJECT_FOLDER + objectId))
            {
                objectListBox.Items.Add(objectId.ToString());
                objectId++;
            }

            if (objectListBox.Items.Count > 0)
                objectListBox.SelectedIndex = 0;
        }

        private void objectListBox_SelectedIndexChanged(object sender, EventArgs e)
        {
            Directory.SetCurrentDirectory(Utils.CURRENT_FOLDER);

            Utils.stretchImageToPictureBox(Image.FromFile(Utils.OBJECT_FOLDER +
                objectListBox.SelectedIndex + Utils.FRAMES_FOLDER + Utils.FIRST_FRAME),
                previewBox);
        }

        private void editObjectButton_Click(object sender, EventArgs e)
        {
            Directory.SetCurrentDirectory(Utils.CURRENT_FOLDER);

            ObjectPropertiesDialog objPropDlg = new ObjectPropertiesDialog();

            FileStream stream = new FileStream(Utils.OBJECT_FOLDER + objectListBox.SelectedIndex +
                Utils.OBJECT_INFO_PATH, FileMode.Open, FileAccess.ReadWrite);

            int nbFr = 0;

            objPropDlg.ObjectType = Utils.readUint16LittleEndian(stream);
            nbFr /*= objPropDlg.ObjectNumberOfFrames*/ = Utils.readUint16LittleEndian(stream);
            objPropDlg.ObjectDelayBetweenFrames = Utils.readUint16LittleEndian(stream);
            objPropDlg.ObjectXDimension = Utils.readUint16LittleEndian(stream);
            objPropDlg.ObjectYDimension = Utils.readUint16LittleEndian(stream);
            objPropDlg.ObjectHeight = Utils.readUint16LittleEndian(stream);

            string frameFolder = Utils.OBJECT_FOLDER + objectListBox.SelectedIndex +
                    Utils.FRAMES_FOLDER;
            string tempFrameFolder = frameFolder + Utils.TEMP_FOLDER;

            Directory.CreateDirectory(tempFrameFolder);

            foreach (string framePath in Directory.GetFiles(frameFolder))
                File.Copy(framePath, framePath.Insert(framePath.LastIndexOf('\\'), Utils.TEMP_FOLDER), true);

            for (int currentFrameID = 0; currentFrameID < nbFr; currentFrameID++)
                objPropDlg.framesPathList.Add(tempFrameFolder + currentFrameID + Utils.BITMAP_EXTENSION);

            if (objPropDlg.ShowDialog() == DialogResult.OK)
            {
                Directory.SetCurrentDirectory(Utils.CURRENT_FOLDER);

                foreach (string framePath in Directory.GetFiles(frameFolder))
                    File.Delete(framePath);

                int currentFrameID = 0;

                foreach (string framePath in objPropDlg.framesPathList)
                {
                    File.Copy(framePath, frameFolder + currentFrameID + Utils.BITMAP_EXTENSION);
                    currentFrameID++;
                }

                stream.Seek(0, SeekOrigin.Begin);

                Utils.writeUint16LittleEndian(stream, objPropDlg.ObjectType);
                Utils.writeUint16LittleEndian(stream, objPropDlg.ObjectNumberOfFrames);
                Utils.writeUint16LittleEndian(stream, objPropDlg.ObjectDelayBetweenFrames);
                Utils.writeUint16LittleEndian(stream, objPropDlg.ObjectXDimension);
                Utils.writeUint16LittleEndian(stream, objPropDlg.ObjectYDimension);
                Utils.writeUint16LittleEndian(stream, objPropDlg.ObjectHeight);

                objectListBox_SelectedIndexChanged(null, null);
            }

            Directory.SetCurrentDirectory(Utils.CURRENT_FOLDER);

            Directory.Delete(tempFrameFolder, true);

            stream.Close();
            stream = null;
        }
    }
}