namespace MapEditor2
{
    partial class MapPropertiesDialog
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
            this.tileWidthTextBox = new System.Windows.Forms.TextBox();
            this.mapWidthTextBox = new System.Windows.Forms.TextBox();
            this.tileHeightTextBox = new System.Windows.Forms.TextBox();
            this.mapHeightTextBox = new System.Windows.Forms.TextBox();
            this.pathTextBox = new System.Windows.Forms.TextBox();
            this.sourceFileGroupBox = new System.Windows.Forms.GroupBox();
            this.browseButton = new System.Windows.Forms.Button();
            this.mapOptionsGroupBox = new System.Windows.Forms.GroupBox();
            this.label1 = new System.Windows.Forms.Label();
            this.baseTilesTextBox = new System.Windows.Forms.TextBox();
            this.baseTilesLabel = new System.Windows.Forms.Label();
            this.tileHeightMultiplyerLabel = new System.Windows.Forms.Label();
            this.tileWidthMultiplyerLabel = new System.Windows.Forms.Label();
            this.mapHeightLabel = new System.Windows.Forms.Label();
            this.mapWidthLabel = new System.Windows.Forms.Label();
            this.tileHeightLabel = new System.Windows.Forms.Label();
            this.tileWidthLabel = new System.Windows.Forms.Label();
            this.nameLabel = new System.Windows.Forms.Label();
            this.nameTextBox = new System.Windows.Forms.TextBox();
            this.bitmapPreviewGroupBox = new System.Windows.Forms.GroupBox();
            this.heightValueLabel = new System.Windows.Forms.Label();
            this.widthValueLabel = new System.Windows.Forms.Label();
            this.heightLabel = new System.Windows.Forms.Label();
            this.widthLabel = new System.Windows.Forms.Label();
            this.bitmapPreviewPictureBox = new System.Windows.Forms.PictureBox();
            this.sourceFileGroupBox.SuspendLayout();
            this.mapOptionsGroupBox.SuspendLayout();
            this.bitmapPreviewGroupBox.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.bitmapPreviewPictureBox)).BeginInit();
            this.SuspendLayout();
            // 
            // okButton
            // 
            this.okButton.DialogResult = System.Windows.Forms.DialogResult.OK;
            this.okButton.Enabled = false;
            this.okButton.Location = new System.Drawing.Point(204, 244);
            this.okButton.Name = "okButton";
            this.okButton.Size = new System.Drawing.Size(75, 23);
            this.okButton.TabIndex = 7;
            this.okButton.Text = "Ok";
            this.okButton.UseVisualStyleBackColor = true;
            // 
            // cancelButton
            // 
            this.cancelButton.DialogResult = System.Windows.Forms.DialogResult.Cancel;
            this.cancelButton.Location = new System.Drawing.Point(285, 244);
            this.cancelButton.Name = "cancelButton";
            this.cancelButton.Size = new System.Drawing.Size(75, 23);
            this.cancelButton.TabIndex = 8;
            this.cancelButton.Text = "Cancel";
            this.cancelButton.UseVisualStyleBackColor = true;
            // 
            // tileWidthTextBox
            // 
            this.tileWidthTextBox.Location = new System.Drawing.Point(76, 42);
            this.tileWidthTextBox.MaxLength = 6;
            this.tileWidthTextBox.Name = "tileWidthTextBox";
            this.tileWidthTextBox.Size = new System.Drawing.Size(44, 20);
            this.tileWidthTextBox.TabIndex = 1;
            // 
            // mapWidthTextBox
            // 
            this.mapWidthTextBox.Location = new System.Drawing.Point(76, 94);
            this.mapWidthTextBox.MaxLength = 6;
            this.mapWidthTextBox.Name = "mapWidthTextBox";
            this.mapWidthTextBox.Size = new System.Drawing.Size(44, 20);
            this.mapWidthTextBox.TabIndex = 3;
            // 
            // tileHeightTextBox
            // 
            this.tileHeightTextBox.Location = new System.Drawing.Point(76, 68);
            this.tileHeightTextBox.MaxLength = 6;
            this.tileHeightTextBox.Name = "tileHeightTextBox";
            this.tileHeightTextBox.Size = new System.Drawing.Size(44, 20);
            this.tileHeightTextBox.TabIndex = 2;
            // 
            // mapHeightTextBox
            // 
            this.mapHeightTextBox.Location = new System.Drawing.Point(76, 120);
            this.mapHeightTextBox.MaxLength = 6;
            this.mapHeightTextBox.Name = "mapHeightTextBox";
            this.mapHeightTextBox.Size = new System.Drawing.Size(44, 20);
            this.mapHeightTextBox.TabIndex = 4;
            // 
            // pathTextBox
            // 
            this.pathTextBox.Enabled = false;
            this.pathTextBox.Location = new System.Drawing.Point(6, 15);
            this.pathTextBox.Name = "pathTextBox";
            this.pathTextBox.Size = new System.Drawing.Size(266, 20);
            this.pathTextBox.TabIndex = 6;
            // 
            // sourceFileGroupBox
            // 
            this.sourceFileGroupBox.Controls.Add(this.browseButton);
            this.sourceFileGroupBox.Controls.Add(this.pathTextBox);
            this.sourceFileGroupBox.Location = new System.Drawing.Point(7, 197);
            this.sourceFileGroupBox.Name = "sourceFileGroupBox";
            this.sourceFileGroupBox.Size = new System.Drawing.Size(359, 41);
            this.sourceFileGroupBox.TabIndex = 7;
            this.sourceFileGroupBox.TabStop = false;
            this.sourceFileGroupBox.Text = "Source file";
            // 
            // browseButton
            // 
            this.browseButton.Location = new System.Drawing.Point(278, 12);
            this.browseButton.Name = "browseButton";
            this.browseButton.Size = new System.Drawing.Size(75, 23);
            this.browseButton.TabIndex = 6;
            this.browseButton.Text = "Browse...";
            this.browseButton.UseVisualStyleBackColor = true;
            this.browseButton.Click += new System.EventHandler(this.browseButton_Click);
            // 
            // mapOptionsGroupBox
            // 
            this.mapOptionsGroupBox.Controls.Add(this.label1);
            this.mapOptionsGroupBox.Controls.Add(this.baseTilesTextBox);
            this.mapOptionsGroupBox.Controls.Add(this.baseTilesLabel);
            this.mapOptionsGroupBox.Controls.Add(this.tileHeightMultiplyerLabel);
            this.mapOptionsGroupBox.Controls.Add(this.tileWidthMultiplyerLabel);
            this.mapOptionsGroupBox.Controls.Add(this.mapHeightLabel);
            this.mapOptionsGroupBox.Controls.Add(this.mapWidthLabel);
            this.mapOptionsGroupBox.Controls.Add(this.tileHeightLabel);
            this.mapOptionsGroupBox.Controls.Add(this.tileWidthLabel);
            this.mapOptionsGroupBox.Controls.Add(this.nameLabel);
            this.mapOptionsGroupBox.Controls.Add(this.nameTextBox);
            this.mapOptionsGroupBox.Controls.Add(this.tileWidthTextBox);
            this.mapOptionsGroupBox.Controls.Add(this.tileHeightTextBox);
            this.mapOptionsGroupBox.Controls.Add(this.mapHeightTextBox);
            this.mapOptionsGroupBox.Controls.Add(this.mapWidthTextBox);
            this.mapOptionsGroupBox.Location = new System.Drawing.Point(6, 12);
            this.mapOptionsGroupBox.Name = "mapOptionsGroupBox";
            this.mapOptionsGroupBox.Size = new System.Drawing.Size(205, 179);
            this.mapOptionsGroupBox.TabIndex = 8;
            this.mapOptionsGroupBox.TabStop = false;
            this.mapOptionsGroupBox.Text = "Map options";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(126, 153);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(22, 13);
            this.label1.TabIndex = 15;
            this.label1.Text = "^ 2";
            // 
            // baseTilesTextBox
            // 
            this.baseTilesTextBox.Location = new System.Drawing.Point(76, 146);
            this.baseTilesTextBox.MaxLength = 6;
            this.baseTilesTextBox.Name = "baseTilesTextBox";
            this.baseTilesTextBox.Size = new System.Drawing.Size(44, 20);
            this.baseTilesTextBox.TabIndex = 5;
            this.baseTilesTextBox.Text = "1";
            // 
            // baseTilesLabel
            // 
            this.baseTilesLabel.AutoSize = true;
            this.baseTilesLabel.Location = new System.Drawing.Point(3, 153);
            this.baseTilesLabel.Name = "baseTilesLabel";
            this.baseTilesLabel.Size = new System.Drawing.Size(55, 13);
            this.baseTilesLabel.TabIndex = 14;
            this.baseTilesLabel.Text = "Base tiles:";
            // 
            // tileHeightMultiplyerLabel
            // 
            this.tileHeightMultiplyerLabel.AutoSize = true;
            this.tileHeightMultiplyerLabel.Location = new System.Drawing.Point(126, 127);
            this.tileHeightMultiplyerLabel.Name = "tileHeightMultiplyerLabel";
            this.tileHeightMultiplyerLabel.Size = new System.Drawing.Size(64, 13);
            this.tileHeightMultiplyerLabel.TabIndex = 13;
            this.tileHeightMultiplyerLabel.Text = "x Tile height";
            // 
            // tileWidthMultiplyerLabel
            // 
            this.tileWidthMultiplyerLabel.AutoSize = true;
            this.tileWidthMultiplyerLabel.Location = new System.Drawing.Point(126, 101);
            this.tileWidthMultiplyerLabel.Name = "tileWidthMultiplyerLabel";
            this.tileWidthMultiplyerLabel.Size = new System.Drawing.Size(60, 13);
            this.tileWidthMultiplyerLabel.TabIndex = 12;
            this.tileWidthMultiplyerLabel.Text = "x Tile width";
            // 
            // mapHeightLabel
            // 
            this.mapHeightLabel.AutoSize = true;
            this.mapHeightLabel.Location = new System.Drawing.Point(3, 127);
            this.mapHeightLabel.Name = "mapHeightLabel";
            this.mapHeightLabel.Size = new System.Drawing.Size(63, 13);
            this.mapHeightLabel.TabIndex = 11;
            this.mapHeightLabel.Text = "Map height:";
            // 
            // mapWidthLabel
            // 
            this.mapWidthLabel.AutoSize = true;
            this.mapWidthLabel.Location = new System.Drawing.Point(3, 101);
            this.mapWidthLabel.Name = "mapWidthLabel";
            this.mapWidthLabel.Size = new System.Drawing.Size(59, 13);
            this.mapWidthLabel.TabIndex = 10;
            this.mapWidthLabel.Text = "Map width:";
            // 
            // tileHeightLabel
            // 
            this.tileHeightLabel.AutoSize = true;
            this.tileHeightLabel.Location = new System.Drawing.Point(3, 75);
            this.tileHeightLabel.Name = "tileHeightLabel";
            this.tileHeightLabel.Size = new System.Drawing.Size(59, 13);
            this.tileHeightLabel.TabIndex = 9;
            this.tileHeightLabel.Text = "Tile height:";
            // 
            // tileWidthLabel
            // 
            this.tileWidthLabel.AutoSize = true;
            this.tileWidthLabel.Location = new System.Drawing.Point(3, 49);
            this.tileWidthLabel.Name = "tileWidthLabel";
            this.tileWidthLabel.Size = new System.Drawing.Size(55, 13);
            this.tileWidthLabel.TabIndex = 8;
            this.tileWidthLabel.Text = "Tile width:";
            // 
            // nameLabel
            // 
            this.nameLabel.AutoSize = true;
            this.nameLabel.Location = new System.Drawing.Point(3, 23);
            this.nameLabel.Name = "nameLabel";
            this.nameLabel.Size = new System.Drawing.Size(38, 13);
            this.nameLabel.TabIndex = 7;
            this.nameLabel.Text = "Name:";
            // 
            // nameTextBox
            // 
            this.nameTextBox.AccessibleDescription = "";
            this.nameTextBox.Location = new System.Drawing.Point(76, 16);
            this.nameTextBox.MaxLength = 64;
            this.nameTextBox.Name = "nameTextBox";
            this.nameTextBox.Size = new System.Drawing.Size(122, 20);
            this.nameTextBox.TabIndex = 0;
            // 
            // bitmapPreviewGroupBox
            // 
            this.bitmapPreviewGroupBox.Controls.Add(this.heightValueLabel);
            this.bitmapPreviewGroupBox.Controls.Add(this.widthValueLabel);
            this.bitmapPreviewGroupBox.Controls.Add(this.heightLabel);
            this.bitmapPreviewGroupBox.Controls.Add(this.widthLabel);
            this.bitmapPreviewGroupBox.Controls.Add(this.bitmapPreviewPictureBox);
            this.bitmapPreviewGroupBox.Location = new System.Drawing.Point(217, 12);
            this.bitmapPreviewGroupBox.Name = "bitmapPreviewGroupBox";
            this.bitmapPreviewGroupBox.Size = new System.Drawing.Size(148, 179);
            this.bitmapPreviewGroupBox.TabIndex = 9;
            this.bitmapPreviewGroupBox.TabStop = false;
            this.bitmapPreviewGroupBox.Text = "Bitmap preview";
            // 
            // heightValueLabel
            // 
            this.heightValueLabel.AutoSize = true;
            this.heightValueLabel.Location = new System.Drawing.Point(47, 163);
            this.heightValueLabel.Name = "heightValueLabel";
            this.heightValueLabel.Size = new System.Drawing.Size(0, 13);
            this.heightValueLabel.TabIndex = 4;
            // 
            // widthValueLabel
            // 
            this.widthValueLabel.AutoSize = true;
            this.widthValueLabel.Location = new System.Drawing.Point(47, 146);
            this.widthValueLabel.Name = "widthValueLabel";
            this.widthValueLabel.Size = new System.Drawing.Size(0, 13);
            this.widthValueLabel.TabIndex = 3;
            // 
            // heightLabel
            // 
            this.heightLabel.AutoSize = true;
            this.heightLabel.Location = new System.Drawing.Point(6, 163);
            this.heightLabel.Name = "heightLabel";
            this.heightLabel.Size = new System.Drawing.Size(41, 13);
            this.heightLabel.TabIndex = 2;
            this.heightLabel.Text = "Height:";
            // 
            // widthLabel
            // 
            this.widthLabel.AutoSize = true;
            this.widthLabel.Location = new System.Drawing.Point(6, 146);
            this.widthLabel.Name = "widthLabel";
            this.widthLabel.Size = new System.Drawing.Size(38, 13);
            this.widthLabel.TabIndex = 1;
            this.widthLabel.Text = "Width:";
            // 
            // bitmapPreviewPictureBox
            // 
            this.bitmapPreviewPictureBox.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.bitmapPreviewPictureBox.Location = new System.Drawing.Point(3, 16);
            this.bitmapPreviewPictureBox.Name = "bitmapPreviewPictureBox";
            this.bitmapPreviewPictureBox.Size = new System.Drawing.Size(142, 124);
            this.bitmapPreviewPictureBox.TabIndex = 0;
            this.bitmapPreviewPictureBox.TabStop = false;
            // 
            // MapPropertiesDialog
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(372, 279);
            this.Controls.Add(this.bitmapPreviewGroupBox);
            this.Controls.Add(this.mapOptionsGroupBox);
            this.Controls.Add(this.sourceFileGroupBox);
            this.Controls.Add(this.cancelButton);
            this.Controls.Add(this.okButton);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "MapPropertiesDialog";
            this.ShowIcon = false;
            this.ShowInTaskbar = false;
            this.SizeGripStyle = System.Windows.Forms.SizeGripStyle.Hide;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "Map properties...";
            this.sourceFileGroupBox.ResumeLayout(false);
            this.sourceFileGroupBox.PerformLayout();
            this.mapOptionsGroupBox.ResumeLayout(false);
            this.mapOptionsGroupBox.PerformLayout();
            this.bitmapPreviewGroupBox.ResumeLayout(false);
            this.bitmapPreviewGroupBox.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.bitmapPreviewPictureBox)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button okButton;
        private System.Windows.Forms.Button cancelButton;
        private System.Windows.Forms.TextBox tileWidthTextBox;
        private System.Windows.Forms.TextBox mapWidthTextBox;
        private System.Windows.Forms.TextBox tileHeightTextBox;
        private System.Windows.Forms.TextBox mapHeightTextBox;
        private System.Windows.Forms.TextBox pathTextBox;
        private System.Windows.Forms.GroupBox sourceFileGroupBox;
        private System.Windows.Forms.Button browseButton;
        private System.Windows.Forms.GroupBox mapOptionsGroupBox;
        private System.Windows.Forms.Label tileWidthLabel;
        private System.Windows.Forms.Label nameLabel;
        private System.Windows.Forms.TextBox nameTextBox;
        private System.Windows.Forms.Label mapHeightLabel;
        private System.Windows.Forms.Label mapWidthLabel;
        private System.Windows.Forms.Label tileHeightLabel;
        private System.Windows.Forms.GroupBox bitmapPreviewGroupBox;
        private System.Windows.Forms.PictureBox bitmapPreviewPictureBox;
        private System.Windows.Forms.Label tileHeightMultiplyerLabel;
        private System.Windows.Forms.Label tileWidthMultiplyerLabel;
        private System.Windows.Forms.Label heightValueLabel;
        private System.Windows.Forms.Label widthValueLabel;
        private System.Windows.Forms.Label heightLabel;
        private System.Windows.Forms.Label widthLabel;
        private System.Windows.Forms.TextBox baseTilesTextBox;
        private System.Windows.Forms.Label baseTilesLabel;
        private System.Windows.Forms.Label label1;
    }
}