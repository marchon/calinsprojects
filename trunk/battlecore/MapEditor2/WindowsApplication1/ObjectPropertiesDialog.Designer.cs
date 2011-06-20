namespace MapEditor2
{
    partial class ObjectPropertiesDialog
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
            this.typeChooser = new System.Windows.Forms.ComboBox();
            this.Type = new System.Windows.Forms.Label();
            this.Delay = new System.Windows.Forms.Label();
            this.delayBox = new System.Windows.Forms.TextBox();
            this.labelDimX = new System.Windows.Forms.Label();
            this.labelDimY = new System.Windows.Forms.Label();
            this.labelHeight = new System.Windows.Forms.Label();
            this.dimXBox = new System.Windows.Forms.TextBox();
            this.dimYBox = new System.Windows.Forms.TextBox();
            this.heightBox = new System.Windows.Forms.TextBox();
            this.addFramesButton = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.previewBox = new System.Windows.Forms.PictureBox();
            this.numberOfFramesLabel = new System.Windows.Forms.Label();
            this.removeFramesButton = new System.Windows.Forms.Button();
            this.removeLastFrameButton = new System.Windows.Forms.Button();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            ((System.ComponentModel.ISupportInitialize)(this.previewBox)).BeginInit();
            this.groupBox1.SuspendLayout();
            this.groupBox2.SuspendLayout();
            this.SuspendLayout();
            // 
            // okButton
            // 
            this.okButton.DialogResult = System.Windows.Forms.DialogResult.OK;
            this.okButton.Enabled = false;
            this.okButton.Location = new System.Drawing.Point(270, 131);
            this.okButton.Name = "okButton";
            this.okButton.Size = new System.Drawing.Size(75, 23);
            this.okButton.TabIndex = 7;
            this.okButton.Text = "Ok";
            this.okButton.UseVisualStyleBackColor = true;
            // 
            // cancelButton
            // 
            this.cancelButton.DialogResult = System.Windows.Forms.DialogResult.Cancel;
            this.cancelButton.Location = new System.Drawing.Point(351, 131);
            this.cancelButton.Name = "cancelButton";
            this.cancelButton.Size = new System.Drawing.Size(75, 23);
            this.cancelButton.TabIndex = 8;
            this.cancelButton.Text = "Cancel";
            this.cancelButton.UseVisualStyleBackColor = true;
            // 
            // typeChooser
            // 
            this.typeChooser.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.typeChooser.FormattingEnabled = true;
            this.typeChooser.Items.AddRange(new object[] {
            "Uncollidable",
            "Undestroyable",
            "Destroyable"});
            this.typeChooser.Location = new System.Drawing.Point(80, 11);
            this.typeChooser.Name = "typeChooser";
            this.typeChooser.Size = new System.Drawing.Size(176, 21);
            this.typeChooser.TabIndex = 1;
            // 
            // Type
            // 
            this.Type.AutoSize = true;
            this.Type.Location = new System.Drawing.Point(4, 16);
            this.Type.Name = "Type";
            this.Type.Size = new System.Drawing.Size(34, 13);
            this.Type.TabIndex = 3;
            this.Type.Text = "Type:";
            // 
            // Delay
            // 
            this.Delay.AutoSize = true;
            this.Delay.Location = new System.Drawing.Point(4, 46);
            this.Delay.Name = "Delay";
            this.Delay.Size = new System.Drawing.Size(37, 13);
            this.Delay.TabIndex = 4;
            this.Delay.Text = "Delay:";
            // 
            // delayBox
            // 
            this.delayBox.Location = new System.Drawing.Point(80, 42);
            this.delayBox.MaxLength = 6;
            this.delayBox.Name = "delayBox";
            this.delayBox.Size = new System.Drawing.Size(50, 20);
            this.delayBox.TabIndex = 2;
            // 
            // labelDimX
            // 
            this.labelDimX.AutoSize = true;
            this.labelDimX.Location = new System.Drawing.Point(4, 76);
            this.labelDimX.Name = "labelDimX";
            this.labelDimX.Size = new System.Drawing.Size(69, 13);
            this.labelDimX.TabIndex = 6;
            this.labelDimX.Text = "Dimension X:";
            // 
            // labelDimY
            // 
            this.labelDimY.AutoSize = true;
            this.labelDimY.Location = new System.Drawing.Point(3, 106);
            this.labelDimY.Name = "labelDimY";
            this.labelDimY.Size = new System.Drawing.Size(69, 13);
            this.labelDimY.TabIndex = 7;
            this.labelDimY.Text = "Dimension Y:";
            // 
            // labelHeight
            // 
            this.labelHeight.AutoSize = true;
            this.labelHeight.Location = new System.Drawing.Point(4, 136);
            this.labelHeight.Name = "labelHeight";
            this.labelHeight.Size = new System.Drawing.Size(41, 13);
            this.labelHeight.TabIndex = 8;
            this.labelHeight.Text = "Height:";
            // 
            // dimXBox
            // 
            this.dimXBox.Location = new System.Drawing.Point(79, 72);
            this.dimXBox.MaxLength = 6;
            this.dimXBox.Name = "dimXBox";
            this.dimXBox.Size = new System.Drawing.Size(50, 20);
            this.dimXBox.TabIndex = 3;
            // 
            // dimYBox
            // 
            this.dimYBox.Location = new System.Drawing.Point(79, 102);
            this.dimYBox.MaxLength = 6;
            this.dimYBox.Name = "dimYBox";
            this.dimYBox.Size = new System.Drawing.Size(50, 20);
            this.dimYBox.TabIndex = 4;
            // 
            // heightBox
            // 
            this.heightBox.Location = new System.Drawing.Point(79, 132);
            this.heightBox.MaxLength = 6;
            this.heightBox.Name = "heightBox";
            this.heightBox.Size = new System.Drawing.Size(50, 20);
            this.heightBox.TabIndex = 5;
            // 
            // addFramesButton
            // 
            this.addFramesButton.Location = new System.Drawing.Point(20, 17);
            this.addFramesButton.Name = "addFramesButton";
            this.addFramesButton.Size = new System.Drawing.Size(117, 23);
            this.addFramesButton.TabIndex = 6;
            this.addFramesButton.Text = "Add Frames";
            this.addFramesButton.UseVisualStyleBackColor = true;
            this.addFramesButton.Click += new System.EventHandler(this.frameAdder_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(20, 109);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(71, 13);
            this.label1.TabIndex = 13;
            this.label1.Text = "Total Frames:";
            // 
            // previewBox
            // 
            this.previewBox.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.previewBox.Location = new System.Drawing.Point(135, 42);
            this.previewBox.Name = "previewBox";
            this.previewBox.Size = new System.Drawing.Size(121, 110);
            this.previewBox.TabIndex = 15;
            this.previewBox.TabStop = false;
            // 
            // numberOfFramesLabel
            // 
            this.numberOfFramesLabel.AutoSize = true;
            this.numberOfFramesLabel.Location = new System.Drawing.Point(87, 109);
            this.numberOfFramesLabel.Name = "numberOfFramesLabel";
            this.numberOfFramesLabel.Size = new System.Drawing.Size(13, 13);
            this.numberOfFramesLabel.TabIndex = 0;
            this.numberOfFramesLabel.Text = "0";
            // 
            // removeFramesButton
            // 
            this.removeFramesButton.Location = new System.Drawing.Point(20, 46);
            this.removeFramesButton.Name = "removeFramesButton";
            this.removeFramesButton.Size = new System.Drawing.Size(117, 23);
            this.removeFramesButton.TabIndex = 16;
            this.removeFramesButton.Text = "Remove Frames";
            this.removeFramesButton.UseVisualStyleBackColor = true;
            this.removeFramesButton.Click += new System.EventHandler(this.removeFramesButton_Click);
            // 
            // removeLastFrameButton
            // 
            this.removeLastFrameButton.Location = new System.Drawing.Point(20, 75);
            this.removeLastFrameButton.Name = "removeLastFrameButton";
            this.removeLastFrameButton.Size = new System.Drawing.Size(117, 23);
            this.removeLastFrameButton.TabIndex = 17;
            this.removeLastFrameButton.Text = "Remove Last Frame";
            this.removeLastFrameButton.UseVisualStyleBackColor = true;
            this.removeLastFrameButton.Click += new System.EventHandler(this.removeLastFrameButton_Click);
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.labelDimX);
            this.groupBox1.Controls.Add(this.typeChooser);
            this.groupBox1.Controls.Add(this.Type);
            this.groupBox1.Controls.Add(this.Delay);
            this.groupBox1.Controls.Add(this.previewBox);
            this.groupBox1.Controls.Add(this.delayBox);
            this.groupBox1.Controls.Add(this.labelDimY);
            this.groupBox1.Controls.Add(this.labelHeight);
            this.groupBox1.Controls.Add(this.heightBox);
            this.groupBox1.Controls.Add(this.dimXBox);
            this.groupBox1.Controls.Add(this.dimYBox);
            this.groupBox1.Location = new System.Drawing.Point(3, -3);
            this.groupBox1.Margin = new System.Windows.Forms.Padding(0);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Padding = new System.Windows.Forms.Padding(0);
            this.groupBox1.Size = new System.Drawing.Size(264, 158);
            this.groupBox1.TabIndex = 18;
            this.groupBox1.TabStop = false;
            // 
            // groupBox2
            // 
            this.groupBox2.Controls.Add(this.addFramesButton);
            this.groupBox2.Controls.Add(this.label1);
            this.groupBox2.Controls.Add(this.removeLastFrameButton);
            this.groupBox2.Controls.Add(this.numberOfFramesLabel);
            this.groupBox2.Controls.Add(this.removeFramesButton);
            this.groupBox2.Location = new System.Drawing.Point(270, -3);
            this.groupBox2.Margin = new System.Windows.Forms.Padding(0);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Padding = new System.Windows.Forms.Padding(0);
            this.groupBox2.Size = new System.Drawing.Size(156, 131);
            this.groupBox2.TabIndex = 19;
            this.groupBox2.TabStop = false;
            // 
            // ObjectPropertiesDialog
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(430, 158);
            this.Controls.Add(this.groupBox2);
            this.Controls.Add(this.groupBox1);
            this.Controls.Add(this.cancelButton);
            this.Controls.Add(this.okButton);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "ObjectPropertiesDialog";
            this.ShowIcon = false;
            this.ShowInTaskbar = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "Object Properties...";
            this.TopMost = true;
            this.Load += new System.EventHandler(this.ObjectPropertiesDialog_Load);
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
        private System.Windows.Forms.ComboBox typeChooser;
        private System.Windows.Forms.Label Type;
        private System.Windows.Forms.Label Delay;
        private System.Windows.Forms.TextBox delayBox;
        private System.Windows.Forms.Label labelDimX;
        private System.Windows.Forms.Label labelDimY;
        private System.Windows.Forms.Label labelHeight;
        private System.Windows.Forms.TextBox dimXBox;
        private System.Windows.Forms.TextBox dimYBox;
        private System.Windows.Forms.TextBox heightBox;
        private System.Windows.Forms.Button addFramesButton;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.PictureBox previewBox;
        private System.Windows.Forms.Label numberOfFramesLabel;
        private System.Windows.Forms.Button removeFramesButton;
        private System.Windows.Forms.Button removeLastFrameButton;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.GroupBox groupBox2;
    }
}