namespace MapEditor2
{
    partial class VehicleEditor
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.addVehicleButton = new System.Windows.Forms.Button();
            this.previewBox = new System.Windows.Forms.PictureBox();
            this.vehicleListBox = new System.Windows.Forms.ListBox();
            this.editVehicleButton = new System.Windows.Forms.Button();
            this.objectsGroupBox = new System.Windows.Forms.GroupBox();
            ((System.ComponentModel.ISupportInitialize)(this.previewBox)).BeginInit();
            this.objectsGroupBox.SuspendLayout();
            this.SuspendLayout();
            // 
            // addVehicleButton
            // 
            this.addVehicleButton.Location = new System.Drawing.Point(9, 12);
            this.addVehicleButton.Name = "addVehicleButton";
            this.addVehicleButton.Size = new System.Drawing.Size(197, 23);
            this.addVehicleButton.TabIndex = 0;
            this.addVehicleButton.Text = "Add Vehicle";
            this.addVehicleButton.UseVisualStyleBackColor = true;
            this.addVehicleButton.Click += new System.EventHandler(this.addVehicleButton_Click);
            // 
            // previewBox
            // 
            this.previewBox.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.previewBox.Location = new System.Drawing.Point(72, 19);
            this.previewBox.Name = "previewBox";
            this.previewBox.Size = new System.Drawing.Size(117, 95);
            this.previewBox.TabIndex = 1;
            this.previewBox.TabStop = false;
            // 
            // vehicleListBox
            // 
            this.vehicleListBox.FormattingEnabled = true;
            this.vehicleListBox.Location = new System.Drawing.Point(6, 19);
            this.vehicleListBox.Name = "vehicleListBox";
            this.vehicleListBox.Size = new System.Drawing.Size(60, 95);
            this.vehicleListBox.TabIndex = 2;
            this.vehicleListBox.SelectedIndexChanged += new System.EventHandler(this.vehicleListBox_SelectedIndexChanged);
            // 
            // editVehicleButton
            // 
            this.editVehicleButton.Location = new System.Drawing.Point(6, 120);
            this.editVehicleButton.Name = "editVehicleButton";
            this.editVehicleButton.Size = new System.Drawing.Size(183, 23);
            this.editVehicleButton.TabIndex = 3;
            this.editVehicleButton.Text = "Edit Vehicle";
            this.editVehicleButton.UseVisualStyleBackColor = true;
            this.editVehicleButton.Click += new System.EventHandler(this.editVehicleButton_Click);
            // 
            // objectsGroupBox
            // 
            this.objectsGroupBox.Controls.Add(this.vehicleListBox);
            this.objectsGroupBox.Controls.Add(this.editVehicleButton);
            this.objectsGroupBox.Controls.Add(this.previewBox);
            this.objectsGroupBox.Location = new System.Drawing.Point(9, 41);
            this.objectsGroupBox.Name = "objectsGroupBox";
            this.objectsGroupBox.Size = new System.Drawing.Size(197, 153);
            this.objectsGroupBox.TabIndex = 4;
            this.objectsGroupBox.TabStop = false;
            this.objectsGroupBox.Text = "Vehicles";
            // 
            // VehicleEditor
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(218, 205);
            this.Controls.Add(this.objectsGroupBox);
            this.Controls.Add(this.addVehicleButton);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "VehicleEditor";
            this.ShowIcon = false;
            this.ShowInTaskbar = false;
            this.SizeGripStyle = System.Windows.Forms.SizeGripStyle.Hide;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "Vehicle Editor";
            this.Load += new System.EventHandler(this.VehicleEditor_Load);
            ((System.ComponentModel.ISupportInitialize)(this.previewBox)).EndInit();
            this.objectsGroupBox.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button addVehicleButton;
        private System.Windows.Forms.PictureBox previewBox;
        private System.Windows.Forms.ListBox vehicleListBox;
        private System.Windows.Forms.Button editVehicleButton;
        private System.Windows.Forms.GroupBox objectsGroupBox;
    }
}