namespace MapEditor2
{
    partial class VehiclePropertiesDialog
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
            this.okButton = new System.Windows.Forms.Button();
            this.cancelButton = new System.Windows.Forms.Button();
            this.accelerationLabel = new System.Windows.Forms.Label();
            this.maxSpeedLabel = new System.Windows.Forms.Label();
            this.maxSpeedTextBox = new System.Windows.Forms.TextBox();
            this.manevrabilityLabel = new System.Windows.Forms.Label();
            this.armorLabel = new System.Windows.Forms.Label();
            this.hitPointsLabel = new System.Windows.Forms.Label();
            this.manevrabilityTextBox = new System.Windows.Forms.TextBox();
            this.armorTextBox = new System.Windows.Forms.TextBox();
            this.hitPointsTextBox = new System.Windows.Forms.TextBox();
            this.addFramesButton = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.previewBox = new System.Windows.Forms.PictureBox();
            this.numberOfFramesLabel = new System.Windows.Forms.Label();
            this.removeFramesButton = new System.Windows.Forms.Button();
            this.removeLastFrameButton = new System.Windows.Forms.Button();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.nameTextBox = new System.Windows.Forms.TextBox();
            this.nameLabel = new System.Windows.Forms.Label();
            this.accelerationTextBox = new System.Windows.Forms.TextBox();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.label2 = new System.Windows.Forms.Label();
            this.setPreviewBitmapButton = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.previewBox)).BeginInit();
            this.groupBox1.SuspendLayout();
            this.groupBox2.SuspendLayout();
            this.SuspendLayout();
            // 
            // okButton
            // 
            this.okButton.DialogResult = System.Windows.Forms.DialogResult.OK;
            this.okButton.Enabled = false;
            this.okButton.Location = new System.Drawing.Point(270, 142);
            this.okButton.Name = "okButton";
            this.okButton.Size = new System.Drawing.Size(75, 23);
            this.okButton.TabIndex = 9;
            this.okButton.Text = "Ok";
            this.okButton.UseVisualStyleBackColor = true;
            // 
            // cancelButton
            // 
            this.cancelButton.DialogResult = System.Windows.Forms.DialogResult.Cancel;
            this.cancelButton.Location = new System.Drawing.Point(351, 142);
            this.cancelButton.Name = "cancelButton";
            this.cancelButton.Size = new System.Drawing.Size(75, 23);
            this.cancelButton.TabIndex = 10;
            this.cancelButton.Text = "Cancel";
            this.cancelButton.UseVisualStyleBackColor = true;
            // 
            // accelerationLabel
            // 
            this.accelerationLabel.AutoSize = true;
            this.accelerationLabel.Location = new System.Drawing.Point(4, 39);
            this.accelerationLabel.Name = "accelerationLabel";
            this.accelerationLabel.Size = new System.Drawing.Size(69, 13);
            this.accelerationLabel.TabIndex = 3;
            this.accelerationLabel.Text = "Acceleration:";
            // 
            // maxSpeedLabel
            // 
            this.maxSpeedLabel.AutoSize = true;
            this.maxSpeedLabel.Location = new System.Drawing.Point(4, 66);
            this.maxSpeedLabel.Name = "maxSpeedLabel";
            this.maxSpeedLabel.Size = new System.Drawing.Size(64, 13);
            this.maxSpeedLabel.TabIndex = 4;
            this.maxSpeedLabel.Text = "Max Speed:";
            // 
            // maxSpeedTextBox
            // 
            this.maxSpeedTextBox.Location = new System.Drawing.Point(79, 63);
            this.maxSpeedTextBox.MaxLength = 6;
            this.maxSpeedTextBox.Name = "maxSpeedTextBox";
            this.maxSpeedTextBox.Size = new System.Drawing.Size(50, 20);
            this.maxSpeedTextBox.TabIndex = 2;
            // 
            // manevrabilityLabel
            // 
            this.manevrabilityLabel.AutoSize = true;
            this.manevrabilityLabel.Location = new System.Drawing.Point(4, 93);
            this.manevrabilityLabel.Name = "manevrabilityLabel";
            this.manevrabilityLabel.Size = new System.Drawing.Size(72, 13);
            this.manevrabilityLabel.TabIndex = 6;
            this.manevrabilityLabel.Text = "Manevrability:";
            // 
            // armorLabel
            // 
            this.armorLabel.AutoSize = true;
            this.armorLabel.Location = new System.Drawing.Point(3, 120);
            this.armorLabel.Name = "armorLabel";
            this.armorLabel.Size = new System.Drawing.Size(37, 13);
            this.armorLabel.TabIndex = 7;
            this.armorLabel.Text = "Armor:";
            // 
            // hitPointsLabel
            // 
            this.hitPointsLabel.AutoSize = true;
            this.hitPointsLabel.Location = new System.Drawing.Point(4, 147);
            this.hitPointsLabel.Name = "hitPointsLabel";
            this.hitPointsLabel.Size = new System.Drawing.Size(55, 13);
            this.hitPointsLabel.TabIndex = 8;
            this.hitPointsLabel.Text = "Hit Points:";
            // 
            // manevrabilityTextBox
            // 
            this.manevrabilityTextBox.Location = new System.Drawing.Point(79, 90);
            this.manevrabilityTextBox.MaxLength = 6;
            this.manevrabilityTextBox.Name = "manevrabilityTextBox";
            this.manevrabilityTextBox.Size = new System.Drawing.Size(50, 20);
            this.manevrabilityTextBox.TabIndex = 3;
            // 
            // armorTextBox
            // 
            this.armorTextBox.Location = new System.Drawing.Point(79, 116);
            this.armorTextBox.MaxLength = 6;
            this.armorTextBox.Name = "armorTextBox";
            this.armorTextBox.Size = new System.Drawing.Size(50, 20);
            this.armorTextBox.TabIndex = 4;
            // 
            // hitPointsTextBox
            // 
            this.hitPointsTextBox.Location = new System.Drawing.Point(79, 143);
            this.hitPointsTextBox.MaxLength = 6;
            this.hitPointsTextBox.Name = "hitPointsTextBox";
            this.hitPointsTextBox.Size = new System.Drawing.Size(50, 20);
            this.hitPointsTextBox.TabIndex = 5;
            // 
            // addFramesButton
            // 
            this.addFramesButton.Location = new System.Drawing.Point(20, 9);
            this.addFramesButton.Name = "addFramesButton";
            this.addFramesButton.Size = new System.Drawing.Size(117, 23);
            this.addFramesButton.TabIndex = 6;
            this.addFramesButton.Text = "Add Frames";
            this.addFramesButton.UseVisualStyleBackColor = true;
            this.addFramesButton.Click += new System.EventHandler(this.addFramesButton_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(17, 113);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(75, 13);
            this.label1.TabIndex = 13;
            this.label1.Text = "Total Frames*:";
            // 
            // previewBox
            // 
            this.previewBox.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.previewBox.Location = new System.Drawing.Point(140, 36);
            this.previewBox.Name = "previewBox";
            this.previewBox.Size = new System.Drawing.Size(121, 123);
            this.previewBox.TabIndex = 15;
            this.previewBox.TabStop = false;
            // 
            // numberOfFramesLabel
            // 
            this.numberOfFramesLabel.AutoSize = true;
            this.numberOfFramesLabel.Location = new System.Drawing.Point(88, 113);
            this.numberOfFramesLabel.Name = "numberOfFramesLabel";
            this.numberOfFramesLabel.Size = new System.Drawing.Size(13, 13);
            this.numberOfFramesLabel.TabIndex = 0;
            this.numberOfFramesLabel.Text = "0";
            // 
            // removeFramesButton
            // 
            this.removeFramesButton.Location = new System.Drawing.Point(20, 34);
            this.removeFramesButton.Name = "removeFramesButton";
            this.removeFramesButton.Size = new System.Drawing.Size(117, 23);
            this.removeFramesButton.TabIndex = 7;
            this.removeFramesButton.Text = "Remove Frames";
            this.removeFramesButton.UseVisualStyleBackColor = true;
            this.removeFramesButton.Click += new System.EventHandler(this.removeFramesButton_Click);
            // 
            // removeLastFrameButton
            // 
            this.removeLastFrameButton.Location = new System.Drawing.Point(20, 59);
            this.removeLastFrameButton.Name = "removeLastFrameButton";
            this.removeLastFrameButton.Size = new System.Drawing.Size(117, 23);
            this.removeLastFrameButton.TabIndex = 8;
            this.removeLastFrameButton.Text = "Remove Last Frame";
            this.removeLastFrameButton.UseVisualStyleBackColor = true;
            this.removeLastFrameButton.Click += new System.EventHandler(this.removeLastFrameButton_Click);
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.nameTextBox);
            this.groupBox1.Controls.Add(this.nameLabel);
            this.groupBox1.Controls.Add(this.accelerationTextBox);
            this.groupBox1.Controls.Add(this.manevrabilityLabel);
            this.groupBox1.Controls.Add(this.accelerationLabel);
            this.groupBox1.Controls.Add(this.maxSpeedLabel);
            this.groupBox1.Controls.Add(this.previewBox);
            this.groupBox1.Controls.Add(this.maxSpeedTextBox);
            this.groupBox1.Controls.Add(this.armorLabel);
            this.groupBox1.Controls.Add(this.hitPointsLabel);
            this.groupBox1.Controls.Add(this.hitPointsTextBox);
            this.groupBox1.Controls.Add(this.manevrabilityTextBox);
            this.groupBox1.Controls.Add(this.armorTextBox);
            this.groupBox1.Location = new System.Drawing.Point(3, -3);
            this.groupBox1.Margin = new System.Windows.Forms.Padding(0);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Padding = new System.Windows.Forms.Padding(0);
            this.groupBox1.Size = new System.Drawing.Size(264, 168);
            this.groupBox1.TabIndex = 18;
            this.groupBox1.TabStop = false;
            // 
            // nameTextBox
            // 
            this.nameTextBox.Location = new System.Drawing.Point(79, 9);
            this.nameTextBox.MaxLength = 32;
            this.nameTextBox.Name = "nameTextBox";
            this.nameTextBox.Size = new System.Drawing.Size(182, 20);
            this.nameTextBox.TabIndex = 0;
            // 
            // nameLabel
            // 
            this.nameLabel.AutoSize = true;
            this.nameLabel.Location = new System.Drawing.Point(4, 12);
            this.nameLabel.Name = "nameLabel";
            this.nameLabel.Size = new System.Drawing.Size(38, 13);
            this.nameLabel.TabIndex = 17;
            this.nameLabel.Text = "Name:";
            // 
            // accelerationTextBox
            // 
            this.accelerationTextBox.Location = new System.Drawing.Point(79, 36);
            this.accelerationTextBox.MaxLength = 6;
            this.accelerationTextBox.Name = "accelerationTextBox";
            this.accelerationTextBox.Size = new System.Drawing.Size(50, 20);
            this.accelerationTextBox.TabIndex = 1;
            // 
            // groupBox2
            // 
            this.groupBox2.Controls.Add(this.label2);
            this.groupBox2.Controls.Add(this.setPreviewBitmapButton);
            this.groupBox2.Controls.Add(this.addFramesButton);
            this.groupBox2.Controls.Add(this.label1);
            this.groupBox2.Controls.Add(this.removeLastFrameButton);
            this.groupBox2.Controls.Add(this.numberOfFramesLabel);
            this.groupBox2.Controls.Add(this.removeFramesButton);
            this.groupBox2.Location = new System.Drawing.Point(270, -3);
            this.groupBox2.Margin = new System.Windows.Forms.Padding(0);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Padding = new System.Windows.Forms.Padding(0);
            this.groupBox2.Size = new System.Drawing.Size(156, 142);
            this.groupBox2.TabIndex = 19;
            this.groupBox2.TabStop = false;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Font = new System.Drawing.Font("Lucida Sans", 5.5F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label2.Location = new System.Drawing.Point(88, 129);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(60, 12);
            this.label2.TabIndex = 15;
            this.label2.Text = "*Must be 16.";
            // 
            // setPreviewBitmapButton
            // 
            this.setPreviewBitmapButton.Location = new System.Drawing.Point(20, 84);
            this.setPreviewBitmapButton.Name = "setPreviewBitmapButton";
            this.setPreviewBitmapButton.Size = new System.Drawing.Size(117, 23);
            this.setPreviewBitmapButton.TabIndex = 14;
            this.setPreviewBitmapButton.Text = "Set Preview Bitmap";
            this.setPreviewBitmapButton.UseVisualStyleBackColor = true;
            this.setPreviewBitmapButton.Click += new System.EventHandler(this.setPreviewBitmapButton_Click);
            // 
            // VehiclePropertiesDialog
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(430, 168);
            this.Controls.Add(this.groupBox2);
            this.Controls.Add(this.groupBox1);
            this.Controls.Add(this.cancelButton);
            this.Controls.Add(this.okButton);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "VehiclePropertiesDialog";
            this.ShowIcon = false;
            this.ShowInTaskbar = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "Vehicle Properties...";
            this.TopMost = true;
            this.Load += new System.EventHandler(this.VehiclePropertiesDialog_Load);
            ((System.ComponentModel.ISupportInitialize)(this.previewBox)).EndInit();
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.groupBox2.ResumeLayout(false);
            this.groupBox2.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button okButton;
        private System.Windows.Forms.Button cancelButton;
        private System.Windows.Forms.Label accelerationLabel;
        private System.Windows.Forms.Label maxSpeedLabel;
        private System.Windows.Forms.TextBox maxSpeedTextBox;
        private System.Windows.Forms.Label manevrabilityLabel;
        private System.Windows.Forms.Label armorLabel;
        private System.Windows.Forms.Label hitPointsLabel;
        private System.Windows.Forms.TextBox manevrabilityTextBox;
        private System.Windows.Forms.TextBox armorTextBox;
        private System.Windows.Forms.TextBox hitPointsTextBox;
        private System.Windows.Forms.Button addFramesButton;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.PictureBox previewBox;
        private System.Windows.Forms.Label numberOfFramesLabel;
        private System.Windows.Forms.Button removeFramesButton;
        private System.Windows.Forms.Button removeLastFrameButton;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.TextBox accelerationTextBox;
        private System.Windows.Forms.Button setPreviewBitmapButton;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.TextBox nameTextBox;
        private System.Windows.Forms.Label nameLabel;
    }
}