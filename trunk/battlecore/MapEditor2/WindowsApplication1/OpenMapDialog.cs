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
    public partial class OpenMapDialog : Form
    {
        public OpenMapDialog()
        {
            InitializeComponent();
        }

        public int SelectedMapIndex
        {
            get
            {
                return mapListComboBox.SelectedIndex;
            }
        }

        public string SelectedMapName
        {
            get
            {
                return (string)(mapListComboBox.SelectedItem);
            }
        }

        public void AddMapName(string mapName)
        {
            this.mapListComboBox.Items.Add(mapName);
        }

        private void OpenMapDialog_Load(object sender, EventArgs e)
        {
            this.mapListComboBox.SelectedIndex = 0;
        }
    }
}