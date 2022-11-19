package edu.wm.cs.cs301.amazebyshamsullahahmadzai;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomView_Anim extends View {

    /**
     * Class variables for the custom view that will be used to draw the rectangles and circle, and the Paint object.
     * This is good for performance, as onDraw() may be called many times and it'll be faster to create the objects once.
     */
    private Rect maze_rect;
    private RectF circle;
    private Paint maze_rect_paint;

    /**
     * Default constructor for the custom view.
     * @param context - the context of the activity
     */
    public CustomView_Anim(Context context) {
        super(context);
        init(null);
    }

    /**
     * Constructor for the custom view that takes in a null attribute set and a context
     * which is then passed to the default constructor of the super class.
     * @param context - the context of the activity
     * @param attrs - null attribute set
     */
    public CustomView_Anim(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(null);
    }

    /**
     * Constructor for the custom view that takes in a null attribute set, a context, and a default style
     * which is then passed to the default constructor of the super class.
     * @param context - the context of the activity
     * @param attrs - null attribute set
     * @param defStyleAttr - default style attribute
     */
    public CustomView_Anim(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(null);
    }

    /**
     * Init method that initializes the class variables that will be used in onDraw().
     * @param set - null attribute set
     */
    private void init(@Nullable AttributeSet set) {
        maze_rect = new Rect();
        circle = new RectF();
        maze_rect_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * onDraw method that draws the rectangles and circle onto the "screen" or canvas.
     * @param canvas - the canvas that the rectangles and circle will be drawn on
     */
    @Override
    protected void onDraw(Canvas canvas) {
        maze_rect.top = 0;                                  // top of the rectangle is at the origin 0 
        maze_rect.left = getWidth();                        // left of the rectangle is at the width of the screen
        maze_rect.bottom = getHeight()/2;                   // bottom of the rectangle is at the height of the screen divided by 2
        maze_rect_paint.setColor(Color.GRAY);               // set the color of the rectangle to gray
        canvas.drawRect(maze_rect, maze_rect_paint);        // draw the rectangle onto the canvas

        maze_rect.top = getHeight()/2;                      // top is 1/2 of the height
        maze_rect.left = getWidth();                        // left is the width
        maze_rect.bottom = getHeight();                     // bottom stops at the rectangle height  
        maze_rect_paint.setColor(Color.BLACK);              // set the color to black
        canvas.drawRect(maze_rect, maze_rect_paint);        // draw the rectangle

        // Draw the maze wall left side
        maze_rect.top = 50;                                 // top is 50 pixels from the top of the screen
        maze_rect.left = getWidth()/4;                      // left is 1/4 of the width of the screen
        maze_rect.bottom = getHeight() - 50;                // bottom is 50 pixels from the bottom of the screen
        maze_rect_paint.setColor(Color.GREEN);              // set the color of the rectangle to green
        canvas.drawRect(maze_rect, maze_rect_paint);        // draw the rectangle

        // Draw the maze wall right side
        maze_rect.top = 50;                                 // top is 50 pixels from the top of the screen
        maze_rect.left = getWidth() - getWidth()/4;         // left is getWidth() - getWidth()/4 pixels from the left of the screen
        maze_rect.right = getWidth();                       // right is getWidth() pixels from the left of the screen
        maze_rect.bottom = getHeight() - 50;                // bottom is getHeight() - 50 pixels from the top of the screen
        maze_rect_paint.setColor(Color.YELLOW);             // set the color of the paint object to yellow
        canvas.drawRect(maze_rect, maze_rect_paint);        // draw the rectangle onto the canvas

        // Draw the red dot and center it in the custom view
        circle.top    = (float) getHeight()/2 - 50;         // top is getHeight()/2 - 50 pixels from the top of the screen
        circle.left   = (float) getWidth()/2 - 50;          // left is getWidth()/2 - 50 pixels from the left of the screen
        circle.right  = (float) getWidth()/2 + 50;          // right is getWidth()/2 + 50 pixels from the left of the screen
        circle.bottom = (float) getHeight()/2 + 50;         // bottom is getHeight()/2 + 50 pixels from the top of the screen
        maze_rect_paint.setColor(Color.RED);                // set the color of the paint object to red
        canvas.drawOval(circle, maze_rect_paint);           // draw the circle onto the canvas
    }
}
