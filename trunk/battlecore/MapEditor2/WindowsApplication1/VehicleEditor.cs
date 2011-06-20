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
    public partial class VehicleEditor : Form
    {
        public VehicleEditor()
        {
            InitializeComponent();
        }

        private void addVehicleButton_Click(object sender, EventArgs e)
        {
            VehiclePropertiesDialog vehPropDlg = new VehiclePropertiesDialog();

            if (vehPropDlg.ShowDialog() == DialogResult.OK)
            {
                Directory.SetCurrentDirectory(Utils.CURRENT_FOLDER);
                
                string newVehiclePath = Utils.VEHICLE_FOLDER + vehPropDlg.VehicleName;
                string newVehicleFramesPath = newVehiclePath + Utils.FRAMES_FOLDER;

                Directory.CreateDirectory(newVehiclePath);
                Directory.CreateDirectory(newVehicleFramesPath);

                int currentFrameID = 0;

                foreach (string framePath in vehPropDlg.framesPathList)
                {
                    File.Copy(framePath, newVehicleFramesPath + currentFrameID + Utils.BITMAP_EXTENSION);
                    currentFrameID++;
                }

                if (vehPropDlg.vehiclePreviewPath != null)
                    File.Copy(vehPropDlg.vehiclePreviewPath, newVehiclePath + Utils.VEHICLE_PREVIEW);

                FileStream stream = new FileStream(newVehiclePath + Utils.VEHICLE_INFO_PATH, FileMode.Create, FileAccess.Write);

                /*
                 * The order is:
                 * accel
                 * maxspd
                 * manevrability
                 * armor
                 * hitpoints
                */

                Utils.writeUint16LittleEndian(stream, vehPropDlg.VehicleAcceleration);
                Utils.writeUint16LittleEndian(stream, vehPropDlg.VehicleMaxSpeed);
                Utils.writeUint16LittleEndian(stream, vehPropDlg.VehicleManevrability);
                Utils.writeUint16LittleEndian(stream, vehPropDlg.VehicleArmor);
                Utils.writeUint16LittleEndian(stream, vehPropDlg.VehicleHitPoints);

                stream.Close();
                stream = null;

                vehicleListBox.Items.Add(vehPropDlg.VehicleName);

                if (vehicleListBox.Items.Count == 1)
                    vehicleListBox.SelectedIndex = 0;
            }
        }

        private void VehicleEditor_Load(object sender, EventArgs e)
        {
            Directory.SetCurrentDirectory(Utils.CURRENT_FOLDER);

            string[] nameList = Directory.GetDirectories(Utils.VEHICLE_FOLDER);

            for (int i = 0; i < nameList.Length; i++)
                vehicleListBox.Items.Add(nameList[i].Substring(nameList[i].LastIndexOf('\\') + 1));

            if (vehicleListBox.Items.Count > 0)
                vehicleListBox.SelectedIndex = 0;
        }

        private void vehicleListBox_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (vehicleListBox.SelectedItem == null)
                return;

            Directory.SetCurrentDirectory(Utils.CURRENT_FOLDER);

            Image preview = Image.FromFile(Utils.VEHICLE_FOLDER +
                vehicleListBox.SelectedItem.ToString() + Utils.FRAMES_FOLDER + Utils.FIRST_FRAME);

            Utils.stretchImageToPictureBox(preview, previewBox);

            preview.Dispose();
        }

        private void editVehicleButton_Click(object sender, EventArgs e)
        {
            Directory.SetCurrentDirectory(Utils.CURRENT_FOLDER);

            VehiclePropertiesDialog vehPropDlg = new VehiclePropertiesDialog();

            string currentVehicleFolder = Utils.VEHICLE_FOLDER + vehicleListBox.SelectedItem.ToString();
            string frameFolder = currentVehicleFolder + Utils.FRAMES_FOLDER;
            string tempFrameFolder = frameFolder + Utils.TEMP_FOLDER;

            bool renameCurrentVehicleFolder = false;

            FileStream stream = new FileStream(currentVehicleFolder + Utils.VEHICLE_INFO_PATH, FileMode.Open, FileAccess.ReadWrite);

            vehPropDlg.VehicleName = vehicleListBox.SelectedItem.ToString();
            vehPropDlg.VehicleAcceleration = Utils.readUint16LittleEndian(stream);
            vehPropDlg.VehicleMaxSpeed = Utils.readUint16LittleEndian(stream);
            vehPropDlg.VehicleManevrability = Utils.readUint16LittleEndian(stream);
            vehPropDlg.VehicleArmor = Utils.readUint16LittleEndian(stream);
            vehPropDlg.VehicleHitPoints = Utils.readUint16LittleEndian(stream);

            Directory.CreateDirectory(tempFrameFolder);

            foreach (string framePath in Directory.GetFiles(frameFolder))
                File.Copy(framePath, framePath.Insert(framePath.LastIndexOf('\\'), Utils.TEMP_FOLDER), true);

            for (int currentFrameID = 0; currentFrameID < Utils.VEHICLE_DIRECTIONS; currentFrameID++)
                vehPropDlg.framesPathList.Add(tempFrameFolder + currentFrameID + Utils.BITMAP_EXTENSION);

            vehPropDlg.vehiclePreviewPath = currentVehicleFolder + Utils.VEHICLE_PREVIEW;

            if (vehPropDlg.ShowDialog() == DialogResult.OK)
            {
                Directory.SetCurrentDirectory(Utils.CURRENT_FOLDER);

                if (vehPropDlg.vehiclePreviewPath.CompareTo(currentVehicleFolder + Utils.VEHICLE_PREVIEW) != 0)
                    File.Copy(vehPropDlg.vehiclePreviewPath, currentVehicleFolder + Utils.VEHICLE_PREVIEW, true);

                foreach (string framePath in Directory.GetFiles(frameFolder))
                    File.Delete(framePath);

                int currentFrameID = 0;

                foreach (string framePath in vehPropDlg.framesPathList)
                {
                    File.Copy(framePath, frameFolder + currentFrameID + Utils.BITMAP_EXTENSION);
                    currentFrameID++;
                }

                stream.Seek(0, SeekOrigin.Begin);

                Utils.writeUint16LittleEndian(stream, vehPropDlg.VehicleAcceleration);
                Utils.writeUint16LittleEndian(stream, vehPropDlg.VehicleMaxSpeed);
                Utils.writeUint16LittleEndian(stream, vehPropDlg.VehicleManevrability);
                Utils.writeUint16LittleEndian(stream, vehPropDlg.VehicleArmor);
                Utils.writeUint16LittleEndian(stream, vehPropDlg.VehicleHitPoints);

                if (vehPropDlg.VehicleName.CompareTo(vehicleListBox.SelectedItem.ToString()) != 0)
                    renameCurrentVehicleFolder = true;

                vehicleListBox_SelectedIndexChanged(null, null);
            }

            Directory.SetCurrentDirectory(Utils.CURRENT_FOLDER);

            Directory.Delete(tempFrameFolder, true);

            stream.Close();
            stream = null;

            if (renameCurrentVehicleFolder)
            {
                try
                {
                    Directory.Move(currentVehicleFolder, Utils.VEHICLE_FOLDER + vehPropDlg.VehicleName);

                    int ind = vehicleListBox.SelectedIndex;
                    vehicleListBox.Items.RemoveAt(ind);
                    vehicleListBox.Items.Insert(ind, vehPropDlg.VehicleName);
                    vehicleListBox.SelectedIndex = ind;
                }
                catch (IOException)
                {
                    MessageBox.Show(Utils.ERROR_RENAMING, Utils.APP_NAME,
                        MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            }
        }
    }
}