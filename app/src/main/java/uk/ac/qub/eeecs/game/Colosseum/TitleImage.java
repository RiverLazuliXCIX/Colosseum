package uk.ac.qub.eeecs.game.Colosseum;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.GraphicsHelper;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

public class TitleImage extends GameObject {

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    //Creation of a new title image

    /**
     * @param x             Centre y location of the title image
     * @param y             Centre x location of the title image
     * @param width         Width of the title image
     * @param height        Height of the title image
     * @param defaultBitmap Base bitmap used to draw this title image
     * @param gameScreen    Gamescreen to which this title image belongs
     */
    public TitleImage(float x, float y, float width, float height, String defaultBitmap, GameScreen gameScreen) {
        super(x, y, width, height, gameScreen.getGame().getAssetManager()
                .getBitmap(defaultBitmap), gameScreen);
    }

    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport) {

        // Call the getBound method to make sure we're using an up-to-date bound
        BoundingBox bound = getBound();

        // Only draw if it is visible
        if (GraphicsHelper.isVisible(bound, layerViewport)) {
            if (GraphicsHelper.getClippedSourceAndScreenRect(
                    bound, mBitmap, layerViewport, screenViewport, drawSourceRect, drawScreenRect)) {
                graphics2D
                        .drawBitmap(mBitmap, drawSourceRect, drawScreenRect, null);
            }
        }
    }
}