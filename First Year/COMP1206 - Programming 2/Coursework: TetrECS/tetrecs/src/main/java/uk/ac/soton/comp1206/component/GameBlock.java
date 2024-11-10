package uk.ac.soton.comp1206.component;

import javafx.animation.AnimationTimer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Visual User Interface component representing a single block in the grid.
 *
 * Extends Canvas and is responsible for drawing itself.
 *
 * Displays an empty square (when the value is 0) or a coloured square depending on value.
 *
 * The GameBlock value should be bound to a corresponding block in the Grid model.
 */
public class GameBlock extends Canvas {

    private static final Logger logger = LogManager.getLogger(GameBlock.class);

    /**
     * The set of colours for different pieces
     */
    public static final Color[] COLOURS = {
            Color.TRANSPARENT,
            Color.DEEPPINK,
            Color.RED,
            Color.ORANGE,
            Color.YELLOW,
            Color.YELLOWGREEN,
            Color.LIME,
            Color.GREEN,
            Color.DARKGREEN,
            Color.DARKTURQUOISE,
            Color.DEEPSKYBLUE,
            Color.AQUA,
            Color.AQUAMARINE,
            Color.BLUE,
            Color.MEDIUMPURPLE,
            Color.PURPLE
    };

    private final GameBoard gameBoard;

    private final double width;
    private final double height;

    /**
     * The column this block exists as in the grid
     */
    private final int x;

    /**
     * The row this block exists as in the grid
     */
    private final int y;

    /**
     * The value of this block (0 = empty, otherwise specifies the colour to render as)
     */
    private final IntegerProperty value = new SimpleIntegerProperty(0);

    /**
     * Create a new single Game Block
     * @param gameBoard the board this block belongs to
     * @param x the column the block exists in
     * @param y the row the block exists in
     * @param width the width of the canvas to render
     * @param height the height of the canvas to render
     */
    public GameBlock(GameBoard gameBoard, int x, int y, double width, double height) {
        this.gameBoard = gameBoard;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;

        //A canvas needs a fixed width and height
        setWidth(width);
        setHeight(height);

        //Do an initial paint
        paint();

        //When the value property is updated, call the internal updateValue method
        value.addListener(this::updateValue);
    }

    /**
     * When the value of this block is updated,
     * @param observable what was updated
     * @param oldValue the old value
     * @param newValue the new value
     */
    private void updateValue(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        paint();
    }

    /**
     * Handle painting of the block canvas
     */
    public void paint() {
        //If the block is empty, paint as empty
        if(value.get() == 0) {
            paintEmpty();
        } else {
            //If the block is not empty, paint with the colour represented by the value
            paintColor(COLOURS[value.get()]);
        }
    }

    /**
     * Paint this canvas empty
     */
    private void paintEmpty() {
        var gc = getGraphicsContext2D();

        //Clear
        gc.clearRect(0,0,width,height);

        //Fill
        gc.setFill(new Color(1, 1, 1, 0.75));
        gc.fillRect(0,0, width, height);

        //Border
        gc.setStroke(Color.LIGHTGREY);
        gc.strokeRect(0,0,width,height);
    }

    /**
     * Paint this canvas with the given colour which is turned into an image
     * @param colour the colour to paint
     */
    private void paintColor(Paint colour) {
        var gc = getGraphicsContext2D();

        String file = null;

        //Clear
        gc.clearRect(0,0,width,height);

        switch (colour.toString()) {
            case "0xff1493ff":
                file = getClass().getResource("/images/11.png").toExternalForm();
                break;
            case "0xff0000ff":
                file = getClass().getResource("/images/12.png").toExternalForm();
                break;
            case "0xffa500ff":
                file = getClass().getResource("/images/13.png").toExternalForm();
                break;
            case "0xffff00ff":
                file = getClass().getResource("/images/14.png").toExternalForm();
                break;
            case "0x9acd32ff":
                file = getClass().getResource("/images/15.png").toExternalForm();
                break;
            case "0x00ff00ff":
                file = getClass().getResource("/images/16.png").toExternalForm();
                break;
            case "0x008000ff":
                file = getClass().getResource("/images/17.png").toExternalForm();
                break;
            case "0x006400ff":
                file = getClass().getResource("/images/18.png").toExternalForm();
                break;
            case "0x00ced1ff":
                file = getClass().getResource("/images/19.png").toExternalForm();
                break;
            case "0x00bfffff":
                file = getClass().getResource("/images/20.png").toExternalForm();
                break;
            case "0x00ffffff":
                file = getClass().getResource("/images/21.png").toExternalForm();
                break;
            case "0x7fffd4ff":
                file = getClass().getResource("/images/22.png").toExternalForm();
                break;
            case "0x0000ffff":
                file = getClass().getResource("/images/23.png").toExternalForm();
                break;
            case "0x9370dbff":
                file = getClass().getResource("/images/24.png").toExternalForm();
                break;
            case "0x800080ff":
                file = getClass().getResource("/images/25.png").toExternalForm();
                break;
        }

        var image = new Image(file);
        gc.setFill(new ImagePattern(image));

        gc.setGlobalAlpha(1);
        gc.fillRect(0,0, width, height);

        //Colour fill
        //gc.setFill(colour);
        //gc.setLineWidth(1);
        //gc.fillRect(0,0, width, height);

        //Border
        gc.setStroke(Color.LIGHTGREY);
        gc.strokeRect(0,0,width,height);
    }

    /**
     * Get the column of this block
     * @return column number
     */
    public int getX() {
        return x;
    }

    /**
     * Get the row of this block
     * @return row number
     */
    public int getY() {
        return y;
    }

    /**
     * Get the current value held by this block, representing it's colour
     * @return value
     */
    public int getValue() {
        return this.value.get();
    }

    /**
     * Bind the value of this block to another property. Used to link the visual block to a corresponding block in the Grid.
     * @param input property to bind the value to
     */
    public void bind(ObservableValue<? extends Number> input) {
        value.bind(input);
    }

    /**
     * Being able to see the value of this block from another property.
     */
    public String toString() {
        return "GameBlock{" +
            "x=" + x +
            ", y=" + y +
            ", value=" + value.get() +
            '}';
    }

    /**
     * Amend a block to be used a toggle for the GameBoard
     */
    public void setToggle(boolean toggle) {
        var gc = getGraphicsContext2D();
        gc.clearRect(0,0,width,height);
        gc.setFill(new Color(1, 1, 1, 0.75));
    }

    /**
     * Fade out the blocks when a line is cleared
     * Uses an AnimationTimer to fade out the blocks and initial opacity of 1.0
     * It decreases the opacity by a small amount each frame
     * When the opacity reaches 0, the animation stops
     */
    public void fadeOut() {
        logger.info("Fading out blocks with fadeOut()");
        AnimationTimer timer = new AnimationTimer() {
            private double opacity = 1.0;

            public void handle(long now) {
                var gc = getGraphicsContext2D();
                gc.clearRect(0,0,width,height);
                gc.setFill(new Color(1, 1, 1, opacity));
                gc.fillRect(0,0, width, height);
                gc.setStroke(Color.LIGHTGREY);
                gc.strokeRect(0,0,width,height);
                opacity -= 0.02;
                if (opacity <= 0) {
                    stop();
                    cleanHighlight();
                }
            }
        };
        timer.start();
    }

    /**
     * Create a circle on the block
     */
    public void createCircle() {
        var gc = getGraphicsContext2D();
        gc.setLineWidth(2);
        gc.setStroke(Color.WHITESMOKE);
        gc.setFill(Color.WHITESMOKE);
        gc.strokeOval(9, 9, 50, 50);
        gc.fillOval(9, 9, 50, 50);
    }

    /**
     * Highlight the block
     */
    public void createHighlight() {
        var gc = getGraphicsContext2D();
        gc.clearRect(0,0,width,height);
        gc.setFill(new Color(1, 1, 1, 0.75));
    }

    /**
     * Unhighlight the block
     */
    public void cleanHighlight() {
        var gc = getGraphicsContext2D();
        gc.clearRect(0,0,width,height);
        paint();
    }
}
