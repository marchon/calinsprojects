namespace MapEditor2
{
    partial class ObjectEditor
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
            this.addObjectButton = new System.Windows.Forms.Button();
            this.previewBox = new System.Windows.Forms.PictureBox();
            this.objectListBox = new System.Windows.Forms.ListBox();
            this.editObjectButton = new System.Windows.Forms.Button();
            this.objectsGroupBox = new System.Windows.Forms.GroupBox();
            ((System.ComponentModel.ISupportInitialize)(this.previewBox)).BeginInit();
            this.objectsGroupBox.SuspendLayout();
            this.SuspendLayout();
            // 
            // addObjectButton
            // 
            this.addObjectButton.Location = new System.Drawing.Point(9, 12);
            this.addObjectButton.Name = "addObjectButton";
            this.addObjectButton.Size = new System.Drawing.Size(197, 23);
            this.addObjectButton.TabIndex = 0;
            this.addObjectButton.Text = "Add Object";
            this.addObjectButton.UseVisualStyleBackColor = true;
            this.addObjectButton.Click += new System.EventHandler(this.addObjectButton_Click);
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
            // objectListBox
            // 
            this.objectListBox.FormattingEnabled = true;
            this.objectListBox.Location = new System.Drawing.Point(6, 19);
            this.objectListBox.Name = "objectListBox";
            this.objectListBox.Size = new System.Drawing.Size(60, 95);
            this.objectListBox.TabIndex = 2;
            this.objectListBox.SelectedIndexChanged += new System.EventHandler(this.objectListBox_SelectedIndexChanged);
            // 
            // editObjectButton
            // 
            this.editObjectButton.Location = new System.Drawing.Point(6, 120);
            this.editObjectButton.Name = "editObjectButton";
            this.editObjectButton.Size = new System.Drawing.Size(183, 23);
            this.editObjectButton.TabIndex = 3;
            this.editObjectButton.Text = "Edit Object";
            this.editObjectButton.UseVisualStyleBackColor = true;
            this.editObjectButton.Click += new System.EventHandler(this.editObjectButton_Click);
            // 
            // objectsGroupBox
            // 
            this.objectsGroupBox.Controls.Add(this.objectListBox);
            this.objectsGroupBox.Controls.Add(this.editObjectButton);
            this.objectsGroupBox.Controls.Add(this.previewBox);
            this.objectsGroupBox.Location = new System.Drawing.Point(9, 41);
            this.objectsGroupBox.Name = "objectsGroupBox";
            this.objectsGroupBox.Size = new System.Drawing.Size(197, 153);
            this.objectsGroupBox.TabIndex = 4;
            this.objectsGroupBox.TabStop = false;
            this.objectsGroupBox.Text = "Objects";
            // 
            // ObjectEditor
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(218, 205);
            this.Controls.Add(this.objectsGroupBox);
            this.Controls.Add(this.addObjectButton);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "ObjectEditor";
            this.ShowIcon = false;
            this.ShowInTaskbar = false;
            this.SizeGripStyle = System.Windows.Forms.SizeGripStyle.Hide;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "Object Editor";
            this.Load += new System.EventHandler(this.ObjectEditor_Load);
            ((System.ComponentModel.ISupportInitialize)(this.previewBox)).EndInit();
            this.objectsGroupBox.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button addObjectButton;
        private System.Windows.Forms.PictureBox previewBox;
        private System.Windows.Forms.ListBox objectListBox;
        private System.Windows.Forms.Button editObjectButton;
        private System.Windows.Forms.GroupBox objectsGroupBox;
    }
}