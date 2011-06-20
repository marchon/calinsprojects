using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Text;
using System.Windows.Forms;

namespace MapEditor2
{
    public class TileContainer : ScrollableControl
    {
        /* SPACE BETWEEN TILES */
        private const int PADDING = 3;
        /* SPACE BETWEEN TILES */

        /* BITMAP WITH TILES */
        private Image tileBitmap = null;
        /* BITMAP WITH TILES */

        /* TILE DIMENSIONS AND NUBER OF TILES */
        private int tileWidth = 0;
        private int tileHeight = 0;
        private int numberOfTiles = 0;
        /* TILE DIMENSIONS AND NUBER OF TILES */
        
        /* AMOUNT OF TILES THAT CAN BE DRAWN ON THE SAME LINE */
        private int tilesPerLine = 0;
        /* AMOUNT OF TILES THAT CAN BE DRAWN ON THE SAME LINE */
        /* SPACE BETWEEN THE LEFT OF THE CONTROL AND THE TILES FROM THE FIRST COLUMN */
        private int space = 0;
        /* SPACE BETWEEN THE LEFT OF THE CONTROL AND THE TILES FROM THE FIRST COLUMN */

        /* CURRENT TILE */
        private int selectedTileX = 0;
        private int selectedTileY = 0;
        private int selectedTile = 0;
        /* CURRENT TILE */

        /* GETTER FOR SELECTED TILE */
        public int SelectedTile
        {
            get
            {
                return selectedTile;
            }
        }
        /* GETTER FOR SELECTED TILE */

        /* FLAG THAT INDICATES IF EVERYTHING IS INITIALIZED */
        private bool isInitialized = false;
        /* FLAG THAT INDICATES IF EVERYTHING IS INITIALIZED */

        /* CONSTRUCTOR */
        public TileContainer()
        {
            this.SetStyle(
            ControlStyles.UserPaint |
            ControlStyles.AllPaintingInWmPaint |
            ControlStyles.OptimizedDoubleBuffer, true);

            this.Cursor = System.Windows.Forms.Cursors.Hand;
        }
        /* CONSTRUCTOR */

        /* DISPOSE METHOD */
        protected override void Dispose(bool disposing)
        {
            base.Dispose(disposing);

            tileBitmap = null;
        }
        /* DISPOSE METHOD */

        /* PAINT HANDLER */
        protected override void OnPaint(PaintEventArgs pe)
        {
            if (!isInitialized)
                return;

            /* TRABSLATE TO SCROLL POSITION */
            pe.Graphics.TranslateTransform(AutoScrollPosition.X, AutoScrollPosition.Y);
            /* TRABSLATE TO SCROLL POSITION */

            /* DRAW ALL TILES */
            int row = 0;
            int column = 0;
            for (int j = 0; j < tileBitmap.Height / tileHeight; j++)
                for (int i = 0; i < tileBitmap.Width / tileWidth; i++)
                {                
                    pe.Graphics.DrawImage(tileBitmap,
                        new Rectangle(space + PADDING + column * (tileWidth + PADDING), PADDING + row * (tileHeight + PADDING), tileWidth, tileHeight),
                        i * tileWidth, j * tileHeight, tileWidth, tileHeight,
                        GraphicsUnit.Pixel);

                    column++;

                    if (column == tilesPerLine)
                    {
                        row++;
                        column = 0;
                    }
                }
            /* DRAW ALL TILES */

            /* DRAW A RED RECTANGLE FOR THE SELECTED TILE */
            pe.Graphics.DrawRectangle(new Pen(Color.Red), new Rectangle(selectedTileX - 1, selectedTileY - 1, tileWidth + 1, tileHeight + 1));
            pe.Graphics.DrawRectangle(new Pen(Color.Red), new Rectangle(selectedTileX - 2, selectedTileY - 2, tileWidth + 3, tileHeight + 3));
            pe.Graphics.DrawRectangle(new Pen(Color.Red), new Rectangle(selectedTileX - 3, selectedTileY - 3, tileWidth + 5, tileHeight + 5));
            /* DRAW A RED RECTANGLE FOR THE SELECTED TILE */
        }
        /* PAINT HANDLER */

        /* SET THE PARAMS/INITIALIZE */
        public void SetParameters(Image tb, int tw, int th)
        {
            if (tb == null || isInitialized)
                return;

            if (tb.Width % tw != 0 || tb.Height % th != 0)
            {
                throw (new Exception("Parsing bitmap error!")); 
            }

            /* SET THE PARAMS */
            tileBitmap = tb;
            tileWidth = tw;
            tileHeight = th;
            /* SET THE PARAMS */

            /* CALCULATE THE NEW PARAMS */
            tilesPerLine = (this.Width - PADDING) / (tileWidth + PADDING);
            if (tilesPerLine == 0)
                tilesPerLine = 1;

            space = (this.Width - ((tileWidth + PADDING) * tilesPerLine + PADDING)) / 2;
            if (space < 0)
                space = 0;

            numberOfTiles = (tileBitmap.Height / tileHeight) * (tileBitmap.Width / tileWidth);

            int rows = (int)(Math.Ceiling(((double)(numberOfTiles) / (double)(tilesPerLine))));
            int newHeight = PADDING + rows * (tileHeight + PADDING);

            selectedTileX = space + PADDING;
            selectedTileY = PADDING;
            selectedTile = 0;
            /* CALCULATE THE NEW PARAMS */

            /* SET SCROLL SIZE */
            this.AutoScrollMinSize = new Size(((tileWidth + 2 * (PADDING + space) < this.Width) ? this.Width : (tileWidth + 2 * (PADDING + space))), newHeight);
            this.Invalidate();
            /* SET SCROLL SIZE */

            /* SET FLAG */
            isInitialized = true;
            /* SET FLAG */
        }
        /* SET THE PARAMS/INITIALIZE */

        /* RESET THE PARAMS */
        public void ResetParameters()
        {
            if (!isInitialized)
                return;

            isInitialized = false;

            tileBitmap = null;

            tileWidth = 0;
            tileHeight = 0;
            numberOfTiles = 0;

            tilesPerLine = 0;
            space = 0;

            selectedTileX = 0;
            selectedTileY = 0;
            selectedTile = 0;

            this.AutoScrollPosition = new Point(0, 0);
        }
        /* RESET THE PARAMS */

        /* CLICK HANDLER */
        protected override void OnMouseClick(MouseEventArgs e)
        {
            if (!isInitialized)
                return;

            if (e.Button == MouseButtons.Right)
            {
                return;
            }

            Point mouseLocation = new Point();

            mouseLocation.X = e.X - AutoScrollPosition.X;
            mouseLocation.Y = e.Y - AutoScrollPosition.Y;

            int x = (mouseLocation.X - space - PADDING) / (tileWidth + PADDING);
            int y = (mouseLocation.Y - PADDING) / (tileHeight + PADDING);

            if (y * tilesPerLine + x < numberOfTiles)
            {
                selectedTile = y * tilesPerLine + x;

                selectedTileX = ((mouseLocation.X - space - PADDING) / (tileWidth + PADDING)) * (tileWidth + PADDING) + space + PADDING;
                selectedTileY = ((mouseLocation.Y - PADDING) / (tileHeight + PADDING)) * (tileHeight + PADDING) + PADDING;

                this.Invalidate();
            }
        }
        /* CLICK HANDLER */
    }
}
