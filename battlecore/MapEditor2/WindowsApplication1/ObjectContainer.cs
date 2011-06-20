using System;
using System.Collections;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Text;
using System.Windows.Forms;

namespace MapEditor2
{
    class ObjectContainer : ScrollableControl
    {
        /* SPACE BETWEEN TILES */
        private const int PADDING = 3;
        /* SPACE BETWEEN TILES */

        ArrayList theObjects = new ArrayList();

        private int selectedObjectX = PADDING;
        private int selectedObjectY = PADDING;
        private int selectedObject = 0;

        public int SelectedObject
        {
            get
            {
                return selectedObject;
            }
        }

        public ObjectContainer()
        {
            this.SetStyle(
            ControlStyles.UserPaint |
            ControlStyles.AllPaintingInWmPaint |
            ControlStyles.OptimizedDoubleBuffer, true);

            this.Cursor = System.Windows.Forms.Cursors.Hand;
            this.AutoScrollMinSize = new Size(this.Width, PADDING);
        }

        protected override void Dispose(bool disposing)
        {
            base.Dispose(disposing);

            theObjects.Clear();
        }

        protected override void OnPaint(PaintEventArgs pe)
        {

            /* TRABSLATE TO SCROLL POSITION */
            pe.Graphics.TranslateTransform(AutoScrollPosition.X, AutoScrollPosition.Y);
            /* TRABSLATE TO SCROLL POSITION */

            int y = PADDING;

            for (int i = 0; i < theObjects.Count; i++)
            {
                pe.Graphics.DrawImage((Image)theObjects[i], 
                    new Rectangle(((i % 2) == 0) ? PADDING : (2 * PADDING + ((Image)theObjects[i - 1]).Width), y,  ((Image)theObjects[i]).Width, ((Image)theObjects[i]).Height),
                    0, 0, ((Image)theObjects[i]).Width, ((Image)theObjects[i]).Height, 
                    GraphicsUnit.Pixel, Utils.imageAttributes);
                if ((i % 2) == 1)
                    y += (PADDING + (((Image)theObjects[i]).Height > ((Image)theObjects[i - 1]).Height ? 
                        ((Image)theObjects[i]).Height : ((Image)theObjects[i - 1]).Height));
            }

            /* DRAW A RED RECTANGLE FOR THE SELECTED OBJECT */
            if (theObjects.Count != 0)
            {
                pe.Graphics.DrawRectangle(new Pen(Color.Red), new Rectangle(selectedObjectX - 1, selectedObjectY - 1,
                    ((Image)theObjects[selectedObject]).Width + 1, ((Image)theObjects[selectedObject]).Height + 1));
                pe.Graphics.DrawRectangle(new Pen(Color.Red), new Rectangle(selectedObjectX - 2, selectedObjectY - 2,
                    ((Image)theObjects[selectedObject]).Width + 3, ((Image)theObjects[selectedObject]).Height + 3));
                pe.Graphics.DrawRectangle(new Pen(Color.Red), new Rectangle(selectedObjectX - 3, selectedObjectY - 3,
                    ((Image)theObjects[selectedObject]).Width + 5, ((Image)theObjects[selectedObject]).Height + 5));
            }
            /* DRAW A RED RECTANGLE FOR THE SELECTED OBJECT */
        }

        protected override void OnMouseClick(MouseEventArgs e)
        {
            if (e.Button == MouseButtons.Right)
            {
                return;
            }

            Point mouseLocation = new Point();

            mouseLocation.X = e.X - AutoScrollPosition.X;
            mouseLocation.Y = e.Y - AutoScrollPosition.Y;

            int y = PADDING;

            int i;
            for (i = 1; i < theObjects.Count; i += 2)
            {
                selectedObjectY = y;
                y += (PADDING + (((Image)theObjects[i]).Height > ((Image)theObjects[i - 1]).Height ?
                        ((Image)theObjects[i]).Height : ((Image)theObjects[i - 1]).Height));

                if (mouseLocation.Y >= selectedObjectY && mouseLocation.Y < y)
                {
                    if (mouseLocation.X < this.Width / 2)
                    {
                        selectedObjectX = PADDING;
                        selectedObject = i - 1;
                    }
                    else
                    {
                        selectedObjectX = (2 * PADDING + ((Image)theObjects[i - 1]).Width);
                        selectedObject = i;
                    }
                    break;
                }

                if (theObjects.Count == i + 2)
                {
                    selectedObjectY = y;
                    y += PADDING + ((Image)theObjects[i + 1]).Height;

                    if (mouseLocation.Y >= selectedObjectY && mouseLocation.Y < y)
                    {
                        selectedObjectX = PADDING;
                        selectedObject = i + 1;
                    }
                }
            }

            Invalidate();
        }

        public void AddObject(Image objectImage)
        {
            Image newImage;
      
            newImage = new Bitmap(objectImage, (this.Width - 3 * PADDING) / 2,
                objectImage.Height * ((this.Width - 3 * PADDING) / 2) / objectImage.Width);
           
            theObjects.Add(newImage);

            if (theObjects.Count % 2 == 0)
            {
                int i = theObjects.Count - 1;

                if (((Image)theObjects[i]).Height > ((Image)theObjects[i - 1]).Height)
                {
                    this.AutoScrollMinSize = new Size(this.Width, this.AutoScrollMinSize.Height +
                        ((Image)theObjects[i]).Height - ((Image)theObjects[i - 1]).Height);
                }
            }
            else
                this.AutoScrollMinSize = new Size(this.Width, this.AutoScrollMinSize.Height +
                    ((Image)theObjects[theObjects.Count - 1]).Height + PADDING);

            Invalidate();
        }

        public void AddObjectList(ArrayList objectImageList)
        {
            for (int i = 0; i < objectImageList.Count; i++)
                this.AddObject((Image)objectImageList[i]);
        }

        public void Clear()
        {
            theObjects.Clear();
            this.AutoScrollMinSize = new Size(this.Width, PADDING);
        }
    }
}
