package ro.calin.game.tetris;

/**
 * The Canvas where the tetris game is drawn.
 *
 * It must expose the following:
 * <ul>
 *     <li>a 30 x 70 bricks play area</li>
 *     <li>a 4 x 4 bricks next piece area</li>
 *     <li>a place where to draw the score</li>
 *     <li>a place where to draw the level</li>
 * </ul>
 *
 */
public interface TetrisCanvas {
    void drawScore(int score);
    void drawLevel(int level);

    void drawPlayArea();
    void drawNextPieceArea();

    void clearRegionInPlayArea(int x, int y, int width, int height);
    void clearRegionInNextPieceArea(int x, int y, int width, int height);

    void drawBrickInPlayArea(int x, int y);
    void drawBrickInNextPieceArea(int x, int y);
}
