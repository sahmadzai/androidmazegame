package edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.R;

public class GraphicsWrapper extends View {

    private Canvas canvas;
    private Paint paint;
    private Bitmap bitmap;
    private final BitmapShader wallShader;
    private final BitmapShader skyShader;
    private final BitmapShader floorShader;
    private Matrix matrix;
    private static final String LOG_TAG = "GraphicsWrapper";
    private static final int BITMAP_WIDTH = Constants.VIEW_WIDTH;
    private static final int BITMAP_HEIGHT = Constants.VIEW_HEIGHT;
    private static final int SHADER_WIDTH = 400;
    private static final int SHADER_HEIGHT = 330;

    public GraphicsWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
        Bitmap wallBMP = BitmapFactory.decodeResource(context.getResources(), R.drawable.forest_walls);
        wallBMP = Bitmap.createScaledBitmap(wallBMP, SHADER_WIDTH + 325, SHADER_HEIGHT + 200, false);
        wallShader = new BitmapShader(wallBMP, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        Bitmap skyBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cloudy_sky);
        skyBitmap = Bitmap.createScaledBitmap(skyBitmap, SHADER_WIDTH, SHADER_HEIGHT, false);
        skyShader = new BitmapShader(skyBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Bitmap floorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.creepy_floor);
        floorBitmap = Bitmap.createScaledBitmap(floorBitmap, SHADER_WIDTH + 50, SHADER_HEIGHT + 50, true);
        floorShader = new BitmapShader(floorBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

    }

    /**
     * Initialize instance variables.
     */
    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmap = Bitmap.createBitmap(BITMAP_WIDTH, BITMAP_HEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    public void drawLine(int x1, int y1, int x2, int y2){
        canvas.drawLine(x1, y1, x2, y2, paint);
    }

    public void newGraphics() {
        init();
    }


    /**
     * Set the Graphics object's color.
     * @param color the name of the color
     */
    public void setColor (String color){
        switch(color){
            case "white":
                paint.setColor(Color.WHITE);
                break;
            case "black":
                paint.setColor(Color.BLACK);
                break;
            case "red":
                paint.setColor(Color.RED);
                break;
            case "orange":
                paint.setColor(Color.CYAN);
                break;
            case "yellow":
                paint.setColor(Color.YELLOW);
                break;
            case "blue":
                paint.setColor(Color.BLUE);
                break;
            case "gray":
                paint.setColor(Color.GRAY);
                break;
            case "dark gray":
                paint.setColor(Color.DKGRAY);
        }
    }

    /**
     * Set the Paint object's color.
     * @param colorArray The color to set it to
     */
    public void setColor(int[] colorArray){
        paint.setColor(Color.rgb(colorArray[0], colorArray[1], colorArray[2]));
    }

    /**
     * Convert an RGB array to a single RGB int.
     * @param colorArray The input color array
     * @return A color int
     */
    public static int getRGB(int[] colorArray){
        return Color.rgb(colorArray[0], colorArray[1], colorArray[2]);
    }

    /**
     * Convert an RGB int to an RGB array.
     * @param colorInt The color int
     * @return A color array
     */
    public static int[] getRGBArray(int colorInt) {
        int[] colorArray = new int[3];
        colorArray[0] = Color.red(colorInt);
        colorArray[1] = Color.green(colorInt);
        colorArray[2] = Color.blue(colorInt);
        return colorArray;
    }

    /**
     * Make a rectangle in the Graphics object.
     * @param x the x value of the top left corner
     * @param y the y value of the top left corner
     * @param width the width
     * @param height the height
     */
    public void fillRect(int x, int y, int width, int height) {
//		paint.setShader(shader);
        canvas.drawRect(x, y, x + width, y + height, paint);
//		paint.setShader(null);
    }

    public void fillSky(int x, int y, int width, int height){
        paint.setShader(skyShader);
        fillRect(x, y, width, height);
        paint.setShader(null);
    }

    public void fillFloor(int x, int y, int width, int height){
        paint.setShader(floorShader);
        fillRect(x, y, width, height);
        paint.setShader(null);
    }

    /**
     * Calls the Graphics' objects fillPolygon method
     * @param xps -- array of x coordinates
     * @param yps -- array of y coordinates
     * @param npoints -- the total number of points
     */
    public void fillPolygon(int[] xps, int[] yps, int npoints){
        paint.setShader(wallShader);
        int i;
        Path path = new Path();
        path.moveTo(xps[0], yps[0]);
        for (i = 1; i < xps.length; i++){
            path.lineTo(xps[i], yps[i]);
        }
        canvas.drawPath(path, paint);
        paint.setShader(null);
    }

    public void fillOval(int x, int y, int width, int height){
        RectF oval = new RectF(x, y, x + width, y + height);
        canvas.drawOval(oval, paint);
    }

    public void measureDimensions() {
        int layoutWidth = this.getMeasuredWidth();
        int layoutHeight = this.getMeasuredHeight();
        Log.v(LOG_TAG, "Layout width: " + layoutWidth);
        Log.v(LOG_TAG, "Layout height: " + layoutHeight);
        init();
    }

    @Override
    public void onDraw(Canvas canvas){
        if (bitmap != null){
            super.onDraw(canvas);
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }
    }

}