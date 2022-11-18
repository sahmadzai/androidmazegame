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

public class CustomView_Manual extends View {

    private Rect maze_rect;
    private RectF circle;
    private Paint maze_rect_paint;

    public CustomView_Manual(Context context) {
        super(context);
        init(null);
    }

    public CustomView_Manual(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(null);
    }

    public CustomView_Manual(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(null);
    }

    private void init(@Nullable AttributeSet set) {
        maze_rect = new Rect();
        circle = new RectF();
        maze_rect_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        maze_rect.top = 0;
        maze_rect.left = getWidth();
        maze_rect.bottom = getHeight()/2;
        maze_rect_paint.setColor(Color.GRAY);
        canvas.drawRect(maze_rect, maze_rect_paint);

        maze_rect.top = getHeight()/2;
        maze_rect.left = getWidth();
        maze_rect.bottom = getHeight();
        maze_rect_paint.setColor(Color.BLACK);
        canvas.drawRect(maze_rect, maze_rect_paint);

        circle.top    = (float) getHeight()/2 - 50;
        circle.left   = (float) getWidth()/2 - 50;
        circle.right  = (float) getWidth()/2 + 50;
        circle.bottom = (float) getHeight()/2 + 50;
        maze_rect_paint.setColor(Color.RED);
        canvas.drawOval(circle, maze_rect_paint);
    }
}
