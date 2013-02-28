package ro.calin.game.tetris;

/**
 * The Canvas where the tetris game is drawn.
 *
 * It must expose the following:
 * <ul>
 *     <li>a 10 x 18 bricks play area</li>
 *     <li>a 4 x 4 bricks next piece area</li>
 *     <li>a place where to draw the score</li>
 *     <li>a place where to draw the level</li>
 * </ul>
 *
 */
public interface TetrisCanvas {

    public static class Const {
        public static int PLAY_AREA_WIDTH = 10;
        public static int PLAY_AREA_HEIGHT = 18;
        public static int NEXT_PIECE_AREA_WIDTH = 4;
        public static int NEXT_PIECE_AREA_HEIGHT = 4;
    }

    void clearScreen();
    void drawScore(int score);
    void drawLevel(int level);

    void drawPlayArea();
    void drawNextPieceArea();

    void drawBrickInPlayArea(int line, int col);
    void drawBrickInNextPieceArea(int line, int col);
}
