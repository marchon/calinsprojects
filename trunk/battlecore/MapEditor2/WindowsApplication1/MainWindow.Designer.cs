namespace MapEditor2
{
    partial class MainWindow
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(MainWindow));
            this.mapEditorMenu = new System.Windows.Forms.MenuStrip();
            this.fileToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.newToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.openToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator = new System.Windows.Forms.ToolStripSeparator();
            this.saveToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.closeToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator1 = new System.Windows.Forms.ToolStripSeparator();
            this.exitToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.editToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.undoToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.redoToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.viewToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.changePropertiesToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator4 = new System.Windows.Forms.ToolStripSeparator();
            this.showGridToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.layerToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.terrainToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.objectsToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.startingCoordinatesToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator3 = new System.Windows.Forms.ToolStripSeparator();
            this.objectLayerOptionsToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.snapToGridToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.pickUpModeToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolsToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.exportMinimapToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator2 = new System.Windows.Forms.ToolStripSeparator();
            this.objectEditorToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.vehicleEditorToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.helpToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.hintsToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator5 = new System.Windows.Forms.ToolStripSeparator();
            this.aboutToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.splitContainer1 = new System.Windows.Forms.SplitContainer();
            this.splitContainer2 = new System.Windows.Forms.SplitContainer();
            this.mapOptionsGroupBox = new System.Windows.Forms.GroupBox();
            this.pickUpModeCheckBox = new System.Windows.Forms.CheckBox();
            this.snapToGridCheckBox = new System.Windows.Forms.CheckBox();
            this.layerSelectionComboBox = new System.Windows.Forms.ComboBox();
            this.showGridCheckBox = new System.Windows.Forms.CheckBox();
            this.layerSelectionLabel = new System.Windows.Forms.Label();
            this.mapInfoGroupBox = new System.Windows.Forms.GroupBox();
            this.tileWidthLabel = new System.Windows.Forms.Label();
            this.descrLabel = new System.Windows.Forms.Label();
            this.mnLabel = new System.Windows.Forms.Label();
            this.descriptionLabel = new System.Windows.Forms.Label();
            this.mapNameLabel = new System.Windows.Forms.Label();
            this.mhLabel = new System.Windows.Forms.Label();
            this.mwLabel = new System.Windows.Forms.Label();
            this.thLabel = new System.Windows.Forms.Label();
            this.twLabel = new System.Windows.Forms.Label();
            this.mapHeightLabel = new System.Windows.Forms.Label();
            this.mapWidthLabel = new System.Windows.Forms.Label();
            this.tileHeightLabel = new System.Windows.Forms.Label();
            this.currentLayerGroupBox = new System.Windows.Forms.GroupBox();
            this.mapGroupBox = new System.Windows.Forms.GroupBox();
            this.tileContainer = new MapEditor2.TileContainer();
            this.objectContainer = new MapEditor2.ObjectContainer();
            this.map = new MapEditor2.Map();
            this.mapEditorMenu.SuspendLayout();
            this.splitContainer1.Panel1.SuspendLayout();
            this.splitContainer1.Panel2.SuspendLayout();
            this.splitContainer1.SuspendLayout();
            this.splitContainer2.Panel1.SuspendLayout();
            this.splitContainer2.Panel2.SuspendLayout();
            this.splitContainer2.SuspendLayout();
            this.mapOptionsGroupBox.SuspendLayout();
            this.mapInfoGroupBox.SuspendLayout();
            this.currentLayerGroupBox.SuspendLayout();
            this.mapGroupBox.SuspendLayout();
            this.SuspendLayout();
            // 
            // mapEditorMenu
            // 
            this.mapEditorMenu.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.fileToolStripMenuItem,
            this.editToolStripMenuItem,
            this.viewToolStripMenuItem,
            this.toolsToolStripMenuItem,
            this.helpToolStripMenuItem});
            this.mapEditorMenu.Location = new System.Drawing.Point(0, 0);
            this.mapEditorMenu.Name = "mapEditorMenu";
            this.mapEditorMenu.Size = new System.Drawing.Size(1016, 24);
            this.mapEditorMenu.TabIndex = 0;
            this.mapEditorMenu.Text = "mapEditorMenu";
            // 
            // fileToolStripMenuItem
            // 
            this.fileToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.newToolStripMenuItem,
            this.openToolStripMenuItem,
            this.toolStripSeparator,
            this.saveToolStripMenuItem,
            this.closeToolStripMenuItem,
            this.toolStripSeparator1,
            this.exitToolStripMenuItem});
            this.fileToolStripMenuItem.Name = "fileToolStripMenuItem";
            this.fileToolStripMenuItem.Size = new System.Drawing.Size(35, 20);
            this.fileToolStripMenuItem.Text = "&File";
            // 
            // newToolStripMenuItem
            // 
            this.newToolStripMenuItem.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.newToolStripMenuItem.Name = "newToolStripMenuItem";
            this.newToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.N)));
            this.newToolStripMenuItem.Size = new System.Drawing.Size(174, 22);
            this.newToolStripMenuItem.Text = "&New Map";
            this.newToolStripMenuItem.Click += new System.EventHandler(this.newToolStripMenuItem_Click);
            // 
            // openToolStripMenuItem
            // 
            this.openToolStripMenuItem.Name = "openToolStripMenuItem";
            this.openToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.O)));
            this.openToolStripMenuItem.Size = new System.Drawing.Size(174, 22);
            this.openToolStripMenuItem.Text = "&Open Map";
            this.openToolStripMenuItem.Click += new System.EventHandler(this.openToolStripMenuItem_Click);
            // 
            // toolStripSeparator
            // 
            this.toolStripSeparator.Name = "toolStripSeparator";
            this.toolStripSeparator.Size = new System.Drawing.Size(171, 6);
            // 
            // saveToolStripMenuItem
            // 
            this.saveToolStripMenuItem.Enabled = false;
            this.saveToolStripMenuItem.Image = ((System.Drawing.Image)(resources.GetObject("saveToolStripMenuItem.Image")));
            this.saveToolStripMenuItem.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.saveToolStripMenuItem.Name = "saveToolStripMenuItem";
            this.saveToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.S)));
            this.saveToolStripMenuItem.Size = new System.Drawing.Size(174, 22);
            this.saveToolStripMenuItem.Text = "&Save";
            this.saveToolStripMenuItem.Click += new System.EventHandler(this.saveToolStripMenuItem_Click);
            // 
            // closeToolStripMenuItem
            // 
            this.closeToolStripMenuItem.Enabled = false;
            this.closeToolStripMenuItem.Name = "closeToolStripMenuItem";
            this.closeToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.C)));
            this.closeToolStripMenuItem.Size = new System.Drawing.Size(174, 22);
            this.closeToolStripMenuItem.Text = "Close";
            this.closeToolStripMenuItem.Click += new System.EventHandler(this.closeToolStripMenuItem_Click);
            // 
            // toolStripSeparator1
            // 
            this.toolStripSeparator1.Name = "toolStripSeparator1";
            this.toolStripSeparator1.Size = new System.Drawing.Size(171, 6);
            // 
            // exitToolStripMenuItem
            // 
            this.exitToolStripMenuItem.Name = "exitToolStripMenuItem";
            this.exitToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.Q)));
            this.exitToolStripMenuItem.Size = new System.Drawing.Size(174, 22);
            this.exitToolStripMenuItem.Text = "E&xit";
            this.exitToolStripMenuItem.Click += new System.EventHandler(this.exitToolStripMenuItem_Click);
            // 
            // editToolStripMenuItem
            // 
            this.editToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.undoToolStripMenuItem,
            this.redoToolStripMenuItem});
            this.editToolStripMenuItem.Name = "editToolStripMenuItem";
            this.editToolStripMenuItem.Size = new System.Drawing.Size(37, 20);
            this.editToolStripMenuItem.Text = "&Edit";
            // 
            // undoToolStripMenuItem
            // 
            this.undoToolStripMenuItem.Enabled = false;
            this.undoToolStripMenuItem.Name = "undoToolStripMenuItem";
            this.undoToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.Z)));
            this.undoToolStripMenuItem.Size = new System.Drawing.Size(148, 22);
            this.undoToolStripMenuItem.Text = "&Undo";
            this.undoToolStripMenuItem.Click += new System.EventHandler(this.undoToolStripMenuItem_Click);
            // 
            // redoToolStripMenuItem
            // 
            this.redoToolStripMenuItem.Enabled = false;
            this.redoToolStripMenuItem.Name = "redoToolStripMenuItem";
            this.redoToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.Y)));
            this.redoToolStripMenuItem.Size = new System.Drawing.Size(148, 22);
            this.redoToolStripMenuItem.Text = "&Redo";
            this.redoToolStripMenuItem.Click += new System.EventHandler(this.redoToolStripMenuItem_Click);
            // 
            // viewToolStripMenuItem
            // 
            this.viewToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.changePropertiesToolStripMenuItem,
            this.toolStripSeparator4,
            this.showGridToolStripMenuItem,
            this.layerToolStripMenuItem,
            this.toolStripSeparator3,
            this.objectLayerOptionsToolStripMenuItem});
            this.viewToolStripMenuItem.Name = "viewToolStripMenuItem";
            this.viewToolStripMenuItem.Size = new System.Drawing.Size(39, 20);
            this.viewToolStripMenuItem.Text = "&Map";
            // 
            // changePropertiesToolStripMenuItem
            // 
            this.changePropertiesToolStripMenuItem.Enabled = false;
            this.changePropertiesToolStripMenuItem.Name = "changePropertiesToolStripMenuItem";
            this.changePropertiesToolStripMenuItem.Size = new System.Drawing.Size(186, 22);
            this.changePropertiesToolStripMenuItem.Text = "Change Properties...";
            this.changePropertiesToolStripMenuItem.Click += new System.EventHandler(this.changePropertiesToolStripMenuItem_Click);
            // 
            // toolStripSeparator4
            // 
            this.toolStripSeparator4.Name = "toolStripSeparator4";
            this.toolStripSeparator4.Size = new System.Drawing.Size(183, 6);
            // 
            // showGridToolStripMenuItem
            // 
            this.showGridToolStripMenuItem.CheckOnClick = true;
            this.showGridToolStripMenuItem.Enabled = false;
            this.showGridToolStripMenuItem.Name = "showGridToolStripMenuItem";
            this.showGridToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.G)));
            this.showGridToolStripMenuItem.Size = new System.Drawing.Size(186, 22);
            this.showGridToolStripMenuItem.Text = "Show Grid";
            this.showGridToolStripMenuItem.Click += new System.EventHandler(this.showGridToolStripMenuItem_Click);
            // 
            // layerToolStripMenuItem
            // 
            this.layerToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.terrainToolStripMenuItem,
            this.objectsToolStripMenuItem,
            this.startingCoordinatesToolStripMenuItem});
            this.layerToolStripMenuItem.Enabled = false;
            this.layerToolStripMenuItem.Name = "layerToolStripMenuItem";
            this.layerToolStripMenuItem.Size = new System.Drawing.Size(186, 22);
            this.layerToolStripMenuItem.Text = "Layer";
            // 
            // terrainToolStripMenuItem
            // 
            this.terrainToolStripMenuItem.Name = "terrainToolStripMenuItem";
            this.terrainToolStripMenuItem.Size = new System.Drawing.Size(184, 22);
            this.terrainToolStripMenuItem.Text = "Terrain";
            this.terrainToolStripMenuItem.Click += new System.EventHandler(this.terrainToolStripMenuItem_Click);
            // 
            // objectsToolStripMenuItem
            // 
            this.objectsToolStripMenuItem.Name = "objectsToolStripMenuItem";
            this.objectsToolStripMenuItem.Size = new System.Drawing.Size(184, 22);
            this.objectsToolStripMenuItem.Text = "Objects";
            this.objectsToolStripMenuItem.Click += new System.EventHandler(this.objectsToolStripMenuItem_Click);
            // 
            // startingCoordinatesToolStripMenuItem
            // 
            this.startingCoordinatesToolStripMenuItem.Name = "startingCoordinatesToolStripMenuItem";
            this.startingCoordinatesToolStripMenuItem.Size = new System.Drawing.Size(184, 22);
            this.startingCoordinatesToolStripMenuItem.Text = "Starting Coordinates";
            this.startingCoordinatesToolStripMenuItem.Click += new System.EventHandler(this.startingCoordinatesToolStripMenuItem_Click);
            // 
            // toolStripSeparator3
            // 
            this.toolStripSeparator3.Name = "toolStripSeparator3";
            this.toolStripSeparator3.Size = new System.Drawing.Size(183, 6);
            // 
            // objectLayerOptionsToolStripMenuItem
            // 
            this.objectLayerOptionsToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.snapToGridToolStripMenuItem,
            this.pickUpModeToolStripMenuItem});
            this.objectLayerOptionsToolStripMenuItem.Enabled = false;
            this.objectLayerOptionsToolStripMenuItem.Name = "objectLayerOptionsToolStripMenuItem";
            this.objectLayerOptionsToolStripMenuItem.Size = new System.Drawing.Size(186, 22);
            this.objectLayerOptionsToolStripMenuItem.Text = "Layer Options";
            // 
            // snapToGridToolStripMenuItem
            // 
            this.snapToGridToolStripMenuItem.CheckOnClick = true;
            this.snapToGridToolStripMenuItem.Enabled = false;
            this.snapToGridToolStripMenuItem.Name = "snapToGridToolStripMenuItem";
            this.snapToGridToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)(((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.Shift)
                        | System.Windows.Forms.Keys.G)));
            this.snapToGridToolStripMenuItem.Size = new System.Drawing.Size(216, 22);
            this.snapToGridToolStripMenuItem.Text = "Snap To Grid";
            this.snapToGridToolStripMenuItem.Click += new System.EventHandler(this.snapToGridToolStripMenuItem_Click);
            // 
            // pickUpModeToolStripMenuItem
            // 
            this.pickUpModeToolStripMenuItem.CheckOnClick = true;
            this.pickUpModeToolStripMenuItem.Enabled = false;
            this.pickUpModeToolStripMenuItem.Name = "pickUpModeToolStripMenuItem";
            this.pickUpModeToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)(((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.Shift)
                        | System.Windows.Forms.Keys.P)));
            this.pickUpModeToolStripMenuItem.Size = new System.Drawing.Size(216, 22);
            this.pickUpModeToolStripMenuItem.Text = "Pick Up Mode";
            this.pickUpModeToolStripMenuItem.Click += new System.EventHandler(this.pickUpModeToolStripMenuItem_Click);
            // 
            // toolsToolStripMenuItem
            // 
            this.toolsToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.exportMinimapToolStripMenuItem,
            this.toolStripSeparator2,
            this.objectEditorToolStripMenuItem,
            this.vehicleEditorToolStripMenuItem});
            this.toolsToolStripMenuItem.Name = "toolsToolStripMenuItem";
            this.toolsToolStripMenuItem.Size = new System.Drawing.Size(44, 20);
            this.toolsToolStripMenuItem.Text = "&Tools";
            // 
            // exportMinimapToolStripMenuItem
            // 
            this.exportMinimapToolStripMenuItem.Enabled = false;
            this.exportMinimapToolStripMenuItem.Name = "exportMinimapToolStripMenuItem";
            this.exportMinimapToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)(((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.Shift)
                        | System.Windows.Forms.Keys.M)));
            this.exportMinimapToolStripMenuItem.Size = new System.Drawing.Size(240, 22);
            this.exportMinimapToolStripMenuItem.Text = "Export Minimap...";
            this.exportMinimapToolStripMenuItem.Click += new System.EventHandler(this.exportMinimapToolStripMenuItem_Click);
            // 
            // toolStripSeparator2
            // 
            this.toolStripSeparator2.Name = "toolStripSeparator2";
            this.toolStripSeparator2.Size = new System.Drawing.Size(237, 6);
            // 
            // objectEditorToolStripMenuItem
            // 
            this.objectEditorToolStripMenuItem.Name = "objectEditorToolStripMenuItem";
            this.objectEditorToolStripMenuItem.Size = new System.Drawing.Size(240, 22);
            this.objectEditorToolStripMenuItem.Text = "Object Editor";
            this.objectEditorToolStripMenuItem.Click += new System.EventHandler(this.objectEditorToolStripMenuItem_Click);
            // 
            // vehicleEditorToolStripMenuItem
            // 
            this.vehicleEditorToolStripMenuItem.Name = "vehicleEditorToolStripMenuItem";
            this.vehicleEditorToolStripMenuItem.Size = new System.Drawing.Size(240, 22);
            this.vehicleEditorToolStripMenuItem.Text = "Vehicle Editor";
            this.vehicleEditorToolStripMenuItem.Click += new System.EventHandler(this.vehicleEditorToolStripMenuItem_Click);
            // 
            // helpToolStripMenuItem
            // 
            this.helpToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.hintsToolStripMenuItem,
            this.toolStripSeparator5,
            this.aboutToolStripMenuItem});
            this.helpToolStripMenuItem.Name = "helpToolStripMenuItem";
            this.helpToolStripMenuItem.Size = new System.Drawing.Size(40, 20);
            this.helpToolStripMenuItem.Text = "&Help";
            // 
            // hintsToolStripMenuItem
            // 
            this.hintsToolStripMenuItem.Name = "hintsToolStripMenuItem";
            this.hintsToolStripMenuItem.Size = new System.Drawing.Size(126, 22);
            this.hintsToolStripMenuItem.Text = "&Hints...";
            this.hintsToolStripMenuItem.Click += new System.EventHandler(this.hintsToolStripMenuItem_Click);
            // 
            // toolStripSeparator5
            // 
            this.toolStripSeparator5.Name = "toolStripSeparator5";
            this.toolStripSeparator5.Size = new System.Drawing.Size(123, 6);
            // 
            // aboutToolStripMenuItem
            // 
            this.aboutToolStripMenuItem.Name = "aboutToolStripMenuItem";
            this.aboutToolStripMenuItem.Size = new System.Drawing.Size(126, 22);
            this.aboutToolStripMenuItem.Text = "&About...";
            this.aboutToolStripMenuItem.Click += new System.EventHandler(this.aboutToolStripMenuItem_Click);
            // 
            // splitContainer1
            // 
            this.splitContainer1.CausesValidation = false;
            this.splitContainer1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.splitContainer1.FixedPanel = System.Windows.Forms.FixedPanel.Panel1;
            this.splitContainer1.IsSplitterFixed = true;
            this.splitContainer1.Location = new System.Drawing.Point(0, 24);
            this.splitContainer1.Margin = new System.Windows.Forms.Padding(0);
            this.splitContainer1.Name = "splitContainer1";
            // 
            // splitContainer1.Panel1
            // 
            this.splitContainer1.Panel1.Controls.Add(this.splitContainer2);
            this.splitContainer1.Panel1MinSize = 180;
            // 
            // splitContainer1.Panel2
            // 
            this.splitContainer1.Panel2.BackColor = System.Drawing.SystemColors.AppWorkspace;
            this.splitContainer1.Panel2.Controls.Add(this.mapGroupBox);
            this.splitContainer1.Panel2.Padding = new System.Windows.Forms.Padding(2);
            this.splitContainer1.Size = new System.Drawing.Size(1016, 710);
            this.splitContainer1.SplitterDistance = 265;
            this.splitContainer1.TabIndex = 0;
            this.splitContainer1.TabStop = false;
            // 
            // splitContainer2
            // 
            this.splitContainer2.Dock = System.Windows.Forms.DockStyle.Fill;
            this.splitContainer2.FixedPanel = System.Windows.Forms.FixedPanel.Panel1;
            this.splitContainer2.IsSplitterFixed = true;
            this.splitContainer2.Location = new System.Drawing.Point(0, 0);
            this.splitContainer2.Margin = new System.Windows.Forms.Padding(0);
            this.splitContainer2.Name = "splitContainer2";
            this.splitContainer2.Orientation = System.Windows.Forms.Orientation.Horizontal;
            // 
            // splitContainer2.Panel1
            // 
            this.splitContainer2.Panel1.BackColor = System.Drawing.SystemColors.AppWorkspace;
            this.splitContainer2.Panel1.Controls.Add(this.mapOptionsGroupBox);
            this.splitContainer2.Panel1.Controls.Add(this.mapInfoGroupBox);
            this.splitContainer2.Panel1.Padding = new System.Windows.Forms.Padding(2);
            // 
            // splitContainer2.Panel2
            // 
            this.splitContainer2.Panel2.BackColor = System.Drawing.SystemColors.AppWorkspace;
            this.splitContainer2.Panel2.Controls.Add(this.currentLayerGroupBox);
            this.splitContainer2.Panel2.Padding = new System.Windows.Forms.Padding(2);
            this.splitContainer2.Size = new System.Drawing.Size(265, 710);
            this.splitContainer2.SplitterDistance = 178;
            this.splitContainer2.TabIndex = 0;
            this.splitContainer2.TabStop = false;
            // 
            // mapOptionsGroupBox
            // 
            this.mapOptionsGroupBox.Controls.Add(this.pickUpModeCheckBox);
            this.mapOptionsGroupBox.Controls.Add(this.snapToGridCheckBox);
            this.mapOptionsGroupBox.Controls.Add(this.layerSelectionComboBox);
            this.mapOptionsGroupBox.Controls.Add(this.showGridCheckBox);
            this.mapOptionsGroupBox.Controls.Add(this.layerSelectionLabel);
            this.mapOptionsGroupBox.ForeColor = System.Drawing.SystemColors.ControlLightLight;
            this.mapOptionsGroupBox.Location = new System.Drawing.Point(136, 2);
            this.mapOptionsGroupBox.Name = "mapOptionsGroupBox";
            this.mapOptionsGroupBox.Size = new System.Drawing.Size(127, 174);
            this.mapOptionsGroupBox.TabIndex = 0;
            this.mapOptionsGroupBox.TabStop = false;
            this.mapOptionsGroupBox.Text = "Map Options";
            // 
            // pickUpModeCheckBox
            // 
            this.pickUpModeCheckBox.AutoSize = true;
            this.pickUpModeCheckBox.Enabled = false;
            this.pickUpModeCheckBox.Location = new System.Drawing.Point(14, 86);
            this.pickUpModeCheckBox.Name = "pickUpModeCheckBox";
            this.pickUpModeCheckBox.Size = new System.Drawing.Size(94, 17);
            this.pickUpModeCheckBox.TabIndex = 0;
            this.pickUpModeCheckBox.TabStop = false;
            this.pickUpModeCheckBox.Text = "Pick Up Mode";
            this.pickUpModeCheckBox.UseVisualStyleBackColor = true;
            this.pickUpModeCheckBox.CheckedChanged += new System.EventHandler(this.pickUpModeCheckBox_CheckedChanged);
            // 
            // snapToGridCheckBox
            // 
            this.snapToGridCheckBox.AutoSize = true;
            this.snapToGridCheckBox.Enabled = false;
            this.snapToGridCheckBox.Location = new System.Drawing.Point(14, 56);
            this.snapToGridCheckBox.Name = "snapToGridCheckBox";
            this.snapToGridCheckBox.Size = new System.Drawing.Size(89, 17);
            this.snapToGridCheckBox.TabIndex = 0;
            this.snapToGridCheckBox.TabStop = false;
            this.snapToGridCheckBox.Text = "Snap To Grid";
            this.snapToGridCheckBox.UseVisualStyleBackColor = true;
            this.snapToGridCheckBox.CheckedChanged += new System.EventHandler(this.snapToGridCheckBox_CheckedChanged);
            // 
            // layerSelectionComboBox
            // 
            this.layerSelectionComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.layerSelectionComboBox.Enabled = false;
            this.layerSelectionComboBox.FormattingEnabled = true;
            this.layerSelectionComboBox.Items.AddRange(new object[] {
            "Terrain",
            "Objects",
            "Spawn coordinates"});
            this.layerSelectionComboBox.Location = new System.Drawing.Point(6, 136);
            this.layerSelectionComboBox.Name = "layerSelectionComboBox";
            this.layerSelectionComboBox.Size = new System.Drawing.Size(115, 21);
            this.layerSelectionComboBox.TabIndex = 0;
            this.layerSelectionComboBox.TabStop = false;
            this.layerSelectionComboBox.SelectedIndexChanged += new System.EventHandler(this.layerSelectionComboBox_SelectedIndexChanged);
            // 
            // showGridCheckBox
            // 
            this.showGridCheckBox.AutoSize = true;
            this.showGridCheckBox.Enabled = false;
            this.showGridCheckBox.Location = new System.Drawing.Point(14, 26);
            this.showGridCheckBox.Name = "showGridCheckBox";
            this.showGridCheckBox.Size = new System.Drawing.Size(75, 17);
            this.showGridCheckBox.TabIndex = 0;
            this.showGridCheckBox.TabStop = false;
            this.showGridCheckBox.Text = "Show Grid";
            this.showGridCheckBox.UseVisualStyleBackColor = true;
            this.showGridCheckBox.CheckedChanged += new System.EventHandler(this.showGridCheckBox_CheckedChanged);
            // 
            // layerSelectionLabel
            // 
            this.layerSelectionLabel.AutoSize = true;
            this.layerSelectionLabel.Location = new System.Drawing.Point(3, 120);
            this.layerSelectionLabel.Name = "layerSelectionLabel";
            this.layerSelectionLabel.Size = new System.Drawing.Size(69, 13);
            this.layerSelectionLabel.TabIndex = 1;
            this.layerSelectionLabel.Text = "Current layer:";
            // 
            // mapInfoGroupBox
            // 
            this.mapInfoGroupBox.Controls.Add(this.tileWidthLabel);
            this.mapInfoGroupBox.Controls.Add(this.descrLabel);
            this.mapInfoGroupBox.Controls.Add(this.mnLabel);
            this.mapInfoGroupBox.Controls.Add(this.descriptionLabel);
            this.mapInfoGroupBox.Controls.Add(this.mapNameLabel);
            this.mapInfoGroupBox.Controls.Add(this.mhLabel);
            this.mapInfoGroupBox.Controls.Add(this.mwLabel);
            this.mapInfoGroupBox.Controls.Add(this.thLabel);
            this.mapInfoGroupBox.Controls.Add(this.twLabel);
            this.mapInfoGroupBox.Controls.Add(this.mapHeightLabel);
            this.mapInfoGroupBox.Controls.Add(this.mapWidthLabel);
            this.mapInfoGroupBox.Controls.Add(this.tileHeightLabel);
            this.mapInfoGroupBox.ForeColor = System.Drawing.SystemColors.ControlLightLight;
            this.mapInfoGroupBox.Location = new System.Drawing.Point(2, 2);
            this.mapInfoGroupBox.Name = "mapInfoGroupBox";
            this.mapInfoGroupBox.Size = new System.Drawing.Size(131, 174);
            this.mapInfoGroupBox.TabIndex = 4;
            this.mapInfoGroupBox.TabStop = false;
            this.mapInfoGroupBox.Text = "Map Info";
            // 
            // tileWidthLabel
            // 
            this.tileWidthLabel.AutoSize = true;
            this.tileWidthLabel.Location = new System.Drawing.Point(1, 90);
            this.tileWidthLabel.Name = "tileWidthLabel";
            this.tileWidthLabel.Size = new System.Drawing.Size(55, 13);
            this.tileWidthLabel.TabIndex = 4;
            this.tileWidthLabel.Text = "Tile width:";
            // 
            // descrLabel
            // 
            this.descrLabel.AutoEllipsis = true;
            this.descrLabel.Cursor = System.Windows.Forms.Cursors.Hand;
            this.descrLabel.Enabled = false;
            this.descrLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 6.5F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.descrLabel.ForeColor = System.Drawing.Color.MidnightBlue;
            this.descrLabel.Location = new System.Drawing.Point(4, 49);
            this.descrLabel.Name = "descrLabel";
            this.descrLabel.Size = new System.Drawing.Size(124, 39);
            this.descrLabel.TabIndex = 15;
            this.descrLabel.Click += new System.EventHandler(this.descrLabel_Click);
            // 
            // mnLabel
            // 
            this.mnLabel.AutoSize = true;
            this.mnLabel.Location = new System.Drawing.Point(63, 16);
            this.mnLabel.Name = "mnLabel";
            this.mnLabel.Size = new System.Drawing.Size(0, 13);
            this.mnLabel.TabIndex = 14;
            // 
            // descriptionLabel
            // 
            this.descriptionLabel.AutoSize = true;
            this.descriptionLabel.Location = new System.Drawing.Point(1, 36);
            this.descriptionLabel.Name = "descriptionLabel";
            this.descriptionLabel.Size = new System.Drawing.Size(63, 13);
            this.descriptionLabel.TabIndex = 13;
            this.descriptionLabel.Text = "Description:";
            // 
            // mapNameLabel
            // 
            this.mapNameLabel.AutoSize = true;
            this.mapNameLabel.Location = new System.Drawing.Point(1, 16);
            this.mapNameLabel.Name = "mapNameLabel";
            this.mapNameLabel.Size = new System.Drawing.Size(62, 13);
            this.mapNameLabel.TabIndex = 12;
            this.mapNameLabel.Text = "Map Name:";
            // 
            // mhLabel
            // 
            this.mhLabel.AutoSize = true;
            this.mhLabel.Location = new System.Drawing.Point(60, 150);
            this.mhLabel.Name = "mhLabel";
            this.mhLabel.Size = new System.Drawing.Size(0, 13);
            this.mhLabel.TabIndex = 11;
            // 
            // mwLabel
            // 
            this.mwLabel.AutoSize = true;
            this.mwLabel.Location = new System.Drawing.Point(60, 130);
            this.mwLabel.Name = "mwLabel";
            this.mwLabel.Size = new System.Drawing.Size(0, 13);
            this.mwLabel.TabIndex = 10;
            // 
            // thLabel
            // 
            this.thLabel.AutoSize = true;
            this.thLabel.Location = new System.Drawing.Point(60, 110);
            this.thLabel.Name = "thLabel";
            this.thLabel.Size = new System.Drawing.Size(0, 13);
            this.thLabel.TabIndex = 9;
            // 
            // twLabel
            // 
            this.twLabel.AutoSize = true;
            this.twLabel.Location = new System.Drawing.Point(60, 90);
            this.twLabel.Name = "twLabel";
            this.twLabel.Size = new System.Drawing.Size(0, 13);
            this.twLabel.TabIndex = 8;
            // 
            // mapHeightLabel
            // 
            this.mapHeightLabel.AutoSize = true;
            this.mapHeightLabel.Location = new System.Drawing.Point(1, 150);
            this.mapHeightLabel.Name = "mapHeightLabel";
            this.mapHeightLabel.Size = new System.Drawing.Size(63, 13);
            this.mapHeightLabel.TabIndex = 7;
            this.mapHeightLabel.Text = "Map height:";
            // 
            // mapWidthLabel
            // 
            this.mapWidthLabel.AutoSize = true;
            this.mapWidthLabel.Location = new System.Drawing.Point(1, 130);
            this.mapWidthLabel.Name = "mapWidthLabel";
            this.mapWidthLabel.Size = new System.Drawing.Size(59, 13);
            this.mapWidthLabel.TabIndex = 6;
            this.mapWidthLabel.Text = "Map width:";
            // 
            // tileHeightLabel
            // 
            this.tileHeightLabel.AutoSize = true;
            this.tileHeightLabel.Location = new System.Drawing.Point(1, 110);
            this.tileHeightLabel.Name = "tileHeightLabel";
            this.tileHeightLabel.Size = new System.Drawing.Size(59, 13);
            this.tileHeightLabel.TabIndex = 5;
            this.tileHeightLabel.Text = "Tile height:";
            // 
            // currentLayerGroupBox
            // 
            this.currentLayerGroupBox.Controls.Add(this.tileContainer);
            this.currentLayerGroupBox.Controls.Add(this.objectContainer);
            this.currentLayerGroupBox.Dock = System.Windows.Forms.DockStyle.Fill;
            this.currentLayerGroupBox.ForeColor = System.Drawing.SystemColors.ControlLightLight;
            this.currentLayerGroupBox.Location = new System.Drawing.Point(2, 2);
            this.currentLayerGroupBox.Name = "currentLayerGroupBox";
            this.currentLayerGroupBox.Size = new System.Drawing.Size(261, 524);
            this.currentLayerGroupBox.TabIndex = 0;
            this.currentLayerGroupBox.TabStop = false;
            // 
            // mapGroupBox
            // 
            this.mapGroupBox.BackColor = System.Drawing.SystemColors.AppWorkspace;
            this.mapGroupBox.Controls.Add(this.map);
            this.mapGroupBox.Cursor = System.Windows.Forms.Cursors.Arrow;
            this.mapGroupBox.Dock = System.Windows.Forms.DockStyle.Fill;
            this.mapGroupBox.ForeColor = System.Drawing.SystemColors.ControlLightLight;
            this.mapGroupBox.Location = new System.Drawing.Point(2, 2);
            this.mapGroupBox.Name = "mapGroupBox";
            this.mapGroupBox.Size = new System.Drawing.Size(743, 706);
            this.mapGroupBox.TabIndex = 0;
            this.mapGroupBox.TabStop = false;
            this.mapGroupBox.Text = "Map";
            // 
            // tileContainer
            // 
            this.tileContainer.AutoScroll = true;
            this.tileContainer.BackColor = System.Drawing.SystemColors.AppWorkspace;
            this.tileContainer.Cursor = System.Windows.Forms.Cursors.Hand;
            this.tileContainer.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tileContainer.Location = new System.Drawing.Point(3, 16);
            this.tileContainer.Margin = new System.Windows.Forms.Padding(0);
            this.tileContainer.Name = "tileContainer";
            this.tileContainer.Size = new System.Drawing.Size(255, 505);
            this.tileContainer.TabIndex = 0;
            this.tileContainer.Text = "tileContainer";
            this.tileContainer.Visible = false;
            // 
            // objectContainer
            // 
            this.objectContainer.AutoScroll = true;
            this.objectContainer.AutoScrollMinSize = new System.Drawing.Size(0, 3);
            this.objectContainer.BackColor = System.Drawing.SystemColors.AppWorkspace;
            this.objectContainer.Cursor = System.Windows.Forms.Cursors.Hand;
            this.objectContainer.Dock = System.Windows.Forms.DockStyle.Fill;
            this.objectContainer.Location = new System.Drawing.Point(3, 16);
            this.objectContainer.Margin = new System.Windows.Forms.Padding(0);
            this.objectContainer.Name = "objectContainer";
            this.objectContainer.Size = new System.Drawing.Size(255, 505);
            this.objectContainer.TabIndex = 0;
            this.objectContainer.Text = "objectContainer";
            this.objectContainer.Visible = false;
            // 
            // map
            // 
            this.map.AutoScroll = true;
            this.map.AutoScrollMinSize = new System.Drawing.Size(0, 3);
            this.map.BackColor = System.Drawing.SystemColors.AppWorkspace;
            this.map.Cursor = System.Windows.Forms.Cursors.Hand;
            this.map.Dock = System.Windows.Forms.DockStyle.Fill;
            this.map.IsSaved = false;
            this.map.Location = new System.Drawing.Point(3, 16);
            this.map.Margin = new System.Windows.Forms.Padding(0);
            this.map.Name = "map";
            this.map.Size = new System.Drawing.Size(737, 687);
            this.map.TabIndex = 0;
            this.map.TabStop = false;
            this.map.Text = "map";
            this.map.Visible = false;
            // 
            // MainWindow
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1016, 734);
            this.Controls.Add(this.splitContainer1);
            this.Controls.Add(this.mapEditorMenu);
            this.MainMenuStrip = this.mapEditorMenu;
            this.Name = "MainWindow";
            this.Text = "Map Editor II";
            this.Load += new System.EventHandler(this.MainWindow_Load);
            this.mapEditorMenu.ResumeLayout(false);
            this.mapEditorMenu.PerformLayout();
            this.splitContainer1.Panel1.ResumeLayout(false);
            this.splitContainer1.Panel2.ResumeLayout(false);
            this.splitContainer1.ResumeLayout(false);
            this.splitContainer2.Panel1.ResumeLayout(false);
            this.splitContainer2.Panel2.ResumeLayout(false);
            this.splitContainer2.ResumeLayout(false);
            this.mapOptionsGroupBox.ResumeLayout(false);
            this.mapOptionsGroupBox.PerformLayout();
            this.mapInfoGroupBox.ResumeLayout(false);
            this.mapInfoGroupBox.PerformLayout();
            this.currentLayerGroupBox.ResumeLayout(false);
            this.mapGroupBox.ResumeLayout(false);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.MenuStrip mapEditorMenu;
        private System.Windows.Forms.ToolStripMenuItem fileToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem newToolStripMenuItem;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator;
        private System.Windows.Forms.ToolStripMenuItem saveToolStripMenuItem;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator1;
        private System.Windows.Forms.ToolStripMenuItem exitToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem editToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem undoToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem redoToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem helpToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem hintsToolStripMenuItem;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator5;
        private System.Windows.Forms.ToolStripMenuItem aboutToolStripMenuItem;
        private System.Windows.Forms.SplitContainer splitContainer1;
        private System.Windows.Forms.SplitContainer splitContainer2;
        private System.Windows.Forms.Label layerSelectionLabel;
        private System.Windows.Forms.ComboBox layerSelectionComboBox;
        private System.Windows.Forms.ToolStripMenuItem openToolStripMenuItem;
        private System.Windows.Forms.GroupBox mapInfoGroupBox;
        private System.Windows.Forms.GroupBox mapGroupBox;
        private System.Windows.Forms.GroupBox currentLayerGroupBox;
        private System.Windows.Forms.Label mapHeightLabel;
        private System.Windows.Forms.Label mapWidthLabel;
        private System.Windows.Forms.Label tileHeightLabel;
        private System.Windows.Forms.Label tileWidthLabel;
        private System.Windows.Forms.Label mhLabel;
        private System.Windows.Forms.Label mwLabel;
        private System.Windows.Forms.Label thLabel;
        private System.Windows.Forms.Label twLabel;

        private TileContainer tileContainer;
        private ObjectContainer objectContainer;
        private Map map;
        private System.Windows.Forms.ToolStripMenuItem toolsToolStripMenuItem;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator2;
        private System.Windows.Forms.ToolStripMenuItem viewToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem showGridToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem closeToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem terrainToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem objectsToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem startingCoordinatesToolStripMenuItem;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator3;
        private System.Windows.Forms.ToolStripMenuItem objectLayerOptionsToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem snapToGridToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem layerToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem pickUpModeToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem changePropertiesToolStripMenuItem;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator4;
        private System.Windows.Forms.ToolStripMenuItem exportMinimapToolStripMenuItem;
        private System.Windows.Forms.CheckBox pickUpModeCheckBox;
        private System.Windows.Forms.CheckBox snapToGridCheckBox;
        private System.Windows.Forms.CheckBox showGridCheckBox;
        private System.Windows.Forms.GroupBox mapOptionsGroupBox;
        private System.Windows.Forms.Label mapNameLabel;
        private System.Windows.Forms.Label descrLabel;
        private System.Windows.Forms.Label mnLabel;
        private System.Windows.Forms.Label descriptionLabel;
        private System.Windows.Forms.ToolStripMenuItem objectEditorToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem vehicleEditorToolStripMenuItem;
    }
}

